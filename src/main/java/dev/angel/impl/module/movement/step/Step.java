package dev.angel.impl.module.movement.step;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.math.StopWatch;
import dev.angel.api.value.EnumValue;
import dev.angel.api.value.NumberValue;
import dev.angel.impl.module.movement.step.mode.StepMode;

public class Step extends Module {

    protected final EnumValue<StepMode> mode = new EnumValue<>(
            new String[]{"Mode", "Type", "Method"},
            "Normal - Sends a position packet to the block we are stepping / Vanilla - Sets your position to the block we are stepping.",
            StepMode.VANILLA
    );

    protected final NumberValue<Float> height = new NumberValue<>(
            new String[]{"Height", "h"},
            "How much we want to set our step height to.",
            2.0f, 0.6f, 5.0f, 0.1f
    );

    protected final StopWatch timer = new StopWatch();

    public Step() {
        super("Step", new String[]{"step", "spider"}, "Allows you to step up walls", Category.MOVEMENT);
        this.offerValues(mode, height);
        this.offerListeners(new ListenerTick(this));
    }

    @Override
    public String getInfo() {
        return mode.getFixedValue();
    }

    @Override
    public void onDisable() {
        if (mc.player == null) {
            return;
        }
        mc.player.setStepHeight(0.6f);
    }

    public double[] getOffset(double height) {
        // enchantment tables
        if (height == 0.75) {
            return new double[] {
                    0.42, 0.753, 0.75
            };
        }
        // end portal frames
        else if (height == 0.8125) {
            return new double[] {
                    0.39, 0.7, 0.8125
            };
        }
        // chests
        else if (height == 0.875) {
            return new double[] {
                    0.39, 0.7, 0.875
            };
        }
        else if (height == 1) {
            return new double[] {
                    0.42, 0.753, 1
            };
        }
        else if (height == 1.5) {
            return new double[] {
                    0.42, 0.75, 1.0, 1.16, 1.23, 1.2
            };
        }
        else if (height == 2) {
            return new double[] {
                    0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43
            };
        }
        else if (height == 2.5) {
            return new double[] {
                    0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907
            };
        }
        return null;
    }
}
