package dev.angel.asm.mixins;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import dev.angel.Swish;
import dev.angel.impl.events.AmbientOcclusionEvent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class MixinAbstractBlockState extends State<Block, BlockState> {
    protected MixinAbstractBlockState(Block owner, ImmutableMap<Property<?>, Comparable<?>> entries, MapCodec<BlockState> codec) {
        super(owner, entries, codec);
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(at = @At("TAIL"), method = {"getAmbientOcclusionLightLevel(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F"}, cancellable = true)
    private void onGetAmbientOcclusionLightLevel(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
        AmbientOcclusionEvent event = new AmbientOcclusionEvent((BlockState) (Object) this, cir.getReturnValueF());
        Swish.getEventBus().dispatch(event);

        cir.setReturnValue(event.getLight());
    }
}
