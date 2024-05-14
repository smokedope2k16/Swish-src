package dev.angel.impl.module.misc.timer;

import dev.angel.Swish;
import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.TickEvent;

public class TickListener extends ModuleListener<Timer, TickEvent> {
    public TickListener(Timer module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        Swish.getTimerManager().set(module.amount.getValue());
    }
}
