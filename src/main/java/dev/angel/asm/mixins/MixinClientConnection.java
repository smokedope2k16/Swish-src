package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.impl.events.PacketEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientConnection.class)
public class MixinClientConnection implements Minecraftable {

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        try {
            /*
            if (packet instanceof BundleS2CPacket bundle) {
                for (Iterator<Packet<ClientPlayPacketListener>> packetIterator = bundle.getPackets().iterator(); packetIterator.hasNext(); ) {
                    final PacketEvent.Receive<?> packetBundleEvent = new PacketEvent.Receive<>(packetIterator.next());
                    Swish.getEventBus().dispatch(packetBundleEvent);

                    if (packetBundleEvent.isCanceled()) {
                        packetIterator.remove();
                        info.cancel();
                    }
                }
            } else {

             */
            final PacketEvent.Receive<?> packetEvent = new PacketEvent.Receive<>(packet);
            Swish.getEventBus().dispatch(packetEvent, packet.getClass());

            if (packetEvent.isCanceled()) {
                info.cancel();
                //}
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacketHead(Packet<?> packet, CallbackInfo info) {
        PacketEvent.Send<?> packetEvent = new PacketEvent.Send<>(packet);
        Swish.getEventBus().dispatch(packetEvent, packet.getClass());

        if (packetEvent.isCanceled()) {
            info.cancel();
        }
    }
}
