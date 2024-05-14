package dev.angel.impl.module.render.esp;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.ColorValue;
import dev.angel.api.value.Value;

import java.awt.*;

public class ESP extends Module {

    protected final Value<Boolean> items = new Value<>(
            new String[]{"Items", "ItemEsp"},
            "Draws esp on items",
            true
    );

    protected final Value<Boolean> pearls = new Value<>(
            new String[]{"Pearls", "pearl"},
            "Draws esp on pearls",
            true
    );

    protected final Value<Boolean> player = new Value<>(
            new String[]{"Players", "player"},
            "Draws esp on players",
            false
    );

    public final ColorValue color = new ColorValue(
            new String[]{"Color", "colour", "clolor", "colr"},
            new Color(255, 255, 255, 255),
            false
    );

    public ESP() {
        super("ESP", new String[]{"ESP", "PlayerEsp", "ItemEsp"}, "Draws boxes on entities", Category.RENDER);
        this.offerValues(items, pearls, player, color);
        this.offerListeners(new ListenerRender(this));
    }
}
