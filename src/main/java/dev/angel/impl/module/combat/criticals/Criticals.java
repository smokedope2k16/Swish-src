package dev.angel.impl.module.combat.criticals;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", new String[]{"Criticals", "Crits"}, "Always lands critical hits", Category.COMBAT);
        this.offerListeners(new ListenerAttackEntity(this));
    }
}
