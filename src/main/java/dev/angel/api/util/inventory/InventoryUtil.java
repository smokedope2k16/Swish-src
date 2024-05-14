package dev.angel.api.util.inventory;

import dev.angel.api.interfaces.Minecraftable;
import dev.angel.asm.ducks.IClientPlayerInteractionManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

@SuppressWarnings("ConstantConditions")
public class InventoryUtil implements Minecraftable {

    public static void moveToOffhand(int slot) {
        int returnSlot = 0;
        if (slot == -1) {
            return;
        }

        click(slot < 9 ? slot + 36 : slot);
        click(45);

        for (int i = 0; i < 45; ++i) {
            if (!mc.player.getInventory().getStack(i).isEmpty()) {
                continue;
            }
            returnSlot = i;
            break;
        }

        click(returnSlot < 9 ? returnSlot + 36 : returnSlot);
    }

    public static void moveItem(int slot, int slotOut) {
        click(slot < 9 ? slot + 36 : slot);
        click(slotOut < 9 ? slotOut + 36 : slotOut);
        click(slot < 9 ? slot + 36 : slot);
    }

    public static void sync() {
        ((IClientPlayerInteractionManager) mc.interactionManager).syncItem();
    }

    public static void swap(int slot) {
        if (mc.player.getInventory().selectedSlot != slot && slot > -1 && slot < 9) {
            mc.player.getInventory().selectedSlot = slot;
            sync();
        }
    }

    public static void click(int slot) {
        mc.interactionManager.clickSlot(0, slot, 0, SlotActionType.PICKUP, mc.player);
    }

    public static int count(Item item) {
        int count = 0;
        int size;

        size = mc.player.getInventory().main.size();
        for (int i = 0; i < size; i++) {
            final ItemStack itemStack = mc.player.getInventory().main.get(i);
            if (itemStack.getItem() == item) {
                count += itemStack.getCount();
            }
        }

        ItemStack offhandStack = mc.player.getOffHandStack();
        if (offhandStack.getItem() == item) {
            count += offhandStack.getCount();
        }

        return count;
    }
}
