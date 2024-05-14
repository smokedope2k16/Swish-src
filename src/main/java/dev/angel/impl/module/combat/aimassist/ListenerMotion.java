package dev.angel.impl.module.combat.aimassist;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.api.util.math.AngleUtil;
import dev.angel.api.util.math.MathUtil;
import dev.angel.impl.events.MotionUpdateEvent;
import dev.angel.impl.module.combat.aimassist.mode.MouseButton;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3d;

@SuppressWarnings("ConstantConditions")
public class ListenerMotion extends ModuleListener<AimAssist, MotionUpdateEvent> {
    public ListenerMotion(AimAssist module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (mc.world == null || mc.player == null) {
            return;
        }

        module.target = EntityUtil.getClosestEnemy();
        HitResult hit = mc.crosshairTarget;

        if (module.target != null) {
            if (AngleUtil.isAiming(
                    EntityUtil.getRotations(module.target)[0],
                    EntityUtil.getRotations(module.target)[1],
                    module.fov.getValue())
                    && !(module.target.squaredDistanceTo(mc.player) > MathUtil.square(module.targetRange.getValue()))) {

                if (hit != null && hit.getType() == HitResult.Type.ENTITY && !module.crosshair.getValue()) {

                    final Entity crosshairTarget = ((EntityHitResult) hit).getEntity();
                    if (crosshairTarget == module.target) {
                        return;
                    }
                }

                if (module.onlyWeapon.getValue() && !ItemUtil.isTool(mc.player.getMainHandStack())) {
                    if (!mc.player.canSee(module.target) || module.target.isInvisible()) {
                        return;
                    }

                    boolean key = module.button.getValue() != MouseButton.NONE;
                    switch (module.button.getValue()) {
                        case LEFT -> key = mc.options.attackKey.isPressed();
                        case RIGHT -> key = mc.options.useKey.isPressed();
                        case NONE -> key = true;
                    }

                    if (key) {
                        aim(module.target, module.speed.getValue());
                    }
                }
            }
        }
    }

    private void aim(Entity target, double delta) {
        Vector3d vec = new Vector3d();
        EntityUtil.set(vec, target, delta);

        switch (module.bodyPart.getValue()) {
            case HEAD -> vec.add(0, target.getEyeHeight(target.getPose()), 0);
            case BODY -> vec.add(0, target.getEyeHeight(target.getPose()) / 2, 0);
        }

        double deltaX = vec.x - mc.player.getX();
        double deltaZ = vec.z - mc.player.getZ();
        double deltaY = vec.y - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));

        double angle = Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90;
        double deltaAngle;
        double toRotate;

        if (module.horizontal.getValue()) {
            deltaAngle = MathHelper.wrapDegrees(angle - mc.player.getYaw());
            toRotate = module.horizontalSpeed.getValue() * (deltaAngle >= 0 ? 1 : -1) * delta;
            if ((toRotate >= 0 && toRotate > deltaAngle) || (toRotate < 0 && toRotate < deltaAngle)) {
                toRotate = deltaAngle;
            }
            mc.player.setYaw(mc.player.getYaw() + (float) toRotate);
        }

        double tick = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        angle = -Math.toDegrees(Math.atan2(deltaY, tick));

        if (module.vertical.getValue()) {
            deltaAngle = MathHelper.wrapDegrees(angle - mc.player.getPitch());
            toRotate = module.verticalSpeed.getValue() * (deltaAngle >= 0 ? 1 : -1) * delta;
            if ((toRotate >= 0 && toRotate > deltaAngle) || (toRotate < 0 && toRotate < deltaAngle)) {
                toRotate = deltaAngle;
            }
            mc.player.setPitch(mc.player.getPitch() + (float) toRotate);
        }
    }
}
