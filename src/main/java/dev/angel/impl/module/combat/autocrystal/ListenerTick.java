package dev.angel.impl.module.combat.autocrystal;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.block.BlockUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.impl.events.TickEvent;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;

import java.util.concurrent.ThreadLocalRandom;

public class ListenerTick extends ModuleListener<AutoCrystal, TickEvent> {
    public ListenerTick(AutoCrystal module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        boolean holding = !module.onlyCrystal.getValue() || ItemUtil.isHolding(Items.END_CRYSTAL);
        boolean click = !module.rightClickOnly.getValue() || mc.options.useKey.isPressed();
        boolean killed = !module.antiLootBlowup.getValue() || module.deathTimer.passed(ThreadLocalRandom.current().nextInt(800, 1500));
        if (mc.player != null && mc.interactionManager != null && holding && click && killed) {
            if (mc.currentScreen instanceof InventoryScreen) {
                return;
            }
            if (module.multiTask.getValue()) {
                if (mc.player.isUsingItem() && !ItemUtil.isHolding(Items.END_CRYSTAL)) {
                    return;
                }
            }
            int placeDelay = module.placeDelay.getValue();
            switch (module.randomize.getValue()) {
                case NONE -> placeDelay = placeDelay * 25;
                case NORMAL -> placeDelay = module.placeDelay.getValue() == 0 ? 0 : ThreadLocalRandom.current().nextInt(placeDelay * 25);
                case EXTRA -> placeDelay = module.placeDelay.getValue() == 0 ? 0 : ThreadLocalRandom.current().nextInt(placeDelay) * 25;
            }
            if (mc.crosshairTarget instanceof BlockHitResult hit) { // place crystal
                if (module.autoPlace.getValue() && module.placeTimer.passed(placeDelay)) {
                    final BlockPos block = hit.getBlockPos();
                    if (BlockUtil.canPlaceCrystal(block)) {
                        if (module.onlyCrystal.getValue() && !ItemUtil.isHolding(Items.END_CRYSTAL)) {
                            return;
                        }
                        final ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                        if (result == ActionResult.SUCCESS) {
                            mc.player.swingHand(Hand.MAIN_HAND);
                            module.placeTimer.reset();
                        }
                    }
                }
            }
            if (mc.crosshairTarget instanceof EntityHitResult hit) { // destroy crystal
                int breakDelay = module.placeDelay.getValue();
                switch (module.randomize.getValue()) {
                    case NONE -> breakDelay = breakDelay * 25;
                    case NORMAL -> breakDelay = module.breakDelay.getValue() == 0 ? 0 : ThreadLocalRandom.current().nextInt(placeDelay * 25);
                    case EXTRA -> breakDelay = module.breakDelay.getValue() == 0 ? 0 : ThreadLocalRandom.current().nextInt(placeDelay) * 25;
                }
                if (hit.getEntity() instanceof EndCrystalEntity && module.autoBreak.getValue() && module.breakTimer.passed(breakDelay)) {
                    mc.interactionManager.attackEntity(mc.player, hit.getEntity());
                    mc.player.swingHand(Hand.MAIN_HAND);
                    module.breakTimer.reset();
                }
            }
        }
    }
}
