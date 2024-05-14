package dev.angel.impl.module.movement.holesnap;

import dev.angel.Swish;
import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.TickEvent;

public class TickListener extends ModuleListener<Holesnap, TickEvent> {
    public TickListener(Holesnap module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.player == null) {
            return;
        }
        if (mc.player.isSpectator()) {
            return;
        }
        if (module.timer.getValue()) {
            if (module.boosted >= module.timerLength.getValue()) {
                Swish.getTimerManager().reset();
                return;
            }
            Swish.getTimerManager().set(module.timerAmount.getValue());
            ++module.boosted;
        }
    }
}
