package dev.angel.impl.events;

import dev.angel.api.event.events.Stage;
import dev.angel.api.event.events.StageEvent;

public class MotionUpdateEvent extends StageEvent {
    public MotionUpdateEvent(Stage stage) {
        super(stage);
    }
}
