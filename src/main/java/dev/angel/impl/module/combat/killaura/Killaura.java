package dev.angel.impl.module.combat.killaura;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import net.minecraft.entity.player.PlayerEntity;

public class Killaura extends Module {

    protected final Value<Boolean> rotate = new Value<>(
            new String[]{"Rotations", "Rotate"},
            "Rotates towards the player",
            true
    );

    protected final Value<Boolean> swordOnly = new Value<>(
            new String[]{"SwordOnly", "OnlySword"},
            "Only attacks if we are holding a sword",
            true
    );

    protected final NumberValue<Float> playerRange = new NumberValue<>(
            new String[]{"PlayerRange", "playerDistance", "Range", "distance"},
            "How close a player has to be to be considered a target",
            4.2f, 1.0f, 6.0f, 0.1f
    );

    protected PlayerEntity target;

    public Killaura() {
        super("KillAura", new String[]{"KillAura", "Aura"}, "Automatically attacks people", Category.COMBAT);
        this.offerValues(playerRange, swordOnly, rotate);
        this.offerListeners(new ListenerMotion(this));
    }
}
