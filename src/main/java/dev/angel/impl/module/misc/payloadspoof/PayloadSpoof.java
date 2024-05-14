package dev.angel.impl.module.misc.payloadspoof;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;

public class PayloadSpoof extends Module {
    public PayloadSpoof() {
        super("PayloadSpoof", new String[]{"PayloadSpoof", "ServerSpoof", "NoHandShake"}, "Sends a fake payload when joining a server", Category.MISC);
        this.offerListeners(new ListenerCustomPayload(this));
    }
}
