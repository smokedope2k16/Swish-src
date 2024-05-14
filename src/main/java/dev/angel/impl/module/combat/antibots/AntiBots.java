package dev.angel.impl.module.combat.antibots;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.Value;

public class AntiBots extends Module {

    protected final Value<Boolean> gamemode = new Value<>(
            new String[]{"Gamemode", "mode"},
            "Removes players without a gamemode",
            true
    );

    protected final Value<Boolean> uuid = new Value<>(
            new String[]{"UUID", "Id", "Api"},
            "Removes players without a UUID",
            true
    );

    protected final Value<Boolean> name = new Value<>(
            new String[]{"Name", "nameington", "profile"},
            "Removes players without a name", true
    );

    protected final Value<Boolean> ping = new Value<>(
            new String[]{"Ping", "Latency", "Connection"},
            "Removes players with ping that is below 0",
            true
    );

    public AntiBots() {
        super("AntiBots", new String[]{"AntiBots", "AntiBot", "NoBots"}, "Removes players that might be bots", Category.COMBAT);
        this.offerValues(gamemode, uuid, name, ping);
        this.offerListeners(new ListenerTick(this));
    }
}
