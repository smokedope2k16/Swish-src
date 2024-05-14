package dev.angel.impl.module.combat.autocrystal;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.DeathEvent;
import net.minecraft.entity.player.PlayerEntity;

public class ListenerDeath extends ModuleListener<AutoCrystal, DeathEvent> {
    public ListenerDeath(AutoCrystal module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        if (mc.player != null
                && event.getEntity() != mc.player
                && event.getEntity() instanceof PlayerEntity
                && (mc.player.squaredDistanceTo(event.getEntity().getPos()) <= 6)) {
            module.deathTimer.reset();
        }
    }
}
