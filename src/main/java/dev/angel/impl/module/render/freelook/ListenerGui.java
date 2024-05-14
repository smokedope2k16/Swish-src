package dev.angel.impl.module.render.freelook;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.ScreenEvent;

public class ListenerGui extends ModuleListener<Freelook, ScreenEvent> {
    public ListenerGui(Freelook module) {
        super(module, ScreenEvent.class);
    }

    @Override
    public void call(ScreenEvent event) {
        module.disable();
    }
}
