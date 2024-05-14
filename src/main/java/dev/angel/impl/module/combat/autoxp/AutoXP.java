package dev.angel.impl.module.combat.autoxp;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.math.StopWatch;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;

public class AutoXP extends Module {

    protected final NumberValue<Integer> delay = new NumberValue<>(
            new String[]{"ExpDelay", "Delay"},
            "Delay between throwing experience bottles",
            10, 0, 50
    );

    protected final Value<Boolean> swap = new Value<>(
            new String[]{"AutoSwap", "Switch", "Swap"},
            "Automatically switches to exp",
            false
    );

    protected final StopWatch expTimer = new StopWatch();

    public AutoXP() {
        super("AutoXP", new String[]{"AutoXP", "AutoExp", "FastExperience", "AutoMend"}, "Automatically throws exp", Category.COMBAT);
        this.offerValues(delay, swap);
        this.offerListeners(new ListenerTick(this));
    }

    @Override
    public void onEnable() {
        expTimer.reset();
    }
}
