package dev.angel.api.event.bus.api;

import dev.angel.api.event.bus.Listener;

import java.util.Collection;

public interface Subscriber {
    Collection<Listener<?>> getListeners();
}

