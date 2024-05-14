package dev.angel.impl.module.combat.autototem;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.math.StopWatch;
import dev.angel.api.value.EnumValue;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import dev.angel.impl.module.combat.autototem.mode.AutoTotemMode;

public class AutoTotem extends Module {

    public final EnumValue<AutoTotemMode> mode = new EnumValue<>(
            new String[]{"Mode", "method", "modejamin"},
            "Legit: - legit looking autototem | Blatant: - blatant looking autototem",
            AutoTotemMode.LEGIT
    );

    protected final Value<Boolean> openInventory = new Value<>(
            new String[]{"OpenInventory", "openinv", "whywouldyouusethis"},
            "Opens inventory when swapping from non hotbar slot",
            true
    );

    protected final NumberValue<Integer> invDelay = new NumberValue<>(
            new String[]{"InventoryDelay", "InvDelay"},
            "Delay for opening inventory",
            2, 0, 10
    );

    protected final Value<Boolean> autoEquip = new Value<>(
            new String[]{"AutoEquip", "Equip"},
            "Automatically equips totems when holding them",
            true
    );

    protected final NumberValue<Integer> equipDelay = new NumberValue<>(
            new String[]{"EquipDelay", "AutoEquipDelay"},
            "Delay for equipping a totem",
            6, 0, 10
    );

    protected final Value<Boolean> refill = new Value<>(
            new String[]{"Refill", "totemrefill", "autorefill"},
            "Puts totems in your hotbar",
            true
    );

    public final NumberValue<Integer> refillSlot = new NumberValue<>(
            new String[]{"RefillSlot", "refillslotington", "totemhotbarslot"},
            "Which hotbar slot should totems be refilled into",
            7, 1, 9
    );


    protected final Value<Boolean> autoSwap = new Value<>(
            new String[]{"AutoSwap", "Swap", "AutoSwitch"},
            "Automatically switches to a totem when you dont have a totem in your offhand",
            true
    );

    protected final NumberValue<Integer> swapDelay = new NumberValue<>(
            new String[]{"SwapDelay", "SwitchDelay"},
            "Delay for swapping to the totem",
            4, 1, 10
    );

    protected final StopWatch swapTimer = new StopWatch();
    protected final StopWatch equipTimer = new StopWatch();
    protected final StopWatch inventoryTimer = new StopWatch();
    protected final StopWatch refillTimer = new StopWatch();

    public AutoTotem() {
        super("AutoTotem", new String[]{"autototem", "autotem", "offhand"}, "Automatically places a totem in your offhand", Category.COMBAT);
        this.offerListeners(new ListenerTick(this));
        this.offerValues(mode, openInventory, invDelay, autoEquip, equipDelay, refill, refillSlot, autoSwap, swapDelay);
    }

    @Override
    public void onEnable() {
        swapTimer.reset();
        equipTimer.reset();
        inventoryTimer.reset();
        refillTimer.reset();
    }
}
