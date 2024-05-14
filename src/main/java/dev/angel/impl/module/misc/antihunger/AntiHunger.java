package dev.angel.impl.module.misc.antihunger;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.Value;

public class AntiHunger extends Module {

    protected final Value<Boolean> sprint = new Value<>(
            new String[]{"Sprint", "Sprinting", "Run"},
            "Cancels sprinting packets",
            true
    );

    protected final Value<Boolean> ground = new Value<>(
            new String[]{"Ground", "OnGround"},
            "Cancels onground check",
            true
    );

    public AntiHunger() {
        super("AntiHunger", new String[]{"AntiHunger", "NoHunger"}, "Stops you from losing hunger", Category.MISC);
        this.offerListeners(new ListenerSprint(this), new ListenerMove(this));
        this.offerValues(sprint, ground);
    }
}
