package dev.angel.api.value;

import dev.angel.api.interfaces.Labeled;
import dev.angel.api.value.util.KeyBind;

import java.awt.*;

@SuppressWarnings("TypeParameterHidesVisibleType")
public class Value<T> implements Labeled {
    private final String[] aliases;
    private String description;
    protected T value;

    public Value(String[] aliases, String description, T value) {
        this.aliases = aliases;
        this.description = description;
        this.value = value;
    }

    public Value(String[] aliases, T value) {
        this.aliases = aliases;
        this.value = value;
    }

    @Override
    public String getLabel() {
        return aliases[0];
    }

    public void setValue(T in) {
        value = in;
    }

    public T getValue() {
        return value;
    }

    public <T> String getClassName(T value) {
        return value.getClass().getSimpleName();
    }

    public boolean isNumber() {
        return (value instanceof Double || value instanceof Integer || value instanceof Short || value instanceof Long || value instanceof Float);
    }

    public boolean isEnum() {
        return !isNumber()
                && !(value instanceof KeyBind)
                && !(value instanceof Character)
                && !(value instanceof Boolean)
                && !(value instanceof Color)
                && !(value instanceof List);
    }

    public String getType() {
        if (this.isEnum()) {
            return "Enum";
        } else if (this instanceof BindValue) {
            return "Bind";
        }

        return this.getClassName(this.value);
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public boolean isString() {
        return value instanceof String;
    }
}