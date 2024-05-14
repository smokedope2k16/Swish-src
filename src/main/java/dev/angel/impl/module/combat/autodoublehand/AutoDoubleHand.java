package dev.angel.impl.module.combat.autodoublehand;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.api.value.NumberValue;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.explosion.Explosion;

public class AutoDoubleHand extends Module {

    protected final NumberValue<Integer> chance = new NumberValue<>(
            new String[]{"Chance", "%"},
            "What percent chance we have of switching",
            80, 0, 100
    );

    protected final NumberValue<Integer> range = new NumberValue<>(
            new String[]{"PlayerRange", "TargetRange", "Distance", "Range", "rang"},
            "How close a player has to be to activate",
            6, 1, 12
    );

    public AutoDoubleHand() {
        super("AutoDoubleHand", new String[]{"AutoDoubleHand", "AntiDTap", "NoDTap"}, "Swaps to a totem in your inventory when you're about to die", Category.COMBAT);
        this.offerListeners(new ListenerTick(this), new ListenerGameJoin(this));
        this.offerValues(chance, range);
    }
}