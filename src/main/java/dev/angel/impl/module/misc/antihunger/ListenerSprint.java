package dev.angel.impl.module.misc.antihunger;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.PacketEvent;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class ListenerSprint extends ModuleListener<AntiHunger, PacketEvent.Send<ClientCommandC2SPacket>> {
    public ListenerSprint(AntiHunger module) {
        super(module, PacketEvent.Send.class, ClientCommandC2SPacket.class);
    }

    @Override
    public void call(PacketEvent.Send<ClientCommandC2SPacket> event) {
        ClientCommandC2SPacket.Mode packet = event.getPacket().getMode();
        if (packet == ClientCommandC2SPacket.Mode.START_SPRINTING && module.sprint.getValue()) {
            event.setCanceled(true);
        }
    }
}
