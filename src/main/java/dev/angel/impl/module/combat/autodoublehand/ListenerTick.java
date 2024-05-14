package dev.angel.impl.module.combat.autodoublehand;

import dev.angel.Swish;
import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.api.util.math.MathUtil;
import dev.angel.impl.events.TickEvent;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListenerTick extends ModuleListener<AutoDoubleHand, TickEvent> {
    public ListenerTick(AutoDoubleHand module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {// maybe change this crystqal chceking thing into safety manager
        if (mc.world != null && mc.player != null) {

            List<EndCrystalEntity> crystals = getNearByCrystals();
            ArrayList<Vec3d> crystalsPos = new ArrayList<>();
            if (crystals != null) {
                crystals.forEach(e -> crystalsPos.add(e.getPos()));
            }

            if (EntityUtil.getClosestEnemy() == null) {
                return;
            }
            if (EntityUtil.getClosestEnemy().distanceTo(mc.player) > MathUtil.square(module.range.getValue())) {
                return;
            }

            if (module.chance.getValue() != 100.0 && !ItemUtil.isHolding(Items.TOTEM_OF_UNDYING)) {
                Random random = new Random();
                int chance = random.nextInt(100);
                if (chance >= module.chance.getValue() / 100.0) {
                    return;
                }
            }

            if (EntityUtil.explosion != null && EntityUtil.raycastContext != null) {
                for (Vec3d pos : crystalsPos) {
                    double damage = EntityUtil.crystalDamage(mc.player, pos, true, null, false);
                    if (damage >= EntityUtil.getHealth(mc.player) && !ItemUtil.isHolding(Items.TOTEM_OF_UNDYING)) {
                        int totemSlot = ItemUtil.getHotbarItemSlot(Items.TOTEM_OF_UNDYING);
                        if (totemSlot != -1) {
                            InventoryUtil.swap(totemSlot);
                        }
                    }
                }
            }
        }
    }

    private List<EndCrystalEntity> getNearByCrystals() {
        if (mc.player != null && mc.world != null) {
            Vec3d pos = mc.player.getPos();
            return mc.world.getEntitiesByClass(EndCrystalEntity.class, new Box(pos.add(-6, -6, -6), pos.add(6, 6, 6)), a -> true);
        }
        return null;
    }
}
