package dev.angel.impl.module.misc.timer;

import dev.angel.Swish;
import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.NumberValue;

public class Timer extends Module {

    protected final NumberValue<Float> amount = new NumberValue<>(
            new String[]{"Amount", "time"},
            "meow",
            1.0f, 0.1f, 50.0f, 0.1f
    );

    public Timer() {
        super("Timer", new String[]{"timer", "time", "zoom"}, "Change the timer whetevr idk", Category.MISC);
        this.offerValues(amount);
        this.offerListeners(new TickListener(this));
    }

    @Override
    public void onDisable() {
        Swish.getTimerManager().reset();
    }

}
