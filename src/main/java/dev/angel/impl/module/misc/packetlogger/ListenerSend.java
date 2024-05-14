package dev.angel.impl.module.misc.packetlogger;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.logging.Logger;
import dev.angel.impl.events.PacketEvent;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class ListenerSend extends ModuleListener<PacketLogger, PacketEvent.Send<?>> {
    public ListenerSend(PacketLogger module) {
        super(module, PacketEvent.Send.class, Integer.MIN_VALUE);
    }

    @Override
    public void call(PacketEvent.Send<?> event) {
        Packet<?> packet = event.getPacket();

        if (module.filter.getValue() && packet instanceof PlayerMoveC2SPacket) {
            return;
        }

        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket && module.blockpacket.getValue()) {
            PlayerInteractBlockC2SPacket playerInteractBlockC2SPacket = (PlayerInteractBlockC2SPacket) event.getPacket();
            Logger.getLogger().log("hit: " + playerInteractBlockC2SPacket.getBlockHitResult().toString() + "\n sequence" + playerInteractBlockC2SPacket.getSequence() + "\n " + playerInteractBlockC2SPacket.getBlockHitResult().getBlockPos());
        }

        String name = packet.getClass().getName().replace("net.minecraft.network.packet.c2s.play.", ""); //CHINA
        Logger.getLogger().log("<PacketLogger.Send> " + name.replace("$", "."), false);
    }
}
