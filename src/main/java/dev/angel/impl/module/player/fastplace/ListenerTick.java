package dev.angel.impl.module.player.fastplace;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.asm.mixins.IMinecraft;
import dev.angel.impl.events.TickEvent;

public class ListenerTick extends ModuleListener<FastPlace, TickEvent> {
    public ListenerTick(FastPlace module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.options.useKey.isPressed()) {
            if (module.delay.getValue() < ((IMinecraft) mc).getItemUseCooldown()) {
                ((IMinecraft) mc).setItemUseCooldown(module.delay.getValue());
            }
        }
    }
}
