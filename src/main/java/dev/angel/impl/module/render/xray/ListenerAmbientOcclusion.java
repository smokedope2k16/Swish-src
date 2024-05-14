package dev.angel.impl.module.render.xray;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.AmbientOcclusionEvent;

public class ListenerAmbientOcclusion extends ModuleListener<XRay, AmbientOcclusionEvent> {
    public ListenerAmbientOcclusion(XRay module) {
        super(module, AmbientOcclusionEvent.class);
    }

    @Override
    public void call(AmbientOcclusionEvent event) {
        event.setLight(1.0F);
    }
}
