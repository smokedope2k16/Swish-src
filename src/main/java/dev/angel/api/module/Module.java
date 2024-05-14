package dev.angel.api.module;

import dev.angel.Swish;
import dev.angel.api.event.bus.Listener;
import dev.angel.api.event.bus.api.Subscriber;
import dev.angel.api.interfaces.Labeled;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.util.logging.Logger;
import dev.angel.api.value.BindValue;
import dev.angel.api.value.Value;
import dev.angel.api.value.util.KeyBind;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Module implements Subscriber, Minecraftable, Labeled {
    private final String name;
    private final String description;
    private final Category category;
    private final List<Listener<?>> listeners = new ArrayList<>();
    private final List<Value<?>> values;
    private final BindValue bind = new BindValue(new String[]{"Keybind", "bind", "b"}, "Current keybind of the module", new KeyBind(-1));
    private final String[] aliases;
    private boolean enabled;

    public Module(String name, String[] aliases, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.aliases = aliases;
        this.enabled = false;
        this.values = new ArrayList<>();
        this.offerValues(bind);
    }

    public void offerValues(Value<?>... properties) {
        Collections.addAll(this.values, properties);
    }

    public void offerListeners(Listener<?>... listeners) {
        Collections.addAll(this.listeners, listeners);
    }

    @SuppressWarnings("rawtypes")
    public Value getValue(String alias) {
        for (Value<?> value : values) {
            for (String aliases : value.getAliases()) {
                if (!alias.equalsIgnoreCase(aliases)) continue;
                return value;
            }
        }
        return null;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void setEnabled(boolean in) {
        enabled = in;
        if (enabled) {
            enable();
        } else if (!(this instanceof PersistentModule)) {
            disable();
        }
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void enable() {
        enabled = true;
        Logger.getLogger().log(Formatting.DARK_AQUA + this.getLabel() + Formatting.LIGHT_PURPLE + " was " + Formatting.GREEN + "enabled");
        if (!Swish.getEventBus().isSubscribed(this)) {
            Swish.getEventBus().subscribe(this);
        }
        onEnable();
    }

    public void enableNoMessage() {
        enabled = true;
        if (!Swish.getEventBus().isSubscribed(this)) {
            Swish.getEventBus().subscribe(this);
        }
        onEnable();
    }

    public void disable() {
        enabled = false;
        Logger.getLogger().log(Formatting.DARK_AQUA + this.getLabel() + Formatting.LIGHT_PURPLE + " was " + Formatting.RED + "disabled");
        onDisable();
        Swish.getEventBus().unsubscribe(this);
    }

    public void disableNoMessage() {
        enabled = false;
        onDisable();
        Swish.getEventBus().unsubscribe(this);
    }

    public int getBind() {
        return bind.getValue().getKey();
    }

    public void setBind(int bind) {
        this.bind.setValue(new KeyBind(bind));
    }

    public List<Value<?>> getValues() {
        return values;
    }

    public String getDescription() {
        return this.description;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getInfo() {
        return null;
    }

    public String[] getAliases() {
        return aliases;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public Collection<Listener<?>> getListeners() {
        return listeners;
    }
}
