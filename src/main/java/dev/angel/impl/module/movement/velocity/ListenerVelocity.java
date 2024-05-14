package dev.angel.impl.module.movement.velocity;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.asm.mixins.IEntityVelocityUpdateS2CPacket;
import dev.angel.impl.events.PacketEvent;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

import java.util.Random;

public class ListenerVelocity extends ModuleListener<Velocity, PacketEvent.Receive<EntityVelocityUpdateS2CPacket>> {
    public ListenerVelocity(Velocity module) {
        super(module, PacketEvent.Receive.class, EntityVelocityUpdateS2CPacket.class);
    }

    @Override
    public void call(PacketEvent.Receive<EntityVelocityUpdateS2CPacket> event) {
        if (module.chance.getValue() != 100.0) {
            Random random = new Random();
            int chance = random.nextInt(100);
            if (chance >= module.chance.getValue() / 100.0) {
                return;
            }
        }

        final EntityVelocityUpdateS2CPacket packet = event.getPacket();
        if (mc.player != null && event.getPacket().getId() == mc.player.getId() && module.notFull()) {
            ((IEntityVelocityUpdateS2CPacket) packet).setX(packet.getVelocityX() * module.horizontalVelocity.getValue() / 100);
            ((IEntityVelocityUpdateS2CPacket) packet).setY(packet.getVelocityY() * module.verticalVelocity.getValue() / 100);
            ((IEntityVelocityUpdateS2CPacket) packet).setZ(packet.getVelocityZ() * module.horizontalVelocity.getValue() / 100);
        } else {
            event.setCanceled(true);
        }
    }
}
