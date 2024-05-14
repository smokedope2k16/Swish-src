package dev.angel.impl.module.movement.elytrafly;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.asm.ducks.ILivingEntity;
import dev.angel.impl.events.UpdateEvent;

@SuppressWarnings("ConstantConditions")
public class UpdateListener extends ModuleListener<ElytraFly, UpdateEvent> {
    public UpdateListener(ElytraFly module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (module.autoJump.getValue()) {
            mc.options.jumpKey.setPressed(true);
        }

        if (module.autoWalk.getValue()) {
            mc.options.forwardKey.setPressed(true);
        }

        if (!mc.player.isFallFlying() && mc.player.fallDistance > 0 && module.checkElytra() && !mc.player.isFallFlying()) {
            module.castElytra();
        }

        ((ILivingEntity) mc.player).setLastJumpCooldown(0);
    }
}
