package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.asm.ducks.ILivingEntity;
import dev.angel.impl.events.DeathEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends MixinEntity implements ILivingEntity, Minecraftable {

    @Shadow
    @Final
    private static TrackedData<Float> HEALTH;

    @Override
    @Accessor("jumpingCooldown")
    public abstract void setLastJumpCooldown(int delay);

    @Inject(method = "onTrackedDataSet", at = @At("RETURN"))
    public void onTrackedDataSet(TrackedData<?> key, CallbackInfo info) {
        if (key.equals(HEALTH) && this.dataTracker.get(HEALTH) <= 0.0 && mc.world != null && mc.world.isClient()) {
            DeathEvent deathEvent = new DeathEvent(LivingEntity.class.cast(this));
            Swish.getEventBus().dispatch(deathEvent);
        }
    }
}
