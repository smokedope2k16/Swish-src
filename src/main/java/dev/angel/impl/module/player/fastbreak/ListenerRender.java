package dev.angel.impl.module.player.fastbreak;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.color.ColorUtil;
import dev.angel.api.util.render.Interpolation;
import dev.angel.api.util.render.RenderMethods;
import dev.angel.impl.events.Render3DEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

import java.awt.*;

public class ListenerRender extends ModuleListener<FastBreak, Render3DEvent> {
    public ListenerRender(FastBreak module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (module.pos == null) {
            return;
        }

        final MatrixStack matrix = event.getMatrix();
        final Box box = Interpolation.interpolatePos(module.pos);

        //enables gl
        RenderMethods.enable3D();

        //push
        matrix.push();

        //color
        final boolean ready = module.timer.passed(module.startDelay.getValue() * 250L);
        final Color color = ready ? Color.GREEN : Color.RED;

        //box
        RenderMethods.color(ColorUtil.changeAlpha(color, 40).getRGB());
        RenderMethods.drawBox(matrix, box);

        //outline
        RenderMethods.color(ColorUtil.changeAlpha(color, 255).getRGB());
        RenderMethods.drawOutline(matrix, box);

        //pop
        matrix.pop();

        //reset gl and colors
        RenderMethods.resetColor();
        RenderMethods.disable3D();
    }
}
