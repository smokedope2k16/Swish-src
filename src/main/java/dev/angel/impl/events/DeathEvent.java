package dev.angel.impl.events;

import dev.angel.api.event.events.Event;
import net.minecraft.entity.LivingEntity;

public class DeathEvent extends Event{
    private final LivingEntity entity;

    public DeathEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

}