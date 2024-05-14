package dev.angel.impl.module.render.norender;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.Value;

public class NoRender extends Module {

    private final Value<Boolean> noPumpkinOverlay = new Value<>(
            new String[]{"PumpkinOverlay", "NoPumpkin", "pumpkin"},
            "Stops rendering pumpkin overlay",
            true
    );

    private final Value<Boolean> noFireOverlay = new Value<>(
            new String[]{"FireOverlay", "NoFire", "Fire"},
            "Stops rendering fire overlay",
            true
    );

    private final Value<Boolean> noTotemOverlay = new Value<>(
            new String[]{"TotemOverlay", "NoTotem", "Totem"},
            "Stops rendering totem pop overlay",
            false
    );

    private final Value<Boolean> noFog = new Value<>(
            new String[]{"Fog", "Fag"},
            "Stops rendering fog",
            true
    );

    public NoRender() {
        super("NoRender", new String[]{"NoRender", "AntiRender"}, "Stops rendering certain features", Category.RENDER);
        this.offerValues(noPumpkinOverlay, noFireOverlay, noTotemOverlay, noFog);
    }

    public boolean noPumpkins() {
        return isEnabled() && noPumpkinOverlay.getValue();
    }

    public boolean noFire() {
        return isEnabled() && noFireOverlay.getValue();
    }

    public boolean noTotems() {
        return isEnabled() && noTotemOverlay.getValue();
    }

    public boolean noFog() {
        return isEnabled() && noFog.getValue();
    }
}
