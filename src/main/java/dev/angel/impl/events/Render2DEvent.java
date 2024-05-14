package dev.angel.impl.events;

import dev.angel.api.event.events.Event;
import net.minecraft.client.gui.DrawContext;

public class Render2DEvent extends Event {
    private final DrawContext context;

    public Render2DEvent(DrawContext context) {
        this.context = context;
    }

    public DrawContext getContext() {
        return context;
    }
}
