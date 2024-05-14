package dev.angel.impl.module.combat.killaura;

import dev.angel.api.event.events.Stage;
import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.api.util.math.MathUtil;
import dev.angel.api.util.math.RotationsUtil;
import dev.angel.impl.events.MotionUpdateEvent;
import net.minecraft.item.AxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;

public class ListenerMotion extends ModuleListener<Killaura, MotionUpdateEvent> {
    public ListenerMotion(Killaura module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (event.getStage() == Stage.PRE) {
            module.target = EntityUtil.getClosestEnemy();
            if (module.target != null && mc.player != null && mc.interactionManager != null) {
                if (!(module.target.squaredDistanceTo(mc.player) > MathUtil.square(module.playerRange.getValue()))) {
                    boolean weapon = (mc.player.getMainHandStack().getItem() instanceof SwordItem) || (mc.player.getMainHandStack().getItem() instanceof AxeItem);
                    if (module.swordOnly.getValue() && !weapon) {
                        return;
                    }

                    if (module.rotate.getValue()) {
                        float[] rotations = RotationsUtil.getRotations(mc.player, module.target, 1, 180);
                        mc.player.setYaw(rotations[0]);
                        mc.player.setPitch(rotations[1]);
                        //Swish.getRotationManager().setRotations(rotations);
                    }

                    if (mc.player.getAttackCooldownProgress(0) >= 1.0f) {
                        mc.interactionManager.attackEntity(mc.player, module.target);
                        mc.player.swingHand(Hand.MAIN_HAND);
                    }
                }
            }
        }
    }
}
