package dev.angel.impl.module.player.fastplace;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.NumberValue;

public class FastPlace extends Module {

    protected final NumberValue<Integer> delay = new NumberValue<>(
            new String[]{"Delay", "Rightclickdelay"},
            "What we change the delay to",
            1, 0, 4
    );

    public FastPlace() {
        super("FastPlace", new String[]{"FastPlace", "FastUse"}, "Changes minecraft clickdelay", Category.PLAYER);
        this.offerValues(delay);
        this.offerListeners(new ListenerTick(this));
    }
}
