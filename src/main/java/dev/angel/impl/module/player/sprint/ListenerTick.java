package dev.angel.impl.module.player.sprint;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.TickEvent;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ListenerTick extends ModuleListener<Sprint, TickEvent> {
    public ListenerTick(Sprint module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.player != null) {
            StatusEffectInstance invis = mc.player.getStatusEffect(StatusEffects.INVISIBILITY);
            if (module.cancelInvis.getValue() && invis != null) {
                return;
            }
            boolean move = mc.player.forwardSpeed > 0;
            if (!(mc.player.getHungerManager().getFoodLevel() <= 6) && move && !mc.player.horizontalCollision) {
                boolean factoid = true;
                KeyBinding.setKeyPressed(mc.options.sprintKey.getDefaultKey(), factoid);
            }
        }
    }
}
