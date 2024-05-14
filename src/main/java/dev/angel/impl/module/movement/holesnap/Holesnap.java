package dev.angel.impl.module.movement.holesnap;

import dev.angel.Swish;
import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.block.HoleUtil;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.api.util.logging.Logger;
import dev.angel.api.util.math.RotationsUtil;
import dev.angel.api.util.text.TextColor;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import dev.angel.impl.events.MoveEvent;
import dev.angel.impl.module.movement.step.Step;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

import static dev.angel.api.util.block.HoleUtil.Hole;

public class Holesnap extends Module {

    // TODO post timer for bypassing

    protected final NumberValue<Float> range = new NumberValue<>(
            new String[]{"Range", "distance"},
            "Maximum distance to hole.",
            4.0f, 1.0f, 6.0f, 0.5f
    );

    protected final Value<Boolean> step = new Value<>(
            new String[]{"Step", "spider"},
            "Enable step when moving.",
            true
    );

    protected final Value<Boolean> timer = new Value<>(
            new String[]{"Timer", "tickshift", "tickspeed"},
            "Speeds up timer",
            true
    );

    protected final NumberValue<Float> timerAmount = new NumberValue<>(
            new String[]{"TimerAmount", "timerspeed"},
            "Timer speed",
            4.0f, 1.1f, 5.0f, 0.1f
    );

    protected final NumberValue<Integer> timerLength = new NumberValue<>(
            new String[]{"TimerLength", "ticks"},
            "The amount of time for which timer is sped up",
            25, 10, 100
    );

    protected final Value<Boolean> postTimer = new Value<>(
            new String[]{"PostTimer", "tickshift", "tickspeed"},
            "Speeds up timer post",
            true
    );


    protected Hole hole;
    protected int stuck;
    protected int boosted;

    public Holesnap() {
        super("Holesnap", new String[]{"holesnap", "snappy", "holepull", "superanchor"}, "Pulls you towards the nearest hole", Category.MOVEMENT);
        this.offerValues(range, step, timer, timerAmount, timerLength, postTimer);
        this.offerListeners(new MoveListener(this), new TickListener(this));
    }

    @Override
    public void onEnable() {
        if (step.getValue()) {
            Swish.getModuleManager().get(Step.class).enableNoMessage();
        }
        hole = getTarget(range.getValue());
        if (hole == null) {
            Logger.getLogger().log(TextColor.RED + "<HolePull> Couldn't find a hole.", 45088);
            disable();
        }
    }

    @Override
    public void onDisable() {
        if (step.getValue()) {
            Swish.getModuleManager().get(Step.class).disableNoMessage();
        }
        Swish.getTimerManager().reset();
        stuck = 0;
        boosted = 0;
        hole = null;
    }

    protected void snap(MoveEvent event) {
        assert mc.player != null;
        Vec3d playerPos = mc.player.getPos();
        Vec3d targetPos = HoleUtil.getCenter(hole);

        double yawRad = Math.toRadians(RotationsUtil.getRotationTo(playerPos, targetPos).x);
        double dist = playerPos.distanceTo(targetPos);
        double speed = mc.player.isOnGround() ? -Math.min(0.2805, dist / 2.0) : -EntityUtil.getDefaultMoveSpeed() + 0.02;

        if (dist < 0.1) {
            event.setVec(new Vec3d(0.0, event.getVec().y, 0.0));
            return;
        }
        event.setVec(new Vec3d(
                -Math.sin(yawRad) * speed,
                event.getVec().y,
                Math.cos(yawRad) * speed));
    }

    protected boolean isSafe() {
        assert mc.player != null;
        double dist = mc.player.getPos().distanceTo(HoleUtil.getCenter(hole));
        return HoleUtil.isInHole(mc.player) || dist < 0.1;
    }

    private Hole getTarget(float range) {
        assert mc.player != null;
        return HoleUtil.getHoles(range, true)
                .stream()
                .filter(hole -> mc.player
                        .getPos()
                        .distanceTo(new Vec3d(
                                (double) hole.pos1.getX() + 0.5,
                                mc.player.getY(),
                                (double) hole.pos1.getZ() + 0.5)) <= range).min(
                        Comparator.comparingDouble(hole -> mc.player.getPos().distanceTo(new Vec3d(
                                (double) hole.pos1.getX() + 0.5,
                                mc.player.getY(),
                                (double) hole.pos1.getZ() + 0.5)))).orElse(null);
    }
}
