package dev.angel.impl.module.combat.autobowrelease;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.NumberValue;

public class AutoBowRelease extends Module {

    protected final NumberValue<Integer> bowCharge = new NumberValue<>(
            new String[]{"ChargeTo", "Charge", "Delay", "Tick"},
            "What point to charge the bow to before releasing",
            13, 0, 21
    );

    public AutoBowRelease() {
        super("AutoBowRelease", new String[]{"AutoBowRelesae", "FastBow", "BowTweaks", "BowRelease"}, "Automatically releases bow when charged to a point", Category.COMBAT);
        this.offerListeners(new ListenerTick(this));
        this.offerValues(bowCharge);
    }
}
