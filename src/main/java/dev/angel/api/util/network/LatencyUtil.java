package dev.angel.api.util.network;

import dev.angel.api.interfaces.Minecraftable;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;

public class LatencyUtil implements Minecraftable {

    public static int getPing() {
        if (mc.getNetworkHandler() == null || mc.player == null) {
            return 0;
        }

        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
        return playerListEntry != null ? playerListEntry.getLatency() : 0;
    }

    public static int getPing(Entity player) {
        if (mc.getNetworkHandler() == null || player == null) {
            return 0;
        }

        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        return playerListEntry != null ? playerListEntry.getLatency() : 0;
    }
}
