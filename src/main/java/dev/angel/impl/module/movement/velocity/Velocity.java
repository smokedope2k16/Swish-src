package dev.angel.impl.module.movement.velocity;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.NumberValue;

public class Velocity extends Module {

    protected final NumberValue<Integer> chance = new NumberValue<>(
            new String[]{"Chance", "%"},
            "What percent chance we have of actually modifying the velocity",
            80, 0, 100
    );

    protected final NumberValue<Integer> horizontalVelocity = new NumberValue<>(
            new String[]{"HorizontalVelocity", "Horizontal", "hor"},
            "How much horizontal velocity we want to take",
            100, -100, 100
    );

    protected final NumberValue<Integer> verticalVelocity = new NumberValue<>(
            new String[]{"VerticalVelocity", "Vertical", "ver"},
            "How much vertical velocity we want to take",
            100, -100, 100
    );

    public Velocity() {
        super("Velocity", new String[]{"Velocity", "Velo", "AntiKb"}, "Modifies minecraft player knockback", Category.MOVEMENT);
        this.offerValues(chance, horizontalVelocity, verticalVelocity);
        this.offerListeners(new ListenerVelocity(this), new ListenerExplode(this));
    }

    protected boolean notFull() {
        return verticalVelocity.getValue() != 0 || horizontalVelocity.getValue() != 0;
    }

    public void setVelocity(int hor, int ver) {
        verticalVelocity.setValue(ver);
        horizontalVelocity.setValue(hor);
    }
}
