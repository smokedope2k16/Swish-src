package dev.angel.impl.module.combat.autocrystal;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.math.StopWatch;
import dev.angel.api.value.EnumValue;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import dev.angel.impl.module.combat.autocrystal.mode.Randomize;

public class AutoCrystal extends Module {

    protected final Value<Boolean> antiLootBlowup = new Value<>(
            new String[]{"AntiLootBlowUp", "AntiLoot", "Lethal", "StopOnKill"},
            "Stops crystaling for a small time window after someone dies nearby",
            false
    );

    protected final Value<Boolean> multiTask = new Value<>(
            new String[]{"MultiTask", "Eating", "Drinking"},
            "Will not activate autocrystal if we are eating or drinking",
            true
    );

    protected final Value<Boolean> onlyCrystal = new Value<>(
            new String[]{"OnlyCrystals", "CrystalHand", "Crystal"},
            "Only activates autocrystal if we are holding a crystal",
            true
    );

    protected final Value<Boolean> rightClickOnly = new Value<>(
            new String[]{"RightClickOnly", "RightOnly", "RightClick"},
            "Only activates autocrystal if we are holding right click",
            false
    );

    protected final EnumValue<Randomize> randomize = new EnumValue<>(
            new String[]{"Randomize", "Random"},
            "Randomizes delays",
            Randomize.NORMAL
    );

    protected final Value<Boolean> autoPlace = new Value<>(
            new String[]{"AutoPlace", "Place"},
            "Places crystals",
            true
    );

    protected final NumberValue<Integer> placeDelay = new NumberValue<>(
            new String[]{"PlaceDelay", "placerdelay"},
            "Delay between placing another crystal",
            1, 0, 25
    );

    protected final Value<Boolean> autoBreak = new Value<>(
            new String[]{"AutoBreak", "Break", "Destroy"},
            "Destroys crystals",
            true
    );

    protected final NumberValue<Integer> breakDelay = new NumberValue<>(
            new String[]{"BreakDelay", "breakerDelay"},
            "Delay between breaking another crystal",
            1, 0, 25
    );

    protected final StopWatch placeTimer = new StopWatch();
    protected final StopWatch breakTimer = new StopWatch();
    protected final StopWatch deathTimer = new StopWatch();

    public AutoCrystal() {
        super("AutoCrystal", new String[]{"AutoCrystal", "Crystaller"}, "Automatically places & breaks crystals", Category.COMBAT);
        this.offerListeners(new ListenerDeath(this), new ListenerTick(this));
        this.offerValues(antiLootBlowup, onlyCrystal, multiTask, rightClickOnly, randomize, autoPlace, placeDelay, autoBreak, breakDelay);
    }

    @Override
    public void onEnable() {
        clear();
    }

    private void clear() {
        placeTimer.reset();
        breakTimer.reset();
    }
}
