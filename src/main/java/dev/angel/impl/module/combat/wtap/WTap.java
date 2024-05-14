package dev.angel.impl.module.combat.wtap;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.math.StopWatch;
import dev.angel.api.value.EnumValue;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import dev.angel.impl.module.combat.wtap.mode.WTapMode;

// TODO: normal mode
public class WTap extends Module {

    protected final EnumValue<WTapMode> mode = new EnumValue<>(
            new String[]{"Mode", "Type", "method"},
            "Normal: - Stops sprinting serverside & clientside / Packet: - Stops sprinting serverside",
            WTapMode.PACKET
    );

    protected final NumberValue<Float> distance = new NumberValue<>(
            new String[]{"Distance", "Range", "rang"},
            "How close the player has to be (Requires normal mode)",// XXXDD
            3f, 1f, 6f, 0.1f
    );

    protected final NumberValue<Integer> duration = new NumberValue<>(
            new String[]{"Duration", "for", "Time"},
            "How long to stay sprinting for",
            12, 1, 25
    );

    protected final NumberValue<Integer> delay = new NumberValue<>(
            new String[]{"Wait", "Delay", "Stop"},
            "How long to wait before sprinting again",
            5, 0, 10
    );

    protected final StopWatch timer = new StopWatch();
    protected int stage = 0;

    public WTap() {
        super("WTap", new String[]{"WTap", "Wtapington", "tap"}, "Automatically W Taps", Category.COMBAT);
        this.offerValues(mode, distance, duration, delay);
        this.offerListeners(new ListenerAttackEntity(this), new ListenerTick(this));
    }
}
