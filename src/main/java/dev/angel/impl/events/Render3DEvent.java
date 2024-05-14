package dev.angel.impl.events;

import dev.angel.api.event.events.Event;
import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent extends Event {
    private final MatrixStack matrix;
    private final float tickDelta;

    public Render3DEvent(MatrixStack matrix, float tickDelta) {
        this.matrix = matrix;
        this.tickDelta = tickDelta;
    }

    public MatrixStack getMatrix() {
        return matrix;
    }

    public float getTickDelta() {
        return tickDelta;
    }
}
