package dev.angel.impl.manager;

import dev.angel.Swish;
import dev.angel.api.command.Command;
import dev.angel.api.event.bus.Listener;
import dev.angel.api.event.bus.SubscriberImpl;
import dev.angel.api.module.Module;
import dev.angel.api.util.text.TextColor;
import dev.angel.api.value.*;
import dev.angel.impl.command.*;
import dev.angel.api.util.logging.Logger;
import dev.angel.impl.events.PacketEvent;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CommandManager extends SubscriberImpl {
    private String prefix = ",";
    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {
        this.listeners.add(new Listener<PacketEvent.Send<ChatMessageC2SPacket>>(PacketEvent.Send.class, Integer.MIN_VALUE, ChatMessageC2SPacket.class) {
            @Override
            public void call(PacketEvent.Send<ChatMessageC2SPacket> event) {
                String message = event.getPacket().chatMessage();
                if (message.startsWith(getPrefix())) {
                    boolean factoid = true;
                    event.setCanceled(factoid);
                    boolean exists = false;
                    String[] args = message.split(" ");
                    if (message.length() < 2) {
                        Logger.getLogger().log("No command was entered");
                        return;
                    }

                    String input = message.split(" ")[0].substring(1);
                    for (Command command : getCommands()) {
                        for (String alias : command.getAlias()) {
                            if (!input.replace(getPrefix(), "").equalsIgnoreCase(alias.replaceAll(" ", ""))) {
                                continue;
                            }
                            exists = true;

                            try {
                                command.run(args);
                            } catch (Exception e) {
                                Logger.getLogger().log("Error while handling command: " + e.getMessage());
                            }
                        }
                    }

                    for (Module mod : Swish.getModuleManager().getModules()) {
                        for (String alias : mod.getAliases()) {
                            try {
                                if (!args[0].equalsIgnoreCase(getPrefix() + alias.replace(" ", ""))) {
                                    continue;
                                }
                                exists = true;
                                if (args.length > 1) {
                                    String valueName = args[1];
                                    if (args[1].equalsIgnoreCase("list")) {
                                        if (!mod.getValues().isEmpty()) {
                                            StringJoiner stringJoiner = new StringJoiner(", ");
                                            for (Value<?> value : mod.getValues()) {

                                                if (value instanceof ColorValue colorValue) {
                                                    boolean isGlobal = colorValue.isGlobal();
                                                    if (!isGlobal) {
                                                        stringJoiner.add(String.format("%s, %s(Red %s, Green %s, Blue %s, Alpha %s)%s",
                                                                colorValue.getLabel(),
                                                                TextColor.GREEN,
                                                                colorValue.getColor().getRed(),
                                                                colorValue.getColor().getGreen(),
                                                                colorValue.getColor().getBlue(),
                                                                colorValue.getColor().getAlpha(),
                                                                TextColor.LIGHT_PURPLE));
                                                        continue;
                                                    }
                                                    stringJoiner.add(String.format("%s [Global]", colorValue.getLabel()));
                                                    continue;
                                                }

                                                if (value instanceof BindValue bindValue) {
                                                    stringJoiner.add(String.format("Keybind [%s]", bindValue.getValue().getName()));
                                                    continue;
                                                }

                                                stringJoiner.add(String.format("%s [%s]", value.getLabel(), value.getValue() instanceof Enum ? ((EnumValue<?>) value).getFixedValue() : value.getValue()));
                                            }
                                            Logger.getLogger().log(String.format("Values (%s) %s", mod.getValues().size(), stringJoiner));
                                            continue;
                                        }
                                        Logger.getLogger().log(String.format("%s%s%s has no values", TextColor.DARK_AQUA, mod.getLabel(), TextColor.LIGHT_PURPLE));
                                        continue;
                                    }

                                    Value val = mod.getValue(valueName);

                                    if (val == null) {
                                        Logger.getLogger().log("That value does not exist");
                                        continue;
                                    }

                                    if (val.getValue() instanceof Number) {
                                        if (!args[2].equalsIgnoreCase("get")) {
                                            if (val.getValue() instanceof Double) {
                                                val.setValue(Double.parseDouble(args[2]));
                                            }
                                            if (val.getValue() instanceof Integer) {
                                                val.setValue(Integer.parseInt(args[2]));
                                            }
                                            if (val.getValue() instanceof Float) {
                                                val.setValue(Float.parseFloat(args[2]));
                                            }
                                            if (val.getValue() instanceof Long) {
                                                val.setValue(Long.parseLong(args[2]));
                                            }
                                            setMessage(mod, val, val.getValue());
                                            continue;
                                        }
                                        currentMessage(mod, val, val.getValue());
                                        continue;
                                    }

                                    if (val.getValue() instanceof Enum) {
                                        if (!args[2].equalsIgnoreCase("list")) {
                                            ((EnumValue<?>) val).setValueFromString(args[2]);
                                            setMessage(mod, val, ((EnumValue<?>) val).getFixedValue());
                                            continue;
                                        }
                                        StringJoiner stringJoiner = new StringJoiner(", ");
                                        Enum<?>[] array = (Enum<?>[]) val.getValue().getClass().getEnumConstants();
                                        for (Enum<?> enumArray : array) {
                                            stringJoiner.add(String.format("%s%s", enumArray.name().equalsIgnoreCase(val.getValue().toString()) ?
                                                    TextColor.GREEN : TextColor.RED, getFixedValue(enumArray) + TextColor.LIGHT_PURPLE));
                                        }
                                        Logger.getLogger().log(String.format("%s%s%s val %s%s%s modes (%s) %s",
                                                TextColor.DARK_AQUA,
                                                mod.getLabel(),
                                                TextColor.LIGHT_PURPLE,
                                                TextColor.AQUA,
                                                val.getLabel(),
                                                TextColor.LIGHT_PURPLE,
                                                array.length,
                                                stringJoiner));
                                        continue;
                                    }

                                    if (val.getValue() instanceof StringValue) {
                                        String str = String.join(" ", args);
                                        str = str.replace(args[0] + " ", "").replace(args[1] + " ", ""); //ZENOV MODE
                                        val.setValue(str);
                                        setMessage(mod, val, val.getValue());
                                        continue;
                                    }

                                    if (val.getValue() instanceof Boolean) {
                                        val.setValue(!((Boolean) val.getValue()));
                                        Logger.getLogger().log(String.format("%s%s%s value %s%s%s was %s",
                                                TextColor.DARK_AQUA,
                                                mod.getLabel(),
                                                TextColor.LIGHT_PURPLE,
                                                TextColor.AQUA,
                                                val.getLabel(),
                                                TextColor.LIGHT_PURPLE,
                                                (Boolean) val.getValue() ? TextColor.GREEN + "enabled" : TextColor.RED + "disabled"));
                                        continue;
                                    }

                                    if (val instanceof ColorValue) {
                                        //noinspection PatternVariableCanBeUsed
                                        ColorValue colorProperty = (ColorValue) val;
                                        int red = colorProperty.getValue().getRed();
                                        int green = colorProperty.getValue().getGreen();
                                        int blue = colorProperty.getValue().getBlue();
                                        int alpha = colorProperty.getValue().getAlpha();
                                        boolean isGlobal = colorProperty.isGlobal();
                                        if (args.length > 2) {
                                            try {
                                                switch (args[2].toUpperCase()) {
                                                    case ("R"), ("RED") -> {
                                                        int colorValue = Integer.parseInt(args[3]);
                                                        colorProperty.setValue(new Color(colorValue, green, blue, alpha));
                                                        setColorMessage(mod, val, "red", colorValue);
                                                    }
                                                    case ("G"), ("GREEN") -> {
                                                        int colorValue = Integer.parseInt(args[3]);
                                                        colorProperty.setValue(new Color(red, colorValue, blue, alpha));
                                                        setColorMessage(mod, val, "green", colorValue);
                                                    }
                                                    case ("B"), ("BLUE") -> {
                                                        int colorValue = Integer.parseInt(args[3]);
                                                        colorProperty.setValue(new Color(red, green, colorValue, alpha));
                                                        setColorMessage(mod, val, "blue", colorValue);
                                                    }
                                                    case ("A"), ("ALPHA") -> {
                                                        int colorValue = Integer.parseInt(args[3]);
                                                        colorProperty.setValue(new Color(red, green, blue, colorValue));
                                                        setColorMessage(mod, val, "alpha", colorValue);
                                                    }
                                                    case ("S"), ("SET") -> {
                                                        int redValue = Integer.parseInt(args[3]);
                                                        int greenValue = Integer.parseInt(args[4]);
                                                        int blueValue = Integer.parseInt(args[5]);
                                                        if (args.length == 6) {
                                                            colorProperty.setValue(new Color(redValue, greenValue, blueValue, alpha));
                                                            final String colorStr = String.format("Red: %s Green: %s Blue: %s", redValue, greenValue, blueValue);
                                                            setMessage(mod, val, colorStr);
                                                        } else {
                                                            int alphaValue = Integer.parseInt(args[6]);
                                                            colorProperty.setValue(new Color(redValue, greenValue, blueValue, alphaValue));
                                                            final String colorStr = String.format("Red: %s Green: %s Blue: %s Alpha: %s", redValue, greenValue, blueValue, alphaValue);
                                                            setMessage(mod, val, colorStr);
                                                        }
                                                    }
                                                    case ("SYNC"), ("GLOBAL") -> {
                                                        colorProperty.setGlobal(!isGlobal);
                                                        Logger.getLogger().log(String.format("%s%s%s property %s%s%s Global was %s",
                                                                TextColor.DARK_AQUA,
                                                                mod.getLabel(),
                                                                TextColor.LIGHT_PURPLE,
                                                                TextColor.AQUA,
                                                                val.getLabel(),
                                                                TextColor.LIGHT_PURPLE,
                                                                !isGlobal ? TextColor.GREEN + "enabled" : TextColor.RED + "disabled"));
                                                    }
                                                    case ("C"), ("COPY") -> {
                                                        String hex = String.format("#%02x%02x%02x%02x", alpha, red, green, blue);
                                                        StringSelection selection = new StringSelection(hex);
                                                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                                                        clipboard.setContents(selection, selection);
                                                        Logger.getLogger().log("Copied colour to clipboard");
                                                    }
                                                    case ("P"), ("PASTE") -> {
                                                        try {
                                                            if (readClipboard() != null) {
                                                                if (readClipboard().startsWith("#")) {
                                                                    String hex = readClipboard();

                                                                    int a = Integer.valueOf(hex.substring(1, 3), 16);
                                                                    int r = Integer.valueOf(hex.substring(3, 5), 16);
                                                                    int g = Integer.valueOf(hex.substring(5, 7), 16);
                                                                    int b = Integer.valueOf(hex.substring(7, 9), 16);

                                                                    colorProperty.setValue(new Color(r, g, b, a));
                                                                    colorProperty.setValue(new Color(r, g, b, a));
                                                                    Logger.getLogger().log(String.format("Colour pasted in property %s", val.getLabel()));
                                                                    continue;
                                                                } else {
                                                                    String[] color = readClipboard().split(",");
                                                                    Color colorValue = new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
                                                                    colorProperty.setValue(colorValue);
                                                                    colorProperty.setValue(colorValue);
                                                                    Logger.getLogger().log(String.format("Colour pasted in property %s", val.getLabel()));
                                                                    continue;
                                                                }
                                                            }
                                                        } catch (NumberFormatException e) {
                                                            Logger.getLogger().log("Bad colour format");
                                                            continue;
                                                        }
                                                    }
                                                }
                                                continue;

                                            } catch (Exception e) {
                                                Logger.getLogger().log("Invalid action");
                                                return;
                                            }
                                        }

                                        Logger.getLogger().log(String.format("%s %s(Red %s, Green %s, Blue %s, Alpha %s)%s [Global %s]", colorProperty.getLabel(), TextColor.GREEN, red, green, blue, alpha, TextColor.LIGHT_PURPLE, isGlobal));
                                        continue;
                                    }

                                }
                                Logger.getLogger().log(String.format("%s [list|value] [list|get]", args[0]));
                            } catch (Exception e) {
                                Logger.getLogger().log("Unknown error");
                            }
                        }
                    }

                    if (!exists) {
                        Logger.getLogger().log("Unknown command");
                    }
                }
            }
        });

        register(new PrefixCommand());
        register(new HelpCommand());
        register(new ToggleCommand());
        register(new VelocityPercentageCommand());
        register(new SetKeyBindCommand());
        register(new FakePlayerCommand());
        register(new OpenFolderCommand());
        register(new ClearPopsCommand());
        register(new ConfigCommand());
        register(new FriendCommand());
    }

    public void register(Command command) {
        getCommands().add(command);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private void currentMessage(Module module, Value<?> val, Object value) {
        Logger.getLogger().log(TextColor.DARK_AQUA
                + module.getLabel()
                + TextColor.LIGHT_PURPLE
                + " property "
                + TextColor.AQUA
                + val.getLabel()
                + TextColor.LIGHT_PURPLE
                + " current value is "
                + TextColor.GREEN + value
        );
    }

    private void setColorMessage(Module module, Value<?> val, String color, Object value) {
        Logger.getLogger().log(TextColor.DARK_AQUA
                + module.getLabel()
                + TextColor.LIGHT_PURPLE
                + " property "
                + TextColor.AQUA
                + val.getLabel()
                + TextColor.LIGHT_PURPLE
                + " "
                + color
                + " was set to "
                + TextColor.GREEN + value
        );
    }

    private void setMessage(Module module, Value<?> val, Object value) {
        Logger.getLogger().log(TextColor.DARK_AQUA
                + module.getLabel()
                + TextColor.LIGHT_PURPLE
                + " property "
                + TextColor.AQUA
                + val.getLabel()
                + TextColor.LIGHT_PURPLE
                + " was set to "
                + TextColor.GREEN + value
        );
    }

    private String readClipboard() {
        try {
            return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        }
        catch (UnsupportedFlavorException | IOException exception) {
            return null;
        }
    }

    private String getFixedValue(Enum<?> enumValue) {
        return enumValue.name().charAt(0) + enumValue.name().toLowerCase().replace(Character.toString(enumValue.name().charAt(0)).toLowerCase(), "");
    }
}
