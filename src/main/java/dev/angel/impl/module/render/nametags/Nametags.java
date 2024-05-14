package dev.angel.impl.module.render.nametags;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.angel.Swish;
import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.api.util.network.LatencyUtil;
import dev.angel.api.util.render.Interpolation;
import dev.angel.api.value.EnumValue;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import dev.angel.impl.events.Render3DEvent;
import dev.angel.impl.module.render.nametags.mode.NametagMode;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class Nametags extends Module {

    public final EnumValue<NametagMode> mode = new EnumValue<>(
            new String[]{"Mode", "method", "modejamin"},
            "Legit: - vanilla looking nametags | Blatant: - blatant looking nametags",
            NametagMode.LEGIT
    );

    public final NumberValue<Float> scale = new NumberValue<>(
            new String[]{"Scale", "scal", "scalin", "scaling", "size", "scallywag", "scalejamin", "scalington"},
            "Scale of blatant tags",
            0.3f, 0.1f, 1.0f, 0.1f
    );

    public final Value<Boolean> health = new Value<>(
            new String[]{"Health", "healthjamin", "healthington"},
            "Shows player health",
            true
    );

    public final Value<Boolean> pops = new Value<>(
            new String[]{"Pops", "totempops", "poppingtons"},
            "Shows amount of times the player has popped",
            true
    );

    public final Value<Boolean> ping = new Value<>(
            new String[]{"Ping", "ms", "pingjamin"},
            "Shows the player's ping",
            true
    );

    public final Value<Boolean> coloredPing = new Value<>(
            new String[]{"ColoredPing", "colorping", "coloredpingjamin"},
            "If the ping should be colored",
            true
    );

    public Nametags() {
        super("Nametags", new String[]{"nametags"}, "Modifies player nametags", Category.RENDER);
        this.offerValues(mode, health, pops, coloredPing, ping);
        this.offerListeners(new ListenerRender(this));
    }

    protected void renderNametagsBlatant(PlayerEntity player, double x, double y, double z, Vec3d mcPlayerInterpolation, Render3DEvent event) {
        double tempY = y + (player.isSneaking() ? 0.5d : 0.7d);
        double xDist = mcPlayerInterpolation.x - x;
        double yDist = mcPlayerInterpolation.y - y;
        double zDist = mcPlayerInterpolation.z - z;
        y = MathHelper.sqrt((float) (xDist * xDist + yDist * yDist + zDist * zDist));

        final String displayTag = EntityUtil.getName(player)
                                  + getPingString(player)
                                  + getHealthString(player)
                                  + getPopString(player);
        final int width = mc.textRenderer.getWidth(displayTag) / 2;
        double s = 0.0018 + (scale.getValue() * y * 0.1f);

        if (y <= 8) {
            s = 0.0245d;
        }

        GL11.glPushMatrix();
        RenderSystem.enablePolygonOffset();
        RenderSystem.polygonOffset(1.0f, -1500000.0f);

        GL11.glTranslatef((float) x, (float) tempY + 1.4f, (float) z);
        GL11.glRotatef((float) -Interpolation.getCameraPos().y, 0.0f, 1.0f, 0.0f);
        float xRot = (mc.options.getPerspective() == Perspective.FIRST_PERSON ? -1.0f : 1.0f);
        GL11.glRotatef((float) Interpolation.getCameraPos().x, xRot, 0.0f, 0.0f);

        GL11.glScaled(-s, -s, s);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.enableBlend();

        /*mc.textRenderer.draw(
                displayTag,
                -width,
                -8,
                -1,
                event.getMatrix()
        );*/

        RenderSystem.disableBlend();
        GL11.glPushMatrix();

        GL11.glPopMatrix();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.disablePolygonOffset();
        RenderSystem.polygonOffset(1.0f, 1500000.0f);
        GL11.glPopMatrix();
    }

    public String getNametagLegit(Entity entity) {
        final String displayName = entity.getDisplayName().getString();

        String nameString =// weird pvp legacy thing
                displayName.replace("[R]", String.format("%s[%sR%s]%s",
                Formatting.GRAY,
                Formatting.RED,
                Formatting.GRAY,
                Formatting.RESET))
                .replace("[B]",String.format("%s[%sB%s]%s",
                        Formatting.GRAY,
                        Formatting.BLUE,
                        Formatting.GRAY,
                        Formatting.RESET));

        return colouredName(entity)
               + getPingString(entity)
               + getHealthString(entity)
               + getPopString(entity);
    }

    private String colouredName(Entity entity) {
        final String name = EntityUtil.getName(entity);
        final boolean isFriend = Swish.getFriendManager().isFriend(name);
        return (isFriend ? Formatting.AQUA : "") + name + Formatting.RESET;
    }

    private String getPingString(Entity player) {
        final int pingInt = LatencyUtil.getPing(player);

        Formatting color = Formatting.WHITE;
        if (coloredPing.getValue()) {
            if (pingInt <= 40.0) {
                color = Formatting.GREEN;
            }
            if (pingInt <= 70.0) {
                color = Formatting.DARK_GREEN;
            }
            if (pingInt <= 99.0) {
                color = Formatting.YELLOW;
            }
            if (pingInt > 99.0) {
                color = Formatting.RED;
            }
        }

        return ping.getValue() ? (
                    " "
                    + color
                    + pingInt
                    + "ms"
                ) : "";
    }

    private String getPopString(Entity player) {
        final String name = EntityUtil.getName(player);
        final Map<String, Integer> registry = Swish.getPopManager().getPopMap();
        final int popsInt = registry.get(name);

        Formatting color = Formatting.DARK_RED;
        if (popsInt == 1) {
            color = Formatting.GREEN;
        }
        if (popsInt == 2) {
            color = Formatting.DARK_GREEN;
        }
        if (popsInt == 3) {
            color = Formatting.YELLOW;
        }
        if (popsInt == 4) {
            color = Formatting.GOLD;
        }
        if (popsInt == 5) {
            color = Formatting.RED;
        }

        return pops.getValue() ? (
                registry.containsKey(name)
                    ? color + " -" + registry.get(name)
                    : ""
                ) : "";
    }

    private String getHealthString(Entity player) {
        final double healthD = EntityUtil.getHealth((PlayerEntity) player);

        Formatting color = Formatting.DARK_RED;
        if (healthD <= 5.0f) {
            color = Formatting.RED;
        } else if (healthD > 5.0f && healthD <= 10.0f) {
            color = Formatting.GOLD;
        } else if (healthD > 10.0f && healthD <= 15.0f) {
            color = Formatting.YELLOW;
        } else if (healthD > 15.0f && healthD <= 20.0f) {
            color = Formatting.DARK_GREEN;
        } else if (healthD > 20.0f) {
            color = Formatting.GREEN;
        }

        return health.getValue() ? (
                    " "
                    + color
                    + (int) healthD
                ) : "";
    }

}
