package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.impl.events.Render3DEvent;
import dev.angel.impl.module.misc.reach.Reach;
import dev.angel.impl.module.render.fovmodifier.FOVModifier;
import dev.angel.impl.module.render.norender.NoRender;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0),
            method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V")
	private void renderWorldHook(float partialTicks, long finishTimeNano, MatrixStack matrixStack, CallbackInfo ci) {
		Render3DEvent event = new Render3DEvent(matrixStack, partialTicks);
		Swish.getEventBus().dispatch(event);
	}

    @Inject(method = "getFov", at = @At("TAIL"), cancellable = true)
    private void getFovHook(Camera camera, float partialTicks, boolean useFovSetting, CallbackInfoReturnable<Double> cir) {
        FOVModifier FOV_MODIFIER = Swish.getModuleManager().get(FOVModifier.class);
        if (useFovSetting && FOV_MODIFIER != null && FOV_MODIFIER.isCustomFov()) {
            cir.setReturnValue(FOV_MODIFIER.getFov());
        }
    }

    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    private void onShowFloatingItem(ItemStack floatingItem, CallbackInfo info) {
        if (floatingItem.getItem() == Items.TOTEM_OF_UNDYING && Swish.getModuleManager().get(NoRender.class).noTotems()) {
            info.cancel();
        }
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 3))
    private double updateTargetedEntityModifySurvivalReach(double d) {
        return Swish.getModuleManager().get(Reach.class).getPlayerDistance();
    }
}
