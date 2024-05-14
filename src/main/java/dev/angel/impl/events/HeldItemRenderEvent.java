package dev.angel.impl.events;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;

public class HeldItemRenderEvent {
    private final Hand hand;
    private final MatrixStack matrix;

    public HeldItemRenderEvent(Hand hand, MatrixStack matrices) {
        this.hand = hand;
        this.matrix = matrices;
    }

    public Hand getHand() {
        return hand;
    }

    public MatrixStack getMatrix() {
        return matrix;
    }
}
