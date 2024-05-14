package dev.angel.impl.module.other.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.angel.Swish;
import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.api.util.math.MathUtil;
import dev.angel.api.util.math.StopWatch;
import dev.angel.api.util.text.TextColor;
import dev.angel.api.util.network.LatencyUtil;
import dev.angel.api.value.ColorValue;
import dev.angel.api.value.Value;
import dev.angel.impl.module.other.colours.Colours;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

@SuppressWarnings("ConstantConditions")
public class HUD extends Module {

    protected final Value<Boolean> watermark = new Value<>(
            new String[]{"Watermark", "Logo"},
            "Displays a watermark at the top left of your screen",
            true
    );

    protected final Value<Boolean> offset = new Value<>(
            new String[]{"WatermarkOffset", "offset"},
            "Offsets the watermark to be a bit lower",
            false
    );

    protected final Value<Boolean> welcomer = new Value<>(
            new String[]{"Welcomer", "welcome", "greeter"},
            "Displays a welcome message at the top middle of your screen",
            true
    );

    protected final Value<Boolean> potions = new Value<>(
            new String[]{"Potions", "potion", "pots", "pot"},
            "Displays your current potion effects in the bottom right of your screen",
            true
    );

    protected final Value<Boolean> frames = new Value<>(
            new String[]{"Frames", "Fps"},
            "Displays your fps in the bottom right of your screen",
            true
    );

    protected final Value<Boolean> ping = new Value<>(
            new String[]{"Ping", "pingerino", "Pengo", "Peng", "pin", "png"},
            "Displays your ping in the bottom right of your screen",
            true
    );

    protected final Value<Boolean> armor = new Value<>(
            new String[]{"Armor", "ArmorHud", "RenderArmor"},
            "Displays your armor above hunger bar in your hotbar",
            true
    );

    protected final Value<Boolean> totem = new Value<>(
            new String[]{"Totem", "TotemCount", "Totems"},
            "Displays how many totems you have to the left of your hunger bar",
            true
    );

    protected final Value<Boolean> coords = new Value<>(
            new String[]{"Coordinates", "coords", "coord"},
            "Displays your coordinates and facing in the bottom left of your screen",
            true
    );

    protected final Value<Boolean> kmh = new Value<>(
            new String[]{"KMH", "Speed", "Sped"},
            "Displays how fast you're going",
            true
    );

    protected final Value<Boolean> tps = new Value<>(
            new String[]{"Tps", "TicksPerSecond"},
            "Displays the servers ticks per second",
            true
    );

    protected final Value<Boolean> rotations = new Value<>(
            new String[]{"Rotations", "yaw", "pitch", "rots"},
            "Displays your yaw & pitch above the coordinates hud",
            true
    );

    protected final Value<Boolean> lagNotify = new Value<>(
            new String[]{"LagNotify", "NoResponding", "impcat"},
            "Notifies when server is lagging",
            true
    );


    protected final Value<Boolean> capes = new Value<>(
            new String[]{"Capes", "Cape"},
            "Renders capes on client users",
            true
    );

    protected final Value<Boolean> toggleMessages = new Value<>(
            new String[]{"ToggleMessages", "togglemsg", "msg"},
            "Sends messages when you toggle modules in chat",
            true
    );

    protected final Value<Boolean> hotbarKeys = new Value<>(
            new String[]{"HotbarKeys", "hotbarkey", "keys"},
            "Shows your keybinds in your hotbar",
            false
    );

    protected final ColorValue hotbarColor = new ColorValue(
            new String[]{"HotbarColor", "keyColor", "bindColor", "hotbarkeyscolor"},
            new Color(-1),
            false
    );

    private final ItemStack TOTEM_STACK = new ItemStack(Items.TOTEM_OF_UNDYING);
    protected final StopWatch timer = new StopWatch();

    public HUD() {
        super("HUD", new String[]{"HUD", "HudElements"}, "Draws hud elements on your screen", Category.OTHER);
        this.offerListeners(new ListenerRender(this), new ListenerPacket(this));
        this.offerValues(watermark, offset, welcomer, potions, frames, ping, kmh, tps, armor, totem,
                coords, rotations, lagNotify, capes, toggleMessages, hotbarKeys, hotbarColor);
    }

    protected void onRender(DrawContext context) {
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();
        int color = Colours.get().color();

        if (watermark.getValue()) {
            context.drawTextWithShadow(mc.textRenderer, Swish.NAME + " " + Swish.VERSION, 2, offset.getValue() ? 12 : 2, Colours.get().color());
        }

        if (welcomer.getValue()) {
            final String welcome = String.format("Hello %s :^)", mc.player.getName().getString());

            context.drawTextWithShadow(mc.textRenderer, welcome, (int) ((width / 2F) - (mc.textRenderer.getWidth(welcome) / 2F) + 2), 2, color);
        }

        if (lagNotify.getValue() && timer.passed(1500) && !mc.isInSingleplayer()) {
            final String lagString = "Server hasn't responded in " + String.format("%.2f", (timer.getTime() / 1000f)) + "s";
            context.drawTextWithShadow(mc.textRenderer, lagString, (int) ((width / 2F) - (mc.textRenderer.getWidth(lagString) / 2F) + 2), welcomer.getValue() ? 12 : 2, color);
        }

        boolean chatOpened = mc.currentScreen instanceof ChatScreen; //mc.inGameHud.getChatHud().isChatFocused();
        int offset = (chatOpened ? 24 : 10);
        int y = 10;

        if (potions.getValue()) {
            for (StatusEffectInstance effect : mc.player.getStatusEffects()) {
                int amplifier = effect.getAmplifier();
                String potionString = effect.getEffectType().getName().getString()
                        + (amplifier > 0 ? (" " + (amplifier + 1) + "") : "")
                        + ": "
                        + TextColor.GRAY
                        + getPotionDurationString(effect);
                int potionColor = effect.getEffectType().getColor();
                context.drawTextWithShadow(mc.textRenderer, potionString, width - mc.textRenderer.getWidth(potionString) - 2, height - offset, potionColor);
                offset += y;
            }
        }


        if (kmh.getValue()) {
            final String kmhStr = "Speed: " + TextColor.WHITE + String.format("%.2f", Swish.getSpeedManager().getSpeed()) + "km/h";
            context.drawTextWithShadow(mc.textRenderer, kmhStr, width - mc.textRenderer.getWidth(kmhStr) - 2, height - offset, color);
            offset += y;
        }

        if (ping.getValue() && !mc.isInSingleplayer()) {
            final String pingStr = "Ping: " + TextColor.WHITE + LatencyUtil.getPing() + "ms";
            context.drawTextWithShadow(mc.textRenderer, pingStr, width - mc.textRenderer.getWidth(pingStr) - 2, height - offset, color);
            offset += y;
        }

        if (tps.getValue()) {
            final String tpsStr = "TPS: " + TextColor.WHITE + String.format("%.2f", Swish.getTpsManager().getTps());
            context.drawTextWithShadow(mc.textRenderer, tpsStr, width - mc.textRenderer.getWidth(tpsStr) -2, height - offset, color);
            offset += y;
        }

        if (frames.getValue()) {
            final String fpsStr = "FPS: " + TextColor.WHITE + mc.getCurrentFps();
            context.drawTextWithShadow(mc.textRenderer, fpsStr, width - mc.textRenderer.getWidth(fpsStr) - 2, height - offset, color);
        }

        if (rotations.getValue()) {
            context.drawTextWithShadow(mc.textRenderer, "Pitch: "
                            + TextColor.WHITE
                            + String.format("%.2f",
                            MathHelper.wrapDegrees(mc.player.getPitch()))
                            + TextColor.RESET
                            + " Yaw: " + TextColor.WHITE
                            + String.format("%.2f", MathHelper.wrapDegrees(mc.player.getYaw())),
                    2, height - (coords.getValue() ? (chatOpened ? 34 : 20) : (chatOpened ? 24 : 10)), color);
        }

        if (coords.getValue()) {
            String directionString = getDirectionForDisplay();
            String coordsString = "XYZ: "
                    + TextColor.WHITE
                    + getRoundedDouble(mc.player.getX())
                    + TextColor.GRAY
                    + ", "
                    + TextColor.WHITE
                    + getRoundedDouble(mc.player.getY())
                    + TextColor.GRAY
                    + ", "
                    + TextColor.WHITE
                    + getRoundedDouble(mc.player.getZ());
            if (!mc.world.getRegistryKey().getValue().getPath().equals("the_nether")) {
                coordsString += TextColor.GRAY
                        + " (" + TextColor.WHITE
                        + getRoundedDouble(getDimensionCoord(mc.player.getX()))
                        + TextColor.GRAY
                        + ", " + TextColor.WHITE
                        + getRoundedDouble(getDimensionCoord(mc.player.getZ()))
                        + TextColor.GRAY
                        + ")";
            }

            context.drawTextWithShadow(mc.textRenderer, coordsString + directionString, 2, height - (chatOpened ? 24 : 10), color);
        }

        if (!mc.player.isSpectator()) {
            if (armor.getValue()) {
                drawArmor(context);
            }

            if (totem.getValue()) {
                drawTotem(context);
            }

            if (hotbarKeys.getValue()) {
                drawHotbar(context);
            }
        }
    }

    private String getPotionDurationString(StatusEffectInstance effect) {
        if (effect.isInfinite()) {
            return "**:**";
        } else {
            int i = MathHelper.floor((float) effect.getDuration());
            return ticksToElapsedTime(i);
        }
    }

    private String ticksToElapsedTime(int ticks) {
        int i = ticks / 20;
        int j = i / 60;
        i = i % 60;
        return i < 10 ? j + ":0" + i : j + ":" + i;
    }

    public void drawTotem(DrawContext context) {
        int totems = InventoryUtil.count(Items.TOTEM_OF_UNDYING);
        if (totems > 0) {
            int width = context.getScaledWindowWidth();
            int height = context.getScaledWindowHeight();
            int i = width / 2;
            int y = height - getArmorY();
            context.drawItem(TOTEM_STACK, i - 6, y - 2);
            context.getMatrices().push();
            context.getMatrices().translate(0.0f, 0.0f, 200.0f);
            context.drawTextWithShadow(mc.textRenderer, totems + "", i + 19 - 2 - mc.textRenderer.getWidth(totems + "") - 7, y + 9 - 2, Colours.get().color());
            context.getMatrices().pop();
        }
    }

    private void drawArmor(DrawContext context) {
        final MatrixStack matrixStack = context.getMatrices();
        int width = context.getScaledWindowWidth() >> 1;
        int height = context.getScaledWindowHeight();
        int i1;
        int i2 = 15;
        int i3 = i1 = 3;
        while (i3 >= 0) {
            ItemStack stack = mc.player.getInventory().armor.get(i1);
            if (!(stack.getItem() instanceof AirBlockItem)) {
                int y = height - getArmorY();
                int x = width + i2;
                int color = stack.getItem().getItemBarColor(stack);

                if (armor.getValue()) {
                    context.drawItem(stack, x, y);
                    RenderSystem.disableDepthTest();
                    String count = (stack.getCount() > 1) ? (stack.getCount() + "") : "";
                    context.drawTextWithShadow(mc.textRenderer, count, x + 19 - 2 - mc.textRenderer.getWidth(count), y + 9, 0xFFFFFFFF);
                }

                i2 += 18;

                if (stack.getItem().isDamageable()) {
                    final int dmg = (int) ItemUtil.getDamageInPercent(stack);
                    matrixStack.push();
                    matrixStack.scale(0.625F, 0.625F, 0.625F);
                    RenderSystem.disableDepthTest();
                    context.drawTextWithShadow(mc.textRenderer, dmg + "%", (int) ((x + getFixedArmorOffset(dmg)) * 1.6F), (int) (y * 1.6F + (armor.getValue() ? -8 : 16)), color);
                    RenderSystem.enableDepthTest();
                    matrixStack.scale(1.0f, 1.0f, 1.0f);
                    matrixStack.pop();
                }
            }
            i3 = --i1;
        }
    }

    private void drawHotbar(DrawContext context) {
        if (hotbarKeys.getValue() && !mc.player.isSpectator()) {
            int x = context.getScaledWindowWidth() / 2 - 87;
            int y = context.getScaledWindowHeight() - 18;
            int length = mc.options.hotbarKeys.length;
            for (int i = 0; i < length; i++) {
                context.drawTextWithShadow(mc.textRenderer, mc.options.hotbarKeys[i].getBoundKeyLocalizedText().getString(), x + i * 20, y, hotbarColor.getColor().getRGB());
            }
        }
    }

    public static int getDirection4D() {
        return MathHelper.floor((mc.player.getYaw() * 4.0F / 360.0F) + 0.5D) & 3;
    }

    private String getDirectionForDisplay() {
        return switch (getDirection4D()) {
            case 0 -> TextColor.GRAY + " [" + TextColor.WHITE + "+Z" + TextColor.GRAY + "]";
            case 1 -> TextColor.GRAY + " [" + TextColor.WHITE + "-X" + TextColor.GRAY + "]";
            case 2 -> TextColor.GRAY + " [" + TextColor.WHITE + "-Z" + TextColor.GRAY + "]";
            case 3 -> TextColor.GRAY + " [" + TextColor.WHITE + "+X" + TextColor.GRAY + "]";
            default -> throw new IllegalStateException("Unexpected value: " + getDirection4D());
        };
    }

    private double getDimensionCoord(double coord) {
        if (mc.world.getRegistryKey().getValue().getPath().equals("the_nether")) {
            return coord * 8;
        } else if (!mc.world.getRegistryKey().getValue().getPath().equals("the_nether")) {
            return coord / 8;
        }

        return coord;
    }

    private String getRoundedDouble(double pos) {
        return String.format("%.2f", pos);
    }

    private int getFixedArmorOffset(int percent) {
        if (percent == 100) {
            return 1;
        } else if (percent < 10) {
            return 5;
        } else {
            return 3;
        }
    }

    private int getArmorY() {
        int y;
        if (mc.player.isSubmergedInWater() && mc.player.getAir() > 0 && !mc.player.isCreative()) {
            y = 65;
        } else if (mc.player.isCreative()) {
            y = mc.player.isRiding() ? 45 : 38;
        } else {
            y = 55;
        }
        return y;
    }

    public boolean cape() {
        return capes.getValue();
    }

}
