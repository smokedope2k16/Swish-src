package dev.angel.impl.module.movement.noaccel;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.api.value.Value;
import dev.angel.impl.events.MoveEvent;
import net.minecraft.util.math.Vec3d;

public class NoAccel extends Module {

    protected final Value<Boolean> air = new Value<>(
            new String[]{"InAir", "air", "airjamin"},
            "Removes acceleration while in the air",
            true
    );

    protected final Value<Boolean> water = new Value<>(
            new String[]{"InWater", "water", "ifidontknowyouyougetxedout"},
            "Removes acceleration while in water",
            false
    );

    public NoAccel() {
        super("NoAccel", new String[]{"noaccel", "noacceleration", "instant"}, "Makes u not accel", Category.MOVEMENT);
        this.offerValues(air, water);
        this.offerListeners(new ListenerMove(this));
    }

    protected void strafe(MoveEvent event, double speed) {
        if (EntityUtil.isMoving()) {
            double[] strafe = strafe(speed);
            event.setVec(new Vec3d(strafe[0], event.getVec().y, strafe[1]));
        }
        else {
            event.setVec(new Vec3d(0.0, event.getVec().y, 0.0));
        }
    }

    private double[] strafe(double speed) {
        assert mc.player != null;
        float moveForward = mc.player.forwardSpeed;
        float moveStrafe  = mc.player.sidewaysSpeed;
        float rotationYaw = mc.player.prevYaw + (mc.player.getYaw() - mc.player.prevYaw) * mc.getRenderTime();

        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }

        double posX = moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));

        return new double[] {posX, posZ};
    }

}
