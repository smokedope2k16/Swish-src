package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.util.entity.CapeUtil;
import dev.angel.impl.module.render.fovmodifier.FOVModifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayerEntity implements Minecraftable {

    @Shadow
    protected abstract PlayerListEntry getPlayerListEntry();

    @Inject(method = "getCapeTexture", at = @At("HEAD"), cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<Identifier> callbackInfoReturnable) {
        PlayerListEntry playerInfo = getPlayerListEntry();
        if (playerInfo != null && CapeUtil.isCapesEnabled()) {
            Identifier location = CapeUtil.getCape(playerInfo.getProfile().getId());
            if (location != null) {
                callbackInfoReturnable.setReturnValue(location);
            }
        }
    }

    @Inject(method = "getFovMultiplier", at = @At("HEAD"), cancellable = true)
    public void getFovMultiplierHook(CallbackInfoReturnable<Float> info) {
        float f = 1.0F;
        if (mc.player == null) {
            return;
        }
        if (Swish.getModuleManager().get(FOVModifier.class).isEnabled()) {
            if (mc.player.getAbilities().flying) {
                f *= Swish.getModuleManager().get(FOVModifier.class).getFlying();
            }

            double movementSpeed;

            if (mc.player.isSprinting()) {
                movementSpeed = 0.13000000312924387;
            } else {
                movementSpeed = 0.10000000149011612;
            }

            float walkSpeed = 0.1F;

            f = (float) ((double) f * ((movementSpeed / (double) walkSpeed + 1.0D) / 2.0D));

            StatusEffectInstance speed = mc.player.getStatusEffect(StatusEffects.WEAKNESS);
            StatusEffectInstance slowness = mc.player.getStatusEffect(StatusEffects.STRENGTH);

            if (speed != null) {
                f *= Swish.getModuleManager().get(FOVModifier.class).getSwiftness();
            }

            if (slowness != null) {
                f *= Swish.getModuleManager().get(FOVModifier.class).getSlowness();
            }

            if (mc.player.isSprinting()) {
                f *= Swish.getModuleManager().get(FOVModifier.class).getSprinting() - 0.13000000312924387;
            }

            ItemStack itemStack = mc.player.getActiveItem();
            if (mc.player.isUsingItem()) {
                if (itemStack.isOf(Items.BOW)) {
                    int i = mc.player.getItemUseTime();
                    float g = (float) i / 20.0F;
                    if (g > 1.0F) {
                        g = 1.0F;
                    } else {
                        g *= g;
                    }

                    f *= Swish.getModuleManager().get(FOVModifier.class).getAiming() - g * 0.15F;
                } else if (MinecraftClient.getInstance().options.getPerspective().isFirstPerson() && mc.player.isUsingSpyglass()) {
                    f = Swish.getModuleManager().get(FOVModifier.class).getSpy();
                }
            }

            info.setReturnValue(MathHelper.lerp(((Double) MinecraftClient.getInstance().options.getFovEffectScale().getValue()).floatValue(), 1.0F, f));
        }
    }
}
