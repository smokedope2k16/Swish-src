package dev.angel.impl.module.combat.autodoublehand;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.impl.events.GameJoinEvent;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.explosion.Explosion;

public class ListenerGameJoin extends ModuleListener<AutoDoubleHand, GameJoinEvent> {
    public ListenerGameJoin(AutoDoubleHand module) {
        super(module, GameJoinEvent.class);
    }

    @Override
    public void call(GameJoinEvent event) {
        EntityUtil.explosion = new Explosion(mc.world, null, 0, 0, 0, 6, false, Explosion.DestructionType.DESTROY);
        if (mc.player != null) {
            EntityUtil.raycastContext = new RaycastContext(null, null, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, mc.player);
        }
    }
}
