package dev.angel.impl.module.movement.noslow;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.Value;
import net.minecraft.client.option.KeyBinding;

public class NoSlow extends Module {

    protected final Value<Boolean> items = new Value<>(
            new String[]{"Items", "itemazoids", "itemjamins"},
            "Lets you move fast while eating",
            false
    );

    protected final Value<Boolean> slowFalling = new Value<>(
            new String[]{"SlowFalling", "NoSlowFalling", "SlowFall"},
            "Removes slow falling effect",
            true
    );

    public NoSlow() {
        super("NoSlow", new String[]{"NoSlow", "NoSlowDown"}, "Prevents slowdowns", Category.MOVEMENT);
        this.offerValues(items, slowFalling);
        this.offerListeners(new ListenerTick(this));
    }
}
