package dev.angel.impl.manager;

import dev.angel.api.event.bus.Listener;
import dev.angel.api.event.bus.SubscriberImpl;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.impl.events.MotionUpdateEvent;

@SuppressWarnings("ConstantConditions")
public class RotationManager extends SubscriberImpl implements Minecraftable {
    private float yaw, pitch;
    private boolean rotated;
    private int ticksSinceNoRotate;

    public RotationManager() {
        this.listeners.add(new Listener<>(MotionUpdateEvent.class, Integer.MAX_VALUE) {
            @Override
            public void call(MotionUpdateEvent event) {
                switch (event.getStage()) {
                    case PRE -> {
                        yaw = mc.player.getYaw();
                        pitch = mc.player.getPitch();
                        //Logger.getLogger().log("pre rotation", false);
                    }
                    case POST -> {
                        ticksSinceNoRotate++;
                        if (ticksSinceNoRotate > 2) {
                            rotated = false;
                        }
                        mc.player.setYaw(yaw);
                        mc.player.setHeadYaw(yaw);
                        mc.player.setPitch(pitch);
                        //Logger.getLogger().log("post rotation", false);
                    }
                }
            }
        });
    }

    public void setRotations(float[] rotations) {
        setRotations(rotations[0], rotations[1]);
    }

    public void setRotations(float yaw, float pitch) {
        rotated = true;
        ticksSinceNoRotate = 0;
        mc.player.setYaw(yaw);
        mc.player.setHeadYaw(yaw);
        mc.player.setPitch(pitch);
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public boolean isRotated() {
        return rotated;
    }
}
