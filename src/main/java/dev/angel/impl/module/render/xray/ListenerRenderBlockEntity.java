package dev.angel.impl.module.render.xray;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.RenderBlockEntityEvent;

public class ListenerRenderBlockEntity extends ModuleListener<XRay, RenderBlockEntityEvent> {
    public ListenerRenderBlockEntity(XRay module) {
        super(module, RenderBlockEntityEvent.class);
    }

    @Override
    public void call(RenderBlockEntityEvent event) {
        event.setCanceled(true);
    }
}
