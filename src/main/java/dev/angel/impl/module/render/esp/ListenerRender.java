package dev.angel.impl.module.render.esp;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.render.Interpolation;
import dev.angel.api.util.render.RenderMethods;
import dev.angel.impl.events.Render3DEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

@SuppressWarnings("ConstantConditions")
public class ListenerRender extends ModuleListener<ESP, Render3DEvent> {
    public ListenerRender(ESP module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        MatrixStack matrix = event.getMatrix();

        RenderMethods.enable3D();

        matrix.push();

        for (Entity entity : mc.world.getEntities()) {
            Vec3d vec = Interpolation.interpolateEntity(entity);

            if (entity instanceof ItemEntity && module.items.getValue()) {
                RenderMethods.color(module.color.getColor().getRGB());
                RenderMethods.drawOutline(matrix, getInterpolatedBox(entity, vec));
                RenderMethods.resetColor();
            }

            if (entity instanceof EnderPearlEntity && module.pearls.getValue()) {
                RenderMethods.color(module.color.getColor().getRGB());
                RenderMethods.drawOutline(matrix, getInterpolatedBox(entity, vec));
                RenderMethods.resetColor();
            }

            if (entity instanceof PlayerEntity && module.player.getValue() && entity != mc.player) {
                RenderMethods.color(module.color.getColor().getRGB());
                RenderMethods.drawOutline(matrix, getInterpolatedBox(entity, vec).shrink(0.05, 0.05, 0.05));
                RenderMethods.resetColor();
            }
        }

        matrix.pop();

        RenderMethods.disable3D();
    }

    //move this to interpolation util?
    private Box getInterpolatedBox(Entity entity, Vec3d interpolation) {
        return new Box(0.0, 0.0, 0.0, entity.getWidth(), entity.getHeight(),
                entity.getWidth()).offset(
                        interpolation.x - (double)(entity.getWidth() / 2.0F),
                        interpolation.y,
                        interpolation.z - (double)(entity.getWidth() / 2.0F))
                .expand(0.05);
    }
}
