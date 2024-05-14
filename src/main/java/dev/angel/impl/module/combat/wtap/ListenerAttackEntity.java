package dev.angel.impl.module.combat.wtap;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.network.PacketUtil;
import dev.angel.impl.events.AttackEntityEvent;
import dev.angel.impl.module.combat.wtap.mode.WTapMode;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class ListenerAttackEntity extends ModuleListener<WTap, AttackEntityEvent> {
    public ListenerAttackEntity(WTap module) {
        super(module, AttackEntityEvent.class);
    }

    @Override
    public void call(AttackEntityEvent event) {
        sendPacket();
        if (module.timer.passed(module.delay.getValue() * 10)) {
            module.stage = 1;
            module.timer.reset();
        }
    }

    private void sendPacket() {
        if (mc.player != null && mc.player.isSprinting() && module.mode.getValue() == WTapMode.PACKET) {
            PacketUtil.send(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
            PacketUtil.send(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
    }
}
