package dev.angel.impl.module.combat.anchorbreaker;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.math.StopWatch;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
// TODO: switch to custom slot
public class AnchorBreaker extends Module {

    protected final NumberValue<Integer> maxCharges = new NumberValue<>(
            new String[]{"MaxCharges", "ChargeTo", "Charges"},
            "How much an anchor can be charged",
            2, 1, 4
    );

    protected final Value<Boolean> autoCharge = new Value<>(
            new String[]{"AutoCharge", "Charge"},
            "Automatically charges anchors",
            true
    );

    protected final NumberValue<Integer> chargeDelay = new NumberValue<>(
            new String[]{"ChargeDelay", "Delay"},
            "Delay for charging up the anchor",
            5, 0, 15
    );

    protected final Value<Boolean> autoExplode = new Value<>(
            new String[]{"AutoExplode", "Exploder", "Explode"},
            "Automatically explodes charged anchors",
            true
    );

    protected final NumberValue<Integer> explodeDelay = new NumberValue<>(
            new String[]{"ExplodeDelay", "DelayExplode"},
            "How long to wait before exploding the anchor",
            1, 0, 15
    );

    protected final Value<Boolean> autoDisable = new Value<>(
            new String[]{"AutoDisable", "Disabler", "Disable"},
            true
    );

    protected final StopWatch chargeTimer = new StopWatch();
    protected final StopWatch breakTimer = new StopWatch();

    public AnchorBreaker() {
        super("AnchorBreaker", new String[]{"AnchorBreaker", "AutoBreaker", "AutoBreak"}, "Automatically charges then breaks anchors", Category.COMBAT);
        this.offerValues(maxCharges, autoCharge, chargeDelay, autoExplode, explodeDelay, autoDisable);
        this.offerListeners(new ListenerTick(this));
    }

    @Override
    public void onEnable() {
        clear();
    }

    public void clear() {
        chargeTimer.reset();
        breakTimer.reset();
    }
}
