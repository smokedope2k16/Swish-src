package dev.angel.impl.module.player.optimizer;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.Value;

public class Optimizer extends Module {

    protected final Value<Boolean> place = new Value<>(
            new String[]{"Placements", "place"},
            "Removes crystal place delay",
            false
    );

    protected final Value<Boolean> attack = new Value<>(
            new String[]{"Breaking", "break"},
            "Sets crystals as dead before the server does",
            true
    );

    public Optimizer() {
        super("Optimizer", new String[]{"Optimizer", "CrystalOptimizer", "Marlowmode", "CrystalFix"}, "Modifies crystal interactions", Category.PLAYER);
        this.offerValues(place, attack);
        this.offerListeners(new ListenerAttack(this), new ListenerTick(this));
    }
}
