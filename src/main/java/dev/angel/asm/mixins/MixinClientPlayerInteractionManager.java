package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.asm.ducks.IClientPlayerInteractionManager;
import dev.angel.impl.events.AttackEntityEvent;
import dev.angel.impl.events.DamageBlockEvent;
import dev.angel.impl.module.misc.reach.Reach;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager implements IClientPlayerInteractionManager {

    @Override
    @Accessor("blockBreakingCooldown")
    public abstract void setBlockHitDelay(int delay);

    @Shadow
    protected abstract void syncSelectedSlot();

    @Shadow
    public abstract void sendSequencedPacket(ClientWorld world, SequencedPacketCreator packetCreator);

    @Inject(method = "getReachDistance", at = @At("HEAD"), cancellable = true)
    private void onGetReachDistance(CallbackInfoReturnable<Float> info) {
        info.setReturnValue(Swish.getModuleManager().get(Reach.class).getBlockDistance());
    }

    @Inject(method = "hasExtendedReach", at = @At("HEAD"), cancellable = true)
    private void onHasExtendedReach(CallbackInfoReturnable<Boolean> info) {
        if (Swish.getModuleManager().get(Reach.class).isEnabled()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        AttackEntityEvent event = new AttackEntityEvent();
        Swish.getEventBus().dispatch(event);
    }

    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    private void onAttackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> info) {
        DamageBlockEvent event = new DamageBlockEvent(pos, direction);
        Swish.getEventBus().dispatch(event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Override
    public void syncItem() {
        syncSelectedSlot();
    }

    @Override
    public void sendPacketWithSequence(ClientWorld world, SequencedPacketCreator packetCreator) {
        sendSequencedPacket(world, packetCreator);
    }
}
