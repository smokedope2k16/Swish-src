package dev.angel.impl.module.movement.noslow;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.TickEvent;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ListenerTick extends ModuleListener<NoSlow, TickEvent> {
    public ListenerTick(NoSlow module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.player != null) {
            if (module.slowFalling.getValue()) {
                StatusEffectInstance slowfall = mc.player.getStatusEffect(StatusEffects.SLOW_FALLING);
                if (slowfall != null) {
                    mc.player.getActiveStatusEffects().remove(slowfall.getEffectType());
                }
            }
        }
    }
}
