package dev.angel.impl.module.combat.autoobsidian;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.api.util.math.StopWatch;
import dev.angel.api.value.Value;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

// for hitcrytsal wtv wtv
public class AutoObsidian extends Module {

    protected final Value<Boolean> swapToCrystal = new Value<>(
            new String[]{"SwapToCrystal", "Crystal", "Swap"},
            "Switches to a crystal before disabling",
            true
    );

    protected final Value<Boolean> placeCrystal = new Value<>(
            new String[]{"PlaceCrystal", "Place", "AutoPlace"},
            "Places a crystal if we are holding one",
            true
    );

    protected final Value<Boolean> breakCrystal = new Value<>(
            new String[]{"BreakCrystal", "break", "Autobreak"},
            "Tries to break the crystal after placing",
            true
    );

    protected final StopWatch timer = new StopWatch();

    public AutoObsidian() {
        super("AutoObsidian", new String[]{"AutoObsidian", "AutoObby", "AutoObbyPLace"}, "Automatically places obsidian", Category.COMBAT);
        this.offerValues(swapToCrystal, placeCrystal, breakCrystal);
        this.offerListeners(new ListenerTick(this));
    }

    @Override
    public void onEnable() {
        if (mc.player != null && mc.interactionManager != null && mc.world != null) {
            int obbySlot = ItemUtil.findHotbarItem(Items.OBSIDIAN);
            HitResult hit = mc.crosshairTarget;
            if (hit == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) {
                return;
            }
            if (obbySlot != -1) {
                InventoryUtil.swap(obbySlot);
                if (ItemUtil.isHolding(Items.OBSIDIAN)) {
                    BlockHitResult blockHit = (BlockHitResult) hit;
                    BlockHitResult blockResult = new BlockHitResult(hit.getPos(), blockHit.getSide(), blockHit.getBlockPos(), false);
                    ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockResult);
                    if (result == ActionResult.SUCCESS) {
                        mc.player.swingHand(Hand.MAIN_HAND);
                        timer.reset();
                    }
                } else {
                    disable();
                }
            }
        }
    }
}
