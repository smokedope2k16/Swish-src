package dev.angel.impl.module.player.fakelag;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.PacketEvent;
import net.minecraft.network.packet.c2s.play.*;

public class ListenerPacket extends ModuleListener<FakeLag, PacketEvent.Send<?>> {
    public ListenerPacket(FakeLag module) {
        super(module, PacketEvent.Send.class);
    }

    @Override
    public void call(PacketEvent.Send<?> event) {
        if (!(event.getPacket() instanceof ChatMessageC2SPacket
                || event.getPacket() instanceof TeleportConfirmC2SPacket
                || event.getPacket() instanceof KeepAliveC2SPacket
                || event.getPacket() instanceof AdvancementTabC2SPacket
                || event.getPacket() instanceof ClientStatusC2SPacket)) {
            if (!module.cache.contains(event.getPacket())) {
                module.cache.add(event.getPacket());
                event.setCanceled(true);
            }
        }
    }
}
