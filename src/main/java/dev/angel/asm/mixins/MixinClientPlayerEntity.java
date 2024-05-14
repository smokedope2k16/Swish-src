package dev.angel.asm.mixins;

import com.mojang.authlib.GameProfile;
import dev.angel.Swish;
import dev.angel.api.event.events.Stage;
import dev.angel.impl.events.MotionUpdateEvent;
import dev.angel.impl.events.MoveEvent;
import dev.angel.impl.events.UpdateEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity extends AbstractClientPlayerEntity {

    private MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow
    private void autoJump(float dx, float dz) {}

    private MotionUpdateEvent motionUpdateEvent;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", ordinal = 0), method = "tick()V")
    private void onTick(CallbackInfo ci) {
        UpdateEvent event = new UpdateEvent();
        Swish.getEventBus().dispatch(event);
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void move(MovementType type, Vec3d movement, CallbackInfo info) {
        MoveEvent event = new MoveEvent(type, movement);
        Swish.getEventBus().dispatch(event);
        if (event.isCanceled()) {
            info.cancel();
        } else if (!type.equals(event.getType()) || !movement.equals(event.getVec())) {
            double double_1 = this.getX();
            double double_2 = this.getZ();
            super.move(event.getType(), event.getVec());
            this.autoJump((float) (this.getX() - double_1), (float) (this.getZ() - double_2));
            info.cancel();
        }
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void onSendMovementPacketsHead(CallbackInfo info) {
        motionUpdateEvent = new MotionUpdateEvent(Stage.PRE);
        Swish.getEventBus().dispatch(motionUpdateEvent);
        if (motionUpdateEvent.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V", ordinal = 0), cancellable = true)
    private void onTickHasVehicleBeforeSendPackets(CallbackInfo info) {
        motionUpdateEvent = new MotionUpdateEvent(Stage.PRE);
        Swish.getEventBus().dispatch(motionUpdateEvent);
        if (motionUpdateEvent.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "sendMovementPackets", at = @At("TAIL"))
    private void onSendMovementPacketsTail(CallbackInfo info) {
        motionUpdateEvent = new MotionUpdateEvent(Stage.POST);
        Swish.getEventBus().dispatch(motionUpdateEvent);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void onTickHasVehicleAfterSendPackets(CallbackInfo info) {
        motionUpdateEvent = new MotionUpdateEvent(Stage.POST);
        Swish.getEventBus().dispatch(motionUpdateEvent);
    }
}
