package dev.angel.api.util.inventory;

import dev.angel.api.interfaces.Minecraftable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;

import java.util.function.Predicate;

@SuppressWarnings("ConstantConditions")
public class ItemUtil implements Minecraftable {

    public static int getInventoryItemSlot(Item item) {
        int slot = -1;
        for (int i = 45; i > 0; --i) {
            if (!mc.player.getInventory().getStack(i).getItem().equals(item)) {
                continue;
            }
            slot = i;
            break;
        }
        return slot;
    }

    public static int getHotbarItemSlot(Item item) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (!mc.player.getInventory().getStack(i).getItem().equals(item)) {
                continue;
            }
            slot = i;
            break;
        }
        return slot;
    }

    public static boolean isHolding(Item item) {
        return isHolding(mc.player, item);
    }

    public static boolean isHolding(PlayerEntity entity, Item item) {
        if (entity != null) {
            ItemStack mainHand = entity.getMainHandStack();
            ItemStack offHand = entity.getOffHandStack();
            return ItemUtil.areSame(mainHand, item) || ItemUtil.areSame(offHand, item);
        }
        return false;
    }

    public static int findHotbarItem(Item item) {
        return findInHotbar(s -> ItemUtil.areSame(s, item));
    }

    public static int findInHotbar(Predicate<ItemStack> condition) {
        return findInHotbar(condition, true);
    }

    public static int findInHotbar(Predicate<ItemStack> condition, boolean offhand) {
        if (offhand && condition.test(mc.player.getOffHandStack())) {
            return -2;
        }
        int result = -1;
        for (int i = 8; i > -1; i--) {
            if (condition.test(mc.player.getInventory().getStack(i))) {
                result = i;
                if (mc.player.getInventory().selectedSlot == i) {
                    break;
                }
            }
        }

        return result;
    }

    public static boolean areSame(Item item1, Item item2) {
        return Item.getRawId(item1) == Item.getRawId(item2);
    }

    public static boolean areSame(ItemStack stack, Item item) {
        return stack != null && areSame(stack.getItem(), item);
    }

    public static boolean isTool(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ToolItem && !(itemStack.getItem() instanceof HoeItem)) {
            ToolMaterial material = ((ToolItem) itemStack.getItem()).getMaterial();
            return material != ToolMaterials.DIAMOND && material != ToolMaterials.NETHERITE;
        } else {
            return true;
        }
    }

    public static double getDamageInPercent(ItemStack stack) {
        double percent = (double) stack.getDamage() / (double) stack.getMaxDamage();
        if (percent == 0.0) {
            return 100.0;
        } else if (percent == 1.0) {
            return 0.0;
        }

        return 100 - (percent * 100);
    }
}
