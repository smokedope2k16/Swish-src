package dev.angel.impl.module.combat.trigger;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.math.StopWatch;
import dev.angel.api.value.EnumValue;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import dev.angel.impl.module.combat.autocrystal.mode.Randomize;

/**
 * @author moneymaker552
 */
public class Trigger extends Module {

    protected final Value<Boolean> playersOnly = new Value<>(
            new String[]{"PlayersOnly", "Players"},
            "Only attacks players",
            true
    );

    protected final Value<Boolean> weaponOnly = new Value<>(
            new String[]{"OnlyWeapon", "SwordOnly", "AxeOnly"},
            "Only attacks if we are holding a weapon",
            true
    );

    protected final Value<Boolean> leftClickOnly = new Value<>(
            new String[]{"LeftClickOnly", "LeftClicker", "Left"},
            "Only attacks if we are looking at the player & left click is held",
            true
    );

    protected final Value<Boolean> attackInAir = new Value<>(
            new String[]{"AttackInAir", "Air"},
            "Won't attack if the target is in the air",
            true
    );

    protected final Value<Boolean> attackWhenInvis = new Value<>(
            new String[]{"AttackInvis", "Invis"},
            "Attacks invisible players",
            false
    );

    protected final Value<Boolean> onGround = new Value<>(
            new String[]{"OnGround", "Ground"},
            "Only attacks if we are on ground",
            true
    );

    protected final EnumValue<Randomize> randomize = new EnumValue<>(
            new String[]{"Random", "Randomize"},
            "How much to randomize the delay",
            Randomize.EXTRA
    );

    protected final NumberValue<Integer> delay = new NumberValue<>(
            new String[]{"Wait", "Delay", "TriggerDelay"},
            "How long to wait before hitting a player after crosshair is charged",
            1, 0, 10
    );

    protected final StopWatch timer = new StopWatch();

    public Trigger() {
        super("Trigger", new String[]{"Trigger", "Triggerbot", "AutoHit"}, "Automatically hits an entity if we are looking at it", Category.COMBAT);
        this.offerListeners(new ListenerTick(this));
        this.offerValues(playersOnly, weaponOnly, leftClickOnly, attackInAir, attackWhenInvis, onGround, randomize, delay);
    }
}
