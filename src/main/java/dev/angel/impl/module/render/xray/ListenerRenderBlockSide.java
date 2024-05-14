package dev.angel.impl.module.render.xray;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.RenderBlockSideEvent;

public class ListenerRenderBlockSide extends ModuleListener<XRay, RenderBlockSideEvent> {
    public ListenerRenderBlockSide(XRay module) {
        super(module, RenderBlockSideEvent.class);
    }

    @Override
    public void call(RenderBlockSideEvent event) {
        event.setCanceled(true);
    }
}
