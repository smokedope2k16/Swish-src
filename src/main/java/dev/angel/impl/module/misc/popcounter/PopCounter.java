package dev.angel.impl.module.misc.popcounter;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.Value;

public class PopCounter extends Module {

    private final Value<Boolean> clearOnVisualRange = new Value<>(
            new String[]{"VisualRange"},
            "Clears totem pops whenever they leave visual range",
            true
    );

    private final Value<Boolean> alternative = new Value<>(
            new String[]{"Alternative", "alt", "ordinal"},
            "Alternative pop counting message",
            true
    );

    public PopCounter() {
        super("PopCounter", new String[]{"popcounter", "totempopcounter"}, "Announces whe someone pops in chat.", Category.MISC);
        this.offerListeners(new ListenerPop(this), new ListenerDeath(this));
        this.offerValues(clearOnVisualRange, alternative);
    }

    protected String appendSuffix(int pops) {
        if (pops == 1) {
            return "st";
        }
        if (pops == 2) {
            return "nd";
        }
        if (pops == 3) {
            return "rd";
        }
        if (pops >= 4 && pops < 21) {
            return "th";
        }
        int lastDigit = pops % 10;
        if (lastDigit == 1) {
            return "st";
        }
        if (lastDigit == 2) {
            return "nd";
        }
        if (lastDigit == 3) {
            return "rd";
        }
        return "th";
    }

    public boolean alternative() {
        return this.alternative.getValue();
    }

    public boolean visualRange() {
        return clearOnVisualRange.getValue();
    }
}
