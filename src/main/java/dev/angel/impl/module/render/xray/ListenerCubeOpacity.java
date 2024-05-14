package dev.angel.impl.module.render.xray;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.CubeOpacityEvent;

public class ListenerCubeOpacity extends ModuleListener<XRay, CubeOpacityEvent> {
    public ListenerCubeOpacity(XRay module) {
        super(module, CubeOpacityEvent.class);
    }

    @Override
    public void call(CubeOpacityEvent event) {
        event.setCanceled(true);
    }
}
