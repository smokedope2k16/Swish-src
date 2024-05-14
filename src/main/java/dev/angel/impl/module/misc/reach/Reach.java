package dev.angel.impl.module.misc.reach;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.NumberValue;

public class Reach extends Module {

    protected final NumberValue<Float> blockReach = new NumberValue<>(
            new String[]{"BlocksAdd", "BlockReachAdd", "Block"},
            "How much reach to add to block reach",
            0.5f, 0.0f, 3.0f, 0.1f
    );

    protected final NumberValue<Float> playerReach = new NumberValue<>(
            new String[]{"PlayerReach", "PlayerReachAdd", "Player"},
            "How much reach to add to player reach",
            0.5f, 0.0f, 3.0f, 0.1f
    );

    protected final NumberValue<Float> hitboxExtend = new NumberValue<>(
            new String[]{"HitboxExtend", "HitboxReach", "Expand"},
            "How much to expand the hitbox of entities",
            0.2f, 0.0f, 3.0f, 0.1f
    );

    public Reach() {
        super("Reach", new String[]{"Reach", "HitBoxExtend", "ReachDistance"}, "Extends reach & modifies hitboxes", Category.MISC);
        this.offerValues(blockReach, playerReach, hitboxExtend);
    }

    public float getHitboxExtend() {
        if (isEnabled()) {
            return hitboxExtend.getValue();
        }
        return 0;
    }

    public float getBlockDistance() {
        float[] defaults = new float[]{5.0f, 4.5f};
        if (mc.interactionManager != null) {
            if (!isEnabled()) {
                return mc.interactionManager.getCurrentGameMode().isCreative() ? defaults[0] : defaults[1];
            }
            return mc.interactionManager.getCurrentGameMode().isCreative() ? defaults[0] + blockReach.getValue() : defaults[1] + blockReach.getValue();
        }
        return 0;
    }

    public float getPlayerDistance() {
        if (!isEnabled()) {
            return 3;
        }
        return 3 + playerReach.getValue();
    }
}
