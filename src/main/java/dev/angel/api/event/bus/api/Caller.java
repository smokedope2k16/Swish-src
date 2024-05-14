package dev.angel.api.event.bus.api;

public interface Caller<E> {
    void call(E event);
}
