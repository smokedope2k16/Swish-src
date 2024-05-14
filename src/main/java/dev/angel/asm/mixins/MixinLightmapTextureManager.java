package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.impl.module.render.fullbright.Fullbright;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LightmapTextureManager.class)
public class MixinLightmapTextureManager implements Minecraftable {

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V"))
    private void update(Args args) {
        if (Swish.getModuleManager().get(Fullbright.class).isEnabled()) {
            args.set(2, 0xFFFFFFFF);
        }
    }
}

  /*  @Shadow
    @Final
    private NativeImage image;

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImageBackedTexture;upload()V", shift = At.Shift.BEFORE))
    private void update(CallbackInfo info) {
        final Fullbright full = Swish.getModuleManager().get(Fullbright.class);
        if (full.isEnabled()) {
            for (int n = 0; n < 16; ++n) {
                for (int o = 0; o < 16; ++o) {
                    int alpha = full.getColor().getAlpha();
                    float modifier = (float) alpha / 255.0f;
                    int color = this.image.getColor(o, n);
                    int[] bgr = toRGBAArray(color);
                    Vector3f values = new Vector3f(
                            (float) bgr[2] / 255.0f,
                            (float) bgr[1] / 255.0f,
                            (float) bgr[0] / 255.0f);
                    Vector3f newValues = new Vector3f(
                            (float) full.getColor().getRed() / 255.0f,
                            (float) full.getColor().getGreen() / 255.0f,
                            (float) full.getColor().getBlue() / 255.0f);
                    Vector3f value = mix(values, newValues, modifier);
                    int red = (int) (value.x * 255.0f);
                    int green = (int) (value.y * 255.0f);
                    int blue = (int) (value.z * 255.0f);
                    this.image.setColor(o, n, (-16777216 | blue << 16 | green << 8 | red));
                }
            }
        }
    }

    private int[] toRGBAArray(int colorBuffer) {
        return new int[]{colorBuffer >> 16 & 0xFF, colorBuffer >> 8 & 0xFF, colorBuffer & 0xFF};
    }

    private Vector3f mix(Vector3f first, Vector3f second, float factor) {
        return new Vector3f(first.x * (1.0f - factor) + second.x * factor, first.y * (1.0f - factor) + second.y * factor, first.z * (1.0f - factor) + first.z * factor);
    } */