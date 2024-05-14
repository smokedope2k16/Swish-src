package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.impl.events.TesselateBlockEvent;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(TerrainRenderContext.class)
public class MixinTerrainRenderContext {

    @Inject(at = @At("HEAD"), method = "tessellateBlock", cancellable = true, remap = false)
    private void onTessellateBlock(BlockState blockState, BlockPos blockPos, final BakedModel model, MatrixStack matrixStack, CallbackInfo ci) {
        TesselateBlockEvent event = new TesselateBlockEvent(blockState);
        Swish.getEventBus().dispatch(event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
