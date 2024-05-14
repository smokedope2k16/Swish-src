package dev.angel.impl.events;

import dev.angel.api.event.events.Event;
import net.minecraft.entity.Entity;

public class EntityWorldEvent extends Event {
    private final Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public EntityWorldEvent(Entity entity) {
        this.entity = entity;
    }

    public static class Add extends EntityWorldEvent {
        public Add(Entity entity) {
            super(entity);
        }
    }

    public static class Remove extends EntityWorldEvent {
        public Remove(Entity entity) {
            super(entity);
        }
    }

}