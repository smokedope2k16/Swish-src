package dev.angel.impl.module.render.holeesp;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.block.HoleUtil;
import dev.angel.impl.events.TickEvent;

public class ListenerTick extends ModuleListener<HoleESP, TickEvent> {
    public ListenerTick(HoleESP module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        module.service.submit(() -> {
            module.holes = HoleUtil.getHoles(module.range.getValue(), module.doubles.getValue());
            if (module.voids.getValue()) {
                module.voidHoles = module.calcVoid();
            }
        });
    }
}
