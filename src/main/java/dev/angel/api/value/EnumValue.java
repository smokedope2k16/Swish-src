package dev.angel.api.value;

import dev.angel.api.value.util.EnumHelper;

@SuppressWarnings("unchecked")
public class EnumValue<T extends Enum<?>> extends Value<T> {
    public EnumValue(String[] aliases, String description, T value) {
        super(aliases, description, value);
    }

    public String getFixedValue() {
        return (value).name().charAt(0) + (value).name().toLowerCase().replaceFirst(Character.toString((value).name().charAt(0)).toLowerCase(), "");
    }

    public void setValueFromString(String string) {
        Enum<?> entry = EnumHelper.fromString(this.value, string);
        setValue((T) entry);
    }

    public void increment() {
        Enum<?>[] array = (getValue()).getClass().getEnumConstants();
        int length = array.length;
        for (int i = 0; i < length; ++i) {
            if (!array[i].name().equalsIgnoreCase(getFixedValue())) continue;
            if (++i > array.length - 1) {
                i = 0;
            }
            setValue((T) array[i]);
        }
    }

    public void decrement() {
        Enum<?>[] array = (getValue()).getClass().getEnumConstants();
        int length = array.length;
        for (int i = 0; i < length; ++i) {
            if (!array[i].name().equalsIgnoreCase(getFixedValue())) continue;
            if (--i < 0) {
                i = array.length - 1;
            }
            setValue((T) array[i]);
        }
    }
}

