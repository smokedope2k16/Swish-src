package dev.angel.impl.module.player.fastbreak;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.api.util.math.StopWatch;
import dev.angel.api.util.network.PacketUtil;
import dev.angel.api.value.ColorValue;
import dev.angel.api.value.NumberValue;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.awt.*;

@SuppressWarnings("ConstantConditions")
public class FastBreak extends Module {

    protected final NumberValue<Float> range = new NumberValue<>(
            new String[]{"Range", "rang", "distance", "dist"},
            "How far we have to be from the block to cancel breaking it",
            4.5F, 1F, 6F, 0.1F
    );

    protected final NumberValue<Integer> delay = new NumberValue<>(
            new String[]{"Delay", "d"},
            "Delay between mining attempts",
            3, 1, 50
    );

    protected final NumberValue<Integer> startDelay = new NumberValue<>(
            new String[]{"StartDelay", "sd"},
            "Delay that simulates us breaking the block",
            5, 1, 50
    );

    protected final ColorValue color = new ColorValue(new String[]{"Color", "colour", "col"},new Color(-1), false);

    protected BlockPos pos;
    protected Direction direction;

    protected boolean abortPacket;

    protected StopWatch timer = new StopWatch();
    protected StopWatch mineTimer = new StopWatch();
    protected StopWatch startTimer = new StopWatch();

    public FastBreak() {
        super("FastBreak", new String[]{"fastbreak", "speedmine"}, "Breaks blocks faster", Category.PLAYER);
        this.offerValues(range, delay, startDelay, color);
        this.offerListeners(new ListenerDamageBlock(this), new ListenerRender(this), new ListenerUpdate(this), new ListenerBlockChange(this));
    }

    @Override
    public void onEnable() {
        reset();
    }

    @Override
    public void onDisable() {
        reset();
    }

    protected void abortCurrentPos() {
        PacketUtil.swing();
        PacketUtil.send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, pos, direction));
        PacketUtil.swing();
        reset();
    }

    protected void reset() {
        pos = null;
        direction = null;
        timer.reset();
        mineTimer.reset();
        startTimer.reset();
    }

    protected void tryMine(int slot) {
        int oldSlot = mc.player.getInventory().selectedSlot;

        PacketUtil.swing();
        InventoryUtil.swap(slot);

        PacketUtil.swing();
        PacketUtil.send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos, direction));
        PacketUtil.swing();
        PacketUtil.swing();
        PacketUtil.send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, pos, direction));
        PacketUtil.swing();
        PacketUtil.swing();

        InventoryUtil.swap(oldSlot);
    }

    protected int findPickaxeSlot() {
        int pick = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof PickaxeItem) {
                pick = i;
            }
        }

        return pick;
    }
}
