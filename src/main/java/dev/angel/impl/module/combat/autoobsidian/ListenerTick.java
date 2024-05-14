package dev.angel.impl.module.combat.autoobsidian;

import dev.angel.api.event.bus.Listener;
import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.block.BlockUtil;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.impl.events.TickEvent;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;

public class ListenerTick extends ModuleListener<AutoObsidian, TickEvent> {
    public ListenerTick(AutoObsidian module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (module.timer.passed(100) && mc.interactionManager != null && mc.player != null) {
            if (module.swapToCrystal.getValue()) {
                int crystalSlot = ItemUtil.findHotbarItem(Items.END_CRYSTAL);
                if (crystalSlot != -1) {
                    InventoryUtil.swap(crystalSlot);
                }
                if (mc.crosshairTarget instanceof BlockHitResult hit) {
                    if (module.placeCrystal.getValue()) {
                        BlockPos block = hit.getBlockPos();
                        if (BlockUtil.canPlaceCrystal(block) && ItemUtil.isHolding(Items.END_CRYSTAL)) {
                            ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                            if (result == ActionResult.SUCCESS) {
                                mc.player.swingHand(Hand.MAIN_HAND);
                            }
                        }
                    }
                }
                if (mc.crosshairTarget instanceof EntityHitResult hit) {
                    if (hit.getEntity() instanceof EndCrystalEntity && module.breakCrystal.getValue()) {
                        mc.interactionManager.attackEntity(mc.player, hit.getEntity());
                        mc.player.swingHand(Hand.MAIN_HAND);
                    }
                }
            }
            module.disable();
        }
    }
}
