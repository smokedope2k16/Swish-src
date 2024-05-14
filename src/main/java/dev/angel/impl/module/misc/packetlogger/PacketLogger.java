package dev.angel.impl.module.misc.packetlogger;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.Value;

public class PacketLogger extends Module {

    protected final Value<Boolean> filter = new Value<>(new String[]{"Filter", "fil"}, false);
    protected final Value<Boolean> blockpacket = new Value<>(
            new String[]{"them blocks from the jungle"},
            true
    );

    public PacketLogger() {
        super("PacketLogger", new String[]{"PacketLogger", "packetlog", "packetprinter", "logger", "loggatron"}, "Prints packets in chat", Category.MISC);
        this.offerValues(filter, blockpacket);
        this.offerListeners(new  ListenerSend(this));
    }
}
