package dev.angel.impl.module.other.hud;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.Render2DEvent;

public class ListenerRender extends ModuleListener<HUD, Render2DEvent> {
    public ListenerRender(HUD module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void call(Render2DEvent event) {
        module.onRender(event.getContext());
    }
}
