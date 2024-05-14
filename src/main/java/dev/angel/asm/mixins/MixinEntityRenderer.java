package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.impl.module.render.nametags.Nametags;
import dev.angel.impl.module.render.nametags.mode.NametagMode;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    public final Nametags nametags = Swish.getModuleManager().get(Nametags.class);

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    private void renderLabelIfPresent(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (nametags.isEnabled()) {
            if (nametags.mode.getValue() == NametagMode.BLATANT) {
                ci.cancel();
            }
        }
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    public void label(Args args) {
        if (nametags.isEnabled()) {
            if (nametags.mode.getValue() == NametagMode.BLATANT) {
                return;
            }

            Entity entity = args.get(0);
            Text text = args.get(1);

            if (entity instanceof ArmorStandEntity) {
                return;
            }

            if (text == null) {
                return;
            }

            args.set(1, Text.literal(nametags.getNametagLegit(entity)));
        }
    }

}
