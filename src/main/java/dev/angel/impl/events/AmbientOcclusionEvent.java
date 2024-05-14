package dev.angel.impl.events;

import net.minecraft.block.BlockState;

public class AmbientOcclusionEvent {
    private final BlockState state;
    private float light;

    public AmbientOcclusionEvent(BlockState state, float light) {
        this.state = state;
        this.light = light;
    }

    public BlockState getState() {
        return state;
    }

    public float getLight() {
        return light;
    }

    public void setLight(float light) {
        this.light = light;
    }
}
