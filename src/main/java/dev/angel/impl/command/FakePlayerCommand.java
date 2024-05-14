package dev.angel.impl.command;

import com.mojang.authlib.GameProfile;
import dev.angel.api.command.Command;
import dev.angel.api.util.logging.Logger;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;

import java.util.UUID;

public class FakePlayerCommand extends Command {
    public static OtherClientPlayerEntity fakePlayer;
    public static boolean executed;

    public FakePlayerCommand() {
        super(new String[]{"FakePlayer", "Fk"});
    }

    @Override
    public void run(String[] args) {
        if (mc.world != null && mc.player != null) {
            if (!executed) {
                GameProfile profile = new GameProfile(UUID.randomUUID(), "wassupington");
                fakePlayer = new OtherClientPlayerEntity(mc.world, profile);
                fakePlayer.copyPositionAndRotation(mc.player);
                fakePlayer.setHealth(20F);
                fakePlayer.setOnGround(mc.player.isOnGround());
                mc.world.addEntity(-2147483647, fakePlayer);
                Logger.getLogger().log("FakePlayer added");
                executed = true;
            } else {
                mc.world.removeEntity(fakePlayer.getId(), Entity.RemovalReason.KILLED);
                Logger.getLogger().log("FakePlayer removed");
                fakePlayer = null;
                executed = false;
            }
        }
    }
}

