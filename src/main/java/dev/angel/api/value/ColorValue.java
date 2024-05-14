package dev.angel.api.value;

import dev.angel.impl.module.other.colours.Colours;

import java.awt.*;

public class ColorValue extends Value<Color> {
    private boolean global;

    public ColorValue(String[] aliases, Color color, boolean global) {
        super(aliases,color);
        this.value = color;
        this.global = global;
    }

    public Color getColor() {
        if (isGlobal()) {
            return Colours.get().getColourCustomAlpha(value.getAlpha());
        }
        return value;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isGlobal() {
        return global;
    }
}