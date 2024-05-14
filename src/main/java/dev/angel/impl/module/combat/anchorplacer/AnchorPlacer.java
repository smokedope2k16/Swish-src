package dev.angel.impl.module.combat.anchorplacer;

import dev.angel.Swish;
import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.api.value.Value;
import dev.angel.impl.module.combat.anchorbreaker.AnchorBreaker;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class AnchorPlacer extends Module {

    protected final Value<Boolean> enableCharger = new Value<>(
            new String[]{"EnableBreaker", "AutoBreaker", "EnableAutobreak"},
            "Enables AnchorBreaker after disabling this",
            false
    );

    public AnchorPlacer() {
        super("AnchorPlacer", new String[]{"AnchorPlacer", "AutoPlacer", "AutoPlace"}, "Automatically places anchors", Category.COMBAT);
        this.offerValues(enableCharger);
    }

    @Override
    public void onEnable() {
        if (mc.player != null && mc.interactionManager != null && mc.world != null) {
            int anchorSlot = ItemUtil.findHotbarItem(Items.RESPAWN_ANCHOR);
            HitResult hit = mc.crosshairTarget;
            if (hit == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) {
                return;
            }
            if (anchorSlot != -1) {
                InventoryUtil.swap(anchorSlot);
                if (ItemUtil.isHolding(Items.RESPAWN_ANCHOR)) {
                    BlockHitResult blockHit = (BlockHitResult) hit;
                    if (mc.world.getBlockState(blockHit.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)) {
                        disable();
                        return;
                    }
                    BlockHitResult blockResult = new BlockHitResult(hit.getPos(), blockHit.getSide(), blockHit.getBlockPos(), false);
                    ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockResult);
                    if (result == ActionResult.SUCCESS) {
                        mc.player.swingHand(Hand.MAIN_HAND);
                        disable();
                    }
                }
            } else {
                disable();
            }
        }
    }

    @Override
    public void onDisable() {
        if (enableCharger.getValue() && !Swish.getModuleManager().get(AnchorBreaker.class).isEnabled()) {
            Swish.getModuleManager().get(AnchorBreaker.class).enable();
            Swish.getModuleManager().get(AnchorBreaker.class).clear();
        }
    }
}
