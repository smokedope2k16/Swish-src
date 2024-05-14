package dev.angel.impl.module.movement.velocity;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.asm.mixins.IExplosionS2CPacket;
import dev.angel.impl.events.PacketEvent;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

import java.util.Random;

public class ListenerExplode extends ModuleListener<Velocity, PacketEvent.Receive<ExplosionS2CPacket>> {
    public ListenerExplode(Velocity module) {
        super(module, PacketEvent.Receive.class, ExplosionS2CPacket.class);
    }

    @Override
    public void call(PacketEvent.Receive<ExplosionS2CPacket> event) {
        if (module.chance.getValue() != 100.0) {
            Random random = new Random();
            int chance = random.nextInt(100);
            if (chance <= module.chance.getValue() / 100.0) {
                return;
            }
        }

        final ExplosionS2CPacket packet = event.getPacket();
        if (mc.player != null && module.notFull()) {
            ((IExplosionS2CPacket) packet).setX((int) (packet.getPlayerVelocityX() * module.horizontalVelocity.getValue() / 100));
            ((IExplosionS2CPacket) packet).setY((int) (packet.getPlayerVelocityY() * module.verticalVelocity.getValue() / 100));
            ((IExplosionS2CPacket) packet).setZ((int) (packet.getPlayerVelocityZ() * module.horizontalVelocity.getValue() / 100));
        } else {
            event.setCanceled(true);
        }
    }
}
