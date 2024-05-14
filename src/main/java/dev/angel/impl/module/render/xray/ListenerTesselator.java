package dev.angel.impl.module.render.xray;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.TesselateBlockEvent;

public class ListenerTesselator extends ModuleListener<XRay, TesselateBlockEvent> {
    public ListenerTesselator(XRay module) {
        super(module, TesselateBlockEvent.class);
    }

    @Override
    public void call(TesselateBlockEvent event) {
        event.setCanceled(true);
    }
}
