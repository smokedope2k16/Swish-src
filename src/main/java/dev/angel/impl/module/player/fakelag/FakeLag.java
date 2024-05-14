package dev.angel.impl.module.player.fakelag;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.network.PacketUtil;
import net.minecraft.network.packet.Packet;

import java.util.ArrayList;

public class FakeLag extends Module {
    protected final ArrayList<Packet<?>> cache = new ArrayList<>();
    public FakeLag() {
        super("FakeLag", new String[]{"FakeLag", "BeanerNet", "Lagjamin"}, "Simulates lag server side", Category.PLAYER);
        this.offerListeners(new ListenerPacket(this));
    }

    @Override
    public void onEnable() {
        cache.clear();
    }

    @Override
    public void onDisable() {
        cache.forEach(packet -> {
            if (packet != null) {
                PacketUtil.send(packet);
            }
        });
        cache.clear();
    }
}
