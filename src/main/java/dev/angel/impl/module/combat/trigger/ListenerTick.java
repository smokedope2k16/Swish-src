package dev.angel.impl.module.combat.trigger;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.impl.events.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.concurrent.ThreadLocalRandom;

public class ListenerTick extends ModuleListener<Trigger, TickEvent> {
    public ListenerTick(Trigger module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.player != null && mc.interactionManager != null) {
            if (module.weaponOnly.getValue() && ItemUtil.isTool(mc.player.getMainHandStack())) {
                return;
            }
            HitResult hit = mc.crosshairTarget;
            if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
                Entity target = ((EntityHitResult) hit).getEntity();

                if (module.playersOnly.getValue() && !(target instanceof PlayerEntity)) {
                    return;
                }

                if (module.leftClickOnly.getValue() && !mc.options.attackKey.isPressed()) {
                    return;
                }

                if (!target.isOnGround() && !module.attackInAir.getValue()) {
                    return;
                }

                if (target.isInvisible() && !module.attackWhenInvis.getValue()) {
                    return;
                }

                if (mc.player.isUsingItem()) {
                    return;
                }

                if (!mc.player.isOnGround() && module.onGround.getValue()) {
                    return;
                }

                if (mc.player.getAttackCooldownProgress(0) >= 1.0f) {
                    if (module.timer.getTime() > 600) {
                        module.timer.reset();
                    }
                    int delay = module.delay.getValue() == 0 ? 0 : module.delay.getValue();
                    switch (module.randomize.getValue()) {
                        case NONE -> delay = module.delay.getValue();
                        case NORMAL -> delay = ThreadLocalRandom.current().nextInt(module.delay.getValue() * 5);
                        case EXTRA -> delay = ThreadLocalRandom.current().nextInt(module.delay.getValue()) * 5;
                    }
                    if (module.timer.passed(delay)) {
                        mc.interactionManager.attackEntity(mc.player, target);
                        mc.player.swingHand(Hand.MAIN_HAND);
                    }
                }
            }
        }
    }
}
