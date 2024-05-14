package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.asm.ducks.ICamera;
import dev.angel.impl.module.render.freelook.Freelook;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class MixinCamera implements Minecraftable {
    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = 0, shift = At.Shift.AFTER))
    public void lockRotation(BlockView focusedBlock, Entity cameraEntity, boolean isThirdPerson, boolean isFrontFacing, float f, CallbackInfo ci) {
        final Freelook FREELOOK = Swish.getModuleManager().get(Freelook.class);
        if (FREELOOK != null && FREELOOK.isEnabled() && cameraEntity instanceof ClientPlayerEntity) {
            ICamera cam = (ICamera) cameraEntity;
            this.setRotation(cam.getCameraYaw(), cam.getCameraPitch());
        }
    }
}