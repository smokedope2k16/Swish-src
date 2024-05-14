package dev.angel.impl.module.render.nametags;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.render.Interpolation;
import dev.angel.api.util.render.RenderUtil;
import dev.angel.impl.events.Render3DEvent;
import dev.angel.impl.module.render.nametags.mode.NametagMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ListenerRender extends ModuleListener<Nametags, Render3DEvent> {
    public ListenerRender(Nametags module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (mc.world == null) {
            return;
        }
        if (module.mode.getValue() == NametagMode.BLATANT) {
            final Entity renderEntity = RenderUtil.getEntity();
            final Vec3d interp = Interpolation.interpolateEntity(renderEntity);

            for (PlayerEntity player : mc.world.getPlayers()) {
                Vec3d vec = Interpolation.interpolateEntity(player);

                if (player == mc.player
                        || player == renderEntity
                        || !player.isAlive()) {
                    continue;
                }

                module.renderNametagsBlatant(player, vec.x, vec.y, vec.z, interp, event);
            }
        }
    }
}
