package dev.angel.impl.module.movement.elytrafly;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.network.PacketUtil;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class ElytraFly extends Module {

    protected final Value<Boolean> changePitch = new Value<>(
            new String[]{"ChangePitch", "", "tickspeed"},
            "Change pitch ? factoid",
            true
    );

    protected final NumberValue<Float> pitchValue  = new NumberValue<>(
            new String[]{"PitchValue", "timerspeed"},
            "Value of pitxch",
            4.0f, 1.1f, 5.0f, 0.1f
    );

    protected final Value<Boolean> autoWalk  = new Value<>(
            new String[]{"AutoWalk", "tickshift", "tickspeed"},
            "Walks automatically",
            true
    );

    protected final Value<Boolean> autoJump  = new Value<>(
            new String[]{"AutoJump", "tickshift", "tickspeed"},
            "Jumps automatically",
            true
    );

    protected final Value<Boolean> allowBroken  = new Value<>(
            new String[]{"AllotBroken", "tickshift", "tickspeed"},
            "no fukkin clue",
            true
    );

    public ElytraFly() {
        super("ElytraFly", new String[]{"elytrafly", "efly"}, "Lets u fly with elytra", Category.MOVEMENT);
        this.offerValues(changePitch, pitchValue, autoWalk, autoJump, allowBroken);
        this.offerListeners(new UpdateListener(this));
    }


    protected boolean castElytra() {
        assert mc.player != null;
        if (checkElytra() && check()) {
            PacketUtil.send(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            return true;
        }
        return false;
    }

    protected boolean checkElytra() {
        assert mc.player != null;
        if (mc.player.input.jumping && !mc.player.getAbilities().flying && !mc.player.hasVehicle() && !mc.player.isClimbing()) {
            ItemStack is = mc.player.getEquippedStack(EquipmentSlot.CHEST);
            return is.isOf(Items.ELYTRA) && (ElytraItem.isUsable(is) || allowBroken.getValue());
        }
        return false;
    }

    private boolean check() {
        assert mc.player != null;
        if (!mc.player.isTouchingWater() && !mc.player.hasStatusEffect(StatusEffects.LEVITATION)) {
            ItemStack is = mc.player.getEquippedStack(EquipmentSlot.CHEST);
            if (is.isOf(Items.ELYTRA) && (ElytraItem.isUsable(is) || allowBroken.getValue()) ) {
                mc.player.startFallFlying();
                return true;
            }
        }
        return false;
    }

}
