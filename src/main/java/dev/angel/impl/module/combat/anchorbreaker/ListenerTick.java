package dev.angel.impl.module.combat.anchorbreaker;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.impl.events.TickEvent;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos ;

public class ListenerTick extends ModuleListener<AnchorBreaker, TickEvent> {
    public ListenerTick(AnchorBreaker module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.player != null && mc.interactionManager != null && mc.world != null) {
            int glowSlot = ItemUtil.findHotbarItem(Items.GLOWSTONE);
            int totemSlot = ItemUtil.findHotbarItem(Items.TOTEM_OF_UNDYING);
            if (glowSlot == -1 && module.autoCharge.getValue()) {
                module.disable();
            }
            HitResult hit = mc.crosshairTarget;
            if (hit == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) {
                return;
            }
            BlockHitResult blockHit = (BlockHitResult) hit;
            if (isAnchorBlock(blockHit.getBlockPos())) {
                if (isAnchorsCharged(blockHit.getBlockPos()) && module.autoCharge.getValue()) {
                    if (higherAnchorCharge(blockHit.getBlockPos(), module.maxCharges.getValue())) {
                        return;
                    }
                    if (!ItemUtil.isHolding(Items.GLOWSTONE)) {
                        InventoryUtil.swap(glowSlot);
                        module.chargeTimer.reset();
                        return;
                    }
                    if (module.chargeTimer.passed(module.chargeDelay.getValue() * 50.0)) {
                        ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHit);
                        if (result == ActionResult.SUCCESS) {
                            mc.player.swingHand(Hand.MAIN_HAND);
                            module.chargeTimer.reset();
                            module.breakTimer.reset();
                            return;
                        }
                    }
                }
                if (module.autoExplode.getValue()) {
                    if (isAnchorBlock(blockHit.getBlockPos())) {
                        if (mc.world.getBlockState(blockHit.getBlockPos()).get(RespawnAnchorBlock.CHARGES) == 0) {
                            return;
                        }
                        if (ItemUtil.isHolding(Items.GLOWSTONE) || ItemUtil.isHolding(Items.RESPAWN_ANCHOR)) {
                            InventoryUtil.swap(totemSlot);
                            module.breakTimer.reset();
                            return;
                        }
                        if (module.breakTimer.passed(module.explodeDelay.getValue() * 25.0)) {
                            InventoryUtil.swap(totemSlot);//  why this instant bro
                            ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHit);
                            if (result == ActionResult.SUCCESS) {
                                mc.player.swingHand(Hand.MAIN_HAND);
                                if (module.autoDisable.getValue()) {
                                    module.disable();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isAnchorsCharged(BlockPos pos) {
        if (mc.world != null) {
            return mc.world.getBlockState(pos).get(RespawnAnchorBlock.CHARGES) == 0;
        }
        return false;
    }

    private boolean higherAnchorCharge(BlockPos pos, int maxCharge) {
        try {
            if (mc.world != null) {
                return mc.world.getBlockState(pos).get(RespawnAnchorBlock.CHARGES) == maxCharge || mc.world.getBlockState(pos).get(RespawnAnchorBlock.CHARGES) > maxCharge;
            }
        } catch (Exception e) {
            // there is no way to fix this its minecraft crash im p sure
        }
        return false;
    }

    private boolean isAnchorBlock(BlockPos pos) {
        if (mc.world != null) {
            return mc.world.getBlockState(pos).isOf(Blocks.RESPAWN_ANCHOR);
        }
        return false;
    }
}
