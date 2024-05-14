package dev.angel.api.module;

public class PersistentModule extends Module {
    public PersistentModule(String name, String[] aliases, String description, Category category) {
        super(name, aliases, description, category);
        setEnabled(true);
    }
}
