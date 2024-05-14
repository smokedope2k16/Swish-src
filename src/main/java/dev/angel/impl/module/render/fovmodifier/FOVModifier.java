package dev.angel.impl.module.render.fovmodifier;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.NumberValue;

import java.util.Scanner;

public class FOVModifier extends Module {

    private final NumberValue<Integer> fov = new NumberValue<>(
            new String[]{"Fov", "fv"},
            "Overrides minecraft's fov",
            100, 0, 180
    );

    private final NumberValue<Float> sprinting = new NumberValue<>(
            new String[]{"Sprinting", "Sprint"},
            "Dynamic fov modifier for sprinting",
            1.0f, 0.0f, 2.5f, 0.01f
    );

    private final NumberValue<Float> swiftness = new NumberValue<>(
            new String[]{"Swiftness", "Speed"},
            "Dynamic fov modifier for swiftness",
            1.0f, 0.0f, 2.5f, 0.01f
    );

    private final NumberValue<Float> slowness = new NumberValue<>(
            new String[]{"Slowness", "slow"},
            "Dynamic fov modifier for slowness",
            1.0f, 0.0f, 2.5f, 0.01f
    );

    private final NumberValue<Float> aim = new NumberValue<>(
            new String[]{"Aiming", "aim", "bow"},
            "Dynamic fov modifier for aiming",
            1.0f, 0.0f, 2.5f, 0.01f
    );

    private final NumberValue<Float> spyglass = new NumberValue<>(
            new String[]{"Spyglass", "spy"},
            "Dynamic fov modifier for spyglass",
            0.1f, 0.0f, 2.5f, 0.01f
    );

    private final NumberValue<Float> flying = new NumberValue<>(
            new String[]{"Fly", "Flight"},
            "Dynamic fov modifier for flying",
            1.0f, 0.0f, 2.5f, 0.01f
    );

    public FOVModifier() {
        super("FOVModifier", new String[]{"FOVModifier", "FovMod", "BetterFov"}, "Modifies dynamic fov", Category.RENDER);
        this.offerValues(fov, sprinting, swiftness, slowness, aim, spyglass, flying);
    }

    public boolean isCustomFov() {
        return isEnabled() && fov.getValue() != 0;
    }

    public double getFov() {
        return fov.getValue();
    }

    public float getSprinting() {
        return sprinting.getValue();
    }

    public float getSwiftness() {
        return swiftness.getValue();
    }

    public float getSlowness() {
        return slowness.getValue();
    }

    public float getAiming() {
        return aim.getValue();
    }

    public float getSpy() {
        return spyglass.getValue();
    }

    public float getFlying() {
        return flying.getValue();
    }
}
