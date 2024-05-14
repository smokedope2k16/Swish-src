package dev.angel.api.util.render;

import dev.angel.api.interfaces.Minecraftable;
import net.minecraft.entity.Entity;

public class RenderUtil implements Minecraftable {
    public static Entity getEntity() {
        return mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity();
    }
}
