package dev.angel.impl.module.movement.noaccel;

import dev.angel.Swish;
import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.impl.events.MoveEvent;
import dev.angel.impl.module.movement.holesnap.Holesnap;

public class ListenerMove extends ModuleListener<NoAccel, MoveEvent> {
    public ListenerMove(NoAccel module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (mc.player == null) {
            return;
        }
        if (mc.player.isSneaking()
            || Swish.getModuleManager().get(Holesnap.class).isEnabled()
            || (!mc.player.isOnGround() && !module.air.getValue())
            || ((mc.player.isInLava() || mc.player.isSubmergedInWater()) && !module.water.getValue())) {
            return;
        }
        module.strafe(event, EntityUtil.getDefaultMoveSpeed());
    }
}
