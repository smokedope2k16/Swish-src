package dev.angel.api.event.listener;

import dev.angel.api.event.bus.Listener;
import dev.angel.api.interfaces.Minecraftable;

public abstract class ModuleListener<M, E> extends Listener<E> implements Minecraftable {
    protected final M module;

    public ModuleListener(M module, Class<? super E> target) {
        this(module, target, 10);
    }

    public ModuleListener(M module, Class<? super E> target, int priority) {
        this(module, target, priority, null);
    }

    public ModuleListener(M module, Class<? super E> target, Class<?> type) {
        this(module, target, 10, type);
    }

    public ModuleListener(M module, Class<? super E> target, int priority, Class<?> type) {
        super(target, priority, type);
        this.module = module;
    }

}

