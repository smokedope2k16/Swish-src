package dev.angel.impl.module.combat.wtap;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.TickEvent;
import dev.angel.impl.module.combat.wtap.mode.WTapMode;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class ListenerTick extends ModuleListener<WTap, TickEvent> {
    public ListenerTick(WTap module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.crosshairTarget != null && mc.player != null && mc.crosshairTarget.getType() == HitResult.Type.ENTITY && module.mode.getValue() == WTapMode.NORMAL) {
            final EntityHitResult hitResult = (EntityHitResult) mc.crosshairTarget;
            if (mc.player.squaredDistanceTo(hitResult.getEntity()) <= module.distance.getValue()) {
                if (module.timer.passed(module.delay.getValue() * 10)) {
                    switch (module.stage) {
                        case 1 -> {
                            KeyBinding.setKeyPressed(mc.options.forwardKey.getDefaultKey(), false);
                            if (module.timer.passed(module.duration.getValue() * 10)) {
                                module.stage = 2;
                            }
                        }
                        case 2 -> {
                            if (mc.options.forwardKey.isPressed()) {
                                KeyBinding.setKeyPressed(mc.options.forwardKey.getDefaultKey(), true);
                            }
                            module.stage = 0;
                            module.timer.reset();
                        }
                    }
                }
            }
        }
    }
}
