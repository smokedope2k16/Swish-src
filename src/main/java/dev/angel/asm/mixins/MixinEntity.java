package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.asm.ducks.ICamera;
import dev.angel.impl.module.misc.reach.Reach;
import dev.angel.impl.module.render.freelook.Freelook;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity implements ICamera, Minecraftable {
    @Shadow
    protected DataTracker dataTracker;

    @Unique
    private float cameraPitch;
    @Unique
    private float cameraYaw;

    @Inject(method = "getTargetingMargin", at = @At("HEAD"), cancellable = true)
    private void onGetTargetingMargin(CallbackInfoReturnable<Float> info) {
        double hitboxExtend = Swish.getModuleManager().get(Reach.class).getHitboxExtend();
        if (hitboxExtend != 0) {
            info.setReturnValue((float) hitboxExtend);
        }
    }

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    public void changeCameraLookDirection(double xDelta, double yDelta, CallbackInfo ci) {
        if (!Swish.getModuleManager().get(Freelook.class).isEnabled() || mc.options.getPerspective() != Perspective.THIRD_PERSON_BACK) {
            return;
        }

        double pitchDelta = (yDelta * Swish.getModuleManager().get(Freelook.class).getVertical());
        double yawDelta = (xDelta * Swish.getModuleManager().get(Freelook.class).getHorizontal());

        this.cameraPitch = MathHelper.clamp(this.cameraPitch + (float) pitchDelta, -90.0f, 90.0f);
        this.cameraYaw += (float) yawDelta;

        ci.cancel();
    }

    @Override
    @Unique
    public float getCameraPitch() {
        return this.cameraPitch;
    }

    @Override
    @Unique
    public float getCameraYaw() {
        return this.cameraYaw;
    }

    @Override
    @Unique
    public void setRotations(float cameraPitch, float cameraYaw) {
        this.cameraPitch = cameraPitch;
        this.cameraYaw = cameraYaw;
    }
}
