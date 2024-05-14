package dev.angel.impl.module.other.hud;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.PacketEvent;

public class ListenerPacket extends ModuleListener<HUD, PacketEvent.Receive<?>> {
    public ListenerPacket(HUD module) {
    super(module, PacketEvent.Receive.class);
    }

    @Override
    public void call(PacketEvent.Receive<?> event) {
        module.timer.reset();
    }
}
