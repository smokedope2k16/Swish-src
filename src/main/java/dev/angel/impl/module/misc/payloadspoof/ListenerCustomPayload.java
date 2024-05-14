package dev.angel.impl.module.misc.payloadspoof;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.asm.mixins.ICustomPayloadC2SPacket;
import dev.angel.impl.events.PacketEvent;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

public class ListenerCustomPayload extends ModuleListener<PayloadSpoof, PacketEvent.Send<CustomPayloadC2SPacket>> {
    public ListenerCustomPayload(PayloadSpoof module) {
        super(module, PacketEvent.Send.class, CustomPayloadC2SPacket.class);
    }

    @Override
    public void call(PacketEvent.Send<CustomPayloadC2SPacket> event) {
        ICustomPayloadC2SPacket packet = (ICustomPayloadC2SPacket) event.getPacket();
        Identifier id = packet.getChannel();
        if (id.equals(CustomPayloadC2SPacket.BRAND)) {
            packet.setData(new PacketByteBuf(Unpooled.buffer()).writeString("vanilla"));
        }
    }
}
