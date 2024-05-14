package dev.angel.impl.events;

import dev.angel.api.event.events.Event;
import net.minecraft.network.packet.Packet;

// please
public class PacketEvent<T extends Packet<?>> extends Event {
    private final T packet;

    private PacketEvent(T packet) {
        this.packet = packet;
    }

    public T getPacket() {
        return packet;
    }

    public static class Send<T extends Packet<?>> extends PacketEvent<T> {
        public Send(T packet) {
            super(packet);
        }
    }

    public static class Receive<T extends Packet<?>> extends PacketEvent<T> {
        public Receive(T packet) {
            super(packet);
        }
    }
}
