package dev.angel.impl.module.movement.holesnap;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.block.HoleUtil;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.api.util.logging.Logger;
import dev.angel.api.util.text.TextColor;
import dev.angel.impl.events.MoveEvent;

public class MoveListener extends ModuleListener<Holesnap, MoveEvent> {
    public MoveListener(Holesnap module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (mc.world == null || mc.player == null) {
            return;
        }
        if (mc.player.isSpectator()) {
            return;
        }
        if (module.isSafe()) {
            Logger.getLogger().log(TextColor.RED + "<HolePull> Entered a hole.", 45088);
            module.disable();
            return;
        }
        if (module.hole != null && mc.world.isAir(module.hole.pos1)) {
            module.snap(event);
            if ((mc.player.horizontalCollision && mc.player.isOnGround())) {
                ++module.stuck;
                if (module.stuck == 10) {
                    Logger.getLogger().log(TextColor.RED + "<HolePull> Player got stuck.", 45088);
                    module.disable();
                }
            }
            else {
                module.stuck = 0;
            }
        }
        else {
            Logger.getLogger().log(TextColor.RED + "<HolePull> Hole no longer exists.", 45088);
            module.disable();
        }
    }
}
