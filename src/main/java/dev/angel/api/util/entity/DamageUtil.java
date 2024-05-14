package dev.angel.api.util.entity;

import dev.angel.api.interfaces.Minecraftable;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;

public class DamageUtil implements Minecraftable {

    public static boolean canBreakWeakness(boolean checkStack) {

        StatusEffect effect = mc.player.getStatusEffect(StatusEffects.STRENGTH).getEffectType();
        if (mc.player.getStatusEffect(StatusEffects.WEAKNESS) == null) {
            return true;
        }

        int strengthAmp = 0;

        if (effect != null) {
            strengthAmp = mc.player.getStatusEffect(StatusEffects.STRENGTH).getAmplifier();
        }

        if (strengthAmp >= 1) {
            return true;
        }

        return checkStack && canBreakWeakness(mc.player.getMainHandStack().getItem().getDefaultStack());
    }

    public static boolean canBreakWeakness(ItemStack stack) {
        if (stack.getItem() instanceof SwordItem) {
            return true;
        }

       /* if (stack.getItem() instanceof ToolItem toolItem) {
            IItemTool tool = (IItemTool) stack.getItem();
            return toolItem.() > 4.0f;
        } */

        return false;
    }

    public static int findAntiWeakness() {
        int slot = -1;
        for (int i = 8; i > -1; i--) {
            if (canBreakWeakness(mc.player.getInventory().getStack(i))) {
                slot = i;
                if (mc.player.getInventory().selectedSlot == i) {
                    break;
                }
            }
        }

        return slot;
    }
}
