package dev.angel.impl.module.misc.antihunger;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.asm.mixins.IPlayerMoveC2SPacket;
import dev.angel.impl.events.PacketEvent;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class ListenerMove extends ModuleListener<AntiHunger, PacketEvent.Send<PlayerMoveC2SPacket>> {
    public ListenerMove(AntiHunger module) {
        super(module, PacketEvent.Send.class, PlayerMoveC2SPacket.class);
    }

    @Override
    public void call(PacketEvent.Send<PlayerMoveC2SPacket> event) {
        if (mc.player != null && mc.interactionManager != null && module.ground.getValue()
                && mc.player.isOnGround() && mc.player.fallDistance <= 0.0 && !mc.interactionManager.isBreakingBlock()) {
            ((IPlayerMoveC2SPacket) event.getPacket()).setOnGround(false);
        }
    }
}
