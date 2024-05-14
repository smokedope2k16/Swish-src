package dev.angel.api.util.friend;

import dev.angel.api.interfaces.Labeled;

public class Friend implements Labeled {
    private final String label;

    public Friend(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return this.label;
    }
}