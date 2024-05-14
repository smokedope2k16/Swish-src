package dev.angel.api.util.network;

import dev.angel.api.interfaces.Minecraftable;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;

public class PacketUtil implements Minecraftable {
    public static void send(Packet<?> packet) {
        ClientPlayNetworkHandler connection = mc.getNetworkHandler();
        if (connection != null) {
            connection.sendPacket(packet);
        }
    }

    public static void swing() {
        send(new HandSwingC2SPacket(Hand.MAIN_HAND));
    }


    public static void sneak(boolean sneak) {
        send(new ClientCommandC2SPacket(mc.player, sneak ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
    }
}
