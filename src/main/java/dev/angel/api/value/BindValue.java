package dev.angel.api.value;

import dev.angel.api.value.util.KeyBind;

public class BindValue extends Value<KeyBind> {
    public BindValue(String[] aliases, String description, KeyBind value) {
        super(aliases, description, value);
    }
}
