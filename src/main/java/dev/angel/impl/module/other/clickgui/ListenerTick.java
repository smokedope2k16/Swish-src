package dev.angel.impl.module.other.clickgui;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.TickEvent;
import dev.angel.impl.gui.SwishGui;

public class ListenerTick extends ModuleListener<ClickGUI, TickEvent> {
    public ListenerTick(ClickGUI module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (module.SWISH_GUI != null) {
            mc.setScreen(module.SWISH_GUI);
            module.SWISH_GUI = null;
            return;
        }

        if (!(mc.currentScreen instanceof SwishGui)) {
            module.disable();
        }
    }
}
