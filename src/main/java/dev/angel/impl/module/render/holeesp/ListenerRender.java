package dev.angel.impl.module.render.holeesp;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.color.ColorUtil;
import dev.angel.api.util.render.Interpolation;
import dev.angel.api.util.render.RenderMethods;
import dev.angel.impl.events.Render3DEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.awt.*;

import static dev.angel.api.util.block.HoleUtil.Hole;

public class ListenerRender extends ModuleListener<HoleESP, Render3DEvent> {
    public ListenerRender(HoleESP module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        MatrixStack matrix = event.getMatrix();

        //enables gl
        RenderMethods.enable3D();

        //push
        matrix.push();

        for (Hole hole : module.holes) {
            Color color = hole.safe
                    ? module.getBedrockColor()
                    : module.getObbyColor();

            Box bb = Interpolation.interpolatePos(hole.pos1, module.height.getValue());

            if (hole.pos2 != null) {
                bb = new Box(
                        hole.pos1.getX() - Interpolation.getCameraPos().x,
                        hole.pos1.getY() - Interpolation.getCameraPos().y,
                        hole.pos1.getZ() - Interpolation.getCameraPos().z,
                        hole.pos2.getX() + 1 - Interpolation.getCameraPos().x,
                        hole.pos2.getY() + module.height.getValue() - Interpolation.getCameraPos().y,
                        hole.pos2.getZ() + 1 - Interpolation.getCameraPos().z
                );
            }
            RenderMethods.color(color.getRGB());
            RenderMethods.drawBox(event.getMatrix(), bb);
            drawOutline(event.getMatrix(), bb, ColorUtil.changeAlpha(color, module.wireAlpha.getValue()));
        }

        for (BlockPos pos : module.voidHoles) {
            Box bb = Interpolation.interpolatePos(pos, module.height.getValue());
            RenderMethods.color(module.getVoidColor().getRGB());
            RenderMethods.drawBox(event.getMatrix(), bb);
        }

        //pop
        matrix.pop();

        //reset gl and colors
        RenderMethods.resetColor();
        RenderMethods.disable3D();
    }

    private void drawOutline(MatrixStack stack, Box bb, Color color) {
        switch (module.outlineMode.getValue()) {
            case NORMAL -> {
                RenderMethods.color(ColorUtil.changeAlpha(color, color.getAlpha()).getRGB());
                RenderMethods.drawOutline(stack, bb);
            }
            case CROSS -> {
                RenderMethods.color(ColorUtil.changeAlpha(color, color.getAlpha()).getRGB());
                RenderMethods.drawCross(stack, bb);
            }
        }
    }
}
