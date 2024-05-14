package dev.angel.impl.module.render.blockhighlight;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.color.ColorUtil;
import dev.angel.api.util.render.Interpolation;
import dev.angel.api.util.render.RenderMethods;
import dev.angel.impl.events.Render3DEvent;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

@SuppressWarnings("ConstantConditions")
public class ListenerRender extends ModuleListener<BlockHighlight, Render3DEvent> {
    public ListenerRender(BlockHighlight module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (mc.crosshairTarget == null || !(mc.crosshairTarget instanceof BlockHitResult result)) {
            return;
        }

        BlockPos pos = new BlockPos(result.getBlockPos());
        Box box = Interpolation.interpolatePos(pos);
        Block block = mc.world.getBlockState(pos).getBlock();
        MatrixStack matrix = event.getMatrix();

        if (block instanceof AirBlock || block instanceof FluidBlock) {
            return;
        }

        //enables gl
        RenderMethods.enable3D();

        //push
        matrix.push();

        //box
        if (module.box.getValue()) {
            RenderMethods.color(ColorUtil.changeAlpha(module.color.getColor(), module.color.getColor().getAlpha()).getRGB());
            RenderMethods.drawBox(matrix, box);
        }

        //outline
        RenderMethods.color(ColorUtil.changeAlpha(module.outlineColor.getColor(), module.outlineColor.getColor().getAlpha()).getRGB());
        RenderMethods.drawOutline(matrix, box);

        //pop
        matrix.pop();

        //reset gl and colors
        RenderMethods.resetColor();
        RenderMethods.disable3D();
    }
}
