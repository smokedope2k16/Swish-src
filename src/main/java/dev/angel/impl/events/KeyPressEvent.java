package dev.angel.impl.events;

import dev.angel.api.event.events.Event;
import dev.angel.api.util.keyboard.KeyPressAction;

public class KeyPressEvent extends Event {
    private final int key;
    private final KeyPressAction action;

    public KeyPressEvent(int key, KeyPressAction action) {
        this.key = key;
        this.action = action;
    }

    public int getKey() {
        return key;
    }

    public KeyPressAction getAction() {
        return action;
    }
}