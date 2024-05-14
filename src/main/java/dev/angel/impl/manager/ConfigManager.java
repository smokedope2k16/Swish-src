package dev.angel.impl.manager;

import com.google.gson.*;
import dev.angel.Swish;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.module.Module;
import dev.angel.api.util.logging.Logger;
import dev.angel.api.value.BindValue;
import dev.angel.api.value.ColorValue;
import dev.angel.api.value.Value;
import dev.angel.api.value.util.EnumHelper;
import dev.angel.api.value.util.KeyBind;
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

//TODO: make it so u can make separate configs
@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
public class ConfigManager implements Minecraftable {
    public ArrayList<Module> modules = new ArrayList<>();
    public static String PATH = Swish.DIRECTORY.getAbsolutePath();
    public static String MODULES = PATH + File.separator + "modules";

    public void loadConfig() {
        for (Module module : modules) {
            try {
                loadProperties(module);
                //Managers.MODULE.get(DisplayTweaks.class).loadFromConfig(module);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadPrefix();
    }

    public void saveConfig() {
        File path = new File(MODULES + File.separator);
        if (!path.exists()) {
            Logger.getLogger().log(Level.INFO, String.format("%s modules directory.", path.mkdir() ? "Created" : "Failed to create"));
            return;
        }

        for (Module module : modules) {
            try {
                saveValues(module);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        savePrefix();
    }

    public void saveValues(Module module) throws IOException {
        JsonObject ignored = new JsonObject(); //this is actually needed
        File directory = new File(MODULES + getDirectory(module));
        if (!directory.exists()) {
            Logger.getLogger().log(Level.INFO, String.format("%s categories directory.", directory.mkdir() ? "Created" : "Failed to create"));
            return;
        }
        String moduleName = MODULES + getDirectory(module) + module.getLabel() + ".json";
        Path outputFile = Paths.get(moduleName);
        if (!Files.exists(outputFile)) {
            Files.createFile(outputFile);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(writeValues(module));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)));
        writer.write(json);
        writer.close();
    }

    public static void setValueFromJson(Value value, JsonElement element) {
        switch (value.getType()) {
            case "Boolean" -> value.setValue(element.getAsBoolean());
            case "Double" -> value.setValue(element.getAsDouble());
            case "Float" -> value.setValue(element.getAsFloat());
            case "Integer" -> value.setValue(element.getAsInt());
            case "String" -> {
                String str = element.getAsString();
                value.setValue(str.replace("_", " "));
            }
            case "Bind" -> {
                String bindValue = element.getAsString();
                int bind = KeyBind.getKeyIndex(bindValue.replace("_", " "));
                value.setValue(new KeyBind(bind));
            }
            case "Enum" -> value.setValue(EnumHelper.fromString((Enum<?>) value.getValue(), element.getAsString()));
            case "Color" -> {
                Color color = new Color(element.getAsInt(), true);
                value.setValue(color);
            }
      /*      case "ItemList" -> {
                final ItemList itemList = (ItemList) value;
                final ArrayList<Item> itemArray = new ArrayList<>();
                element.getAsJsonArray().forEach(elem -> {
                    final Item item = Item.getByNameOrId(elem.getAsString().replace("minecraft:", ""));
                    if (item == null) return;
                    itemArray.add(item);
                });
                itemList.setValue(itemArray);
            }
            case "BlockList" -> {
                final BlockList blockList = (BlockList) value;
                final ArrayList<Block> blocks = new ArrayList<>();
                element.getAsJsonArray().forEach(elem -> {
                    Block block = Block.getBlockFromName(elem.getAsString().replace("minecraft:", ""));
                    if (block == null) return;
                    blocks.add(block);
                });
                blockList.setValue(blocks); */
        }
    }

    public void init() {
        modules.addAll(Swish.getModuleManager().getModules());
        loadConfig();
        Logger.getLogger().log(Level.INFO, "Config loaded.");
    }

    private void loadProperties(Module module) throws IOException {
        String moduleName = MODULES + getDirectory(module) + module.getLabel() + ".json";
        Path modulePath = Paths.get(moduleName);
        if (!Files.exists(modulePath)) {
            return;
        }
        loadPath(modulePath, module);
    }

    private void loadPath(Path path, Module module) throws IOException {
        InputStream stream = Files.newInputStream(path);
        try {
            loadFile(new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject(), module);
        } catch (IllegalStateException e) {
            Logger.getLogger().log(Level.ERROR, "Bad Config File for: " + module.getLabel());
            loadFile(new JsonObject(), module);
        }
        stream.close();
    }

    private static void loadFile(JsonObject input, Module module) {
        for (Map.Entry<String, JsonElement> entry : input.entrySet()) {
            String colorValue = entry.getKey();
            JsonElement element = entry.getValue();

            if (entry.getKey().equalsIgnoreCase("Enabled")) {
                if (element.getAsBoolean()) {
                    module.setEnabled(true);
                }
            }

            for (Value value : module.getValues()) {
                if (colorValue.equals(value.getLabel())) {
                    try {
                        setValueFromJson(value, element);
                    } catch (Exception e) {
                        Logger.getLogger().log(Level.INFO, "Error loading config, module: " + module.getLabel() + ", value: " + value.getLabel());
                    }
                }

                if (colorValue.equals("Global " + value.getLabel())) {
                    ColorValue colorProperty = (ColorValue) value;
                    colorProperty.setGlobal(element.getAsBoolean());
                }
            }
        }
    }

    @SuppressWarnings("PatternVariableCanBeUsed")
    public JsonObject writeValues(Module module) {
        JsonObject object = new JsonObject();
        JsonParser jp = new JsonParser();

        object.add("Enabled", jp.parse(module.isEnabled() ? "true" : "false"));

        for (Value value : module.getValues()) {

           /* if (value instanceof ItemList) {
                JsonArray jsonArray = new JsonArray();
                ItemList itemList = (ItemList) value;
                ArrayList<Item> list = (ArrayList<Item>) itemList.getValue();
                list.forEach(item -> {
                    final ResourceLocation resourceLocation = Item.REGISTRY.getNameForObject(item);
                    if (resourceLocation != null) {
                        jsonArray.add(resourceLocation.toString());
                    }
                });
                object.add(itemList.getLabel(), jsonArray);
                continue;
            }

            if (value instanceof BlockList) {
                JsonArray jsonArray = new JsonArray();
                BlockList blockList = (BlockList) value;
                ArrayList<Block> list = (ArrayList<Block>) blockList.getValue();
                list.forEach(block -> jsonArray.add(Block.REGISTRY.getNameForObject(block).toString()));
                object.add(blockList.getLabel(), jsonArray);
                continue;
            } */

            if (value instanceof BindValue) {
                BindValue bindValue = (BindValue) value;
                String name = bindValue.getValue().getName();
                object.add(value.getLabel(), jp.parse(name.replace(" ", "_")));
                continue;
            }

            if (value instanceof ColorValue) {
                //Lithium.LOGGER.info("writing color config for value [" + value.getLabel() + "] of module: [" + module.getLabel() + "]");
                ColorValue colorValue = (ColorValue) value;
                object.add(value.getLabel(), jp.parse(colorValue.getValue().getRGB() + ""));
                object.add("Global " + value.getLabel(), jp.parse(colorValue.isGlobal() + ""));
                continue;
            }

            if (value.isString()) {
                String str = (String) value.getValue();
                value.setValue(str.replace(" ", "_"));
            }

            try {
                object.add(value.getLabel(), jp.parse(value.getValue().toString()));
            } catch (Exception e) {
                Logger.getLogger().log(Level.ERROR, "Error handling config");
            }

        }
        return object;
    }

    public String getDirectory(Module module) {
        String directory = "";
        if (module != null) {
            directory = directory + "/" + module.getCategory().getLabel() + "/";
        }
        return directory;
    }

    public void savePrefix() {
        try {
            File file = new File(PATH, "prefix.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(Swish.getCommandManager().getPrefix());
            out.write("\r\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPrefix() {
        try {
            File file = new File(PATH, "prefix.txt");
            if (!file.exists()) {
                savePrefix();
            }
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                Swish.getCommandManager().setPrefix(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            savePrefix();
        }
    }
}
