package dev.angel.impl.module.render.freelook;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.TickEvent;
import net.minecraft.client.option.Perspective;

public class ListenerTick extends ModuleListener<Freelook, TickEvent> {
    public ListenerTick(Freelook module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.options.getPerspective() != Perspective.THIRD_PERSON_BACK) {
            module.disable();
        }
    }
}
