package dev.angel.impl.module.misc.visualrange;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.Value;

public class VisualRange extends Module {

    protected final Value<Boolean> left = new Value<>(
            new String[]{"Leave", "leaves", "left"},
            "Notifies if a player leaves your visual range",
            false
    );

    public VisualRange() {
        super("VisualRange", new String[]{"VisualRange", "VisualRangeNotify", "VisRange"}, "Notifies you when players leave / enter your visual range", Category.MISC);
        this.offerValues(left);
        this.offerListeners(new ListenerAdd(this), new ListenerLeave(this));
    }
}
