package dev.angel.impl.module.combat.criticals;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.network.PacketUtil;
import dev.angel.impl.events.AttackEntityEvent;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class ListenerAttackEntity extends ModuleListener<Criticals, AttackEntityEvent> {
    public ListenerAttackEntity(Criticals module) {
        super(module, AttackEntityEvent.class);
    }

    @Override
    public void call(AttackEntityEvent event) {
        if (mc.player != null) {
            PacketUtil.send(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.0625, mc.player.getZ(), false));
            PacketUtil.send(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
        }
    }
}
