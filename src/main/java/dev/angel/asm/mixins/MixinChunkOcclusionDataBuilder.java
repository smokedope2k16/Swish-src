package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.impl.events.CubeOpacityEvent;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkOcclusionDataBuilder.class)
public class MixinChunkOcclusionDataBuilder {

    @Inject(at = @At("HEAD"), method = {"markClosed(Lnet/minecraft/util/math/BlockPos;)V"}, cancellable = true)
    private void onMarkClosed(BlockPos pos, CallbackInfo ci) {
        CubeOpacityEvent event = new CubeOpacityEvent();
        Swish.getEventBus().dispatch(event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
