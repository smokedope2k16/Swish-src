package dev.angel.api.command;

import dev.angel.api.interfaces.Minecraftable;

public abstract class Command implements Minecraftable {
    private final String[] alias;

    public Command(String[] alias) {
        this.alias = alias;
    }

    public abstract void run(String[] args);

    public String[] getAlias() {
        return this.alias;
    }
}
