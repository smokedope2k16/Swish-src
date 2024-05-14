package dev.angel.impl.module.movement.step;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.TickEvent;
import dev.angel.impl.module.movement.step.mode.StepMode;

public class ListenerTick extends ModuleListener<Step, TickEvent> {
    public ListenerTick(Step module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.player == null) {
            return;
        }
        if (module.timer.passed(200)) {
            mc.player.setStepHeight(module.height.getValue());

            if (module.mode.getValue() == StepMode.NORMAL) {

            }
        }
        else {
            mc.player.setStepHeight(0.6f);
        }
    }
}
