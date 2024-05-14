package dev.angel.impl.module.misc.autorekit;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.math.StopWatch;
import dev.angel.api.value.EnumValue;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.StringValue;
import dev.angel.impl.module.misc.autorekit.mode.RekitName;

public class AutoRekit extends Module {

    protected final EnumValue<RekitName> mode = new EnumValue<>(
            new String[]{"KitName", "mode"},
            "Kit: - /kit<number> | K: - /k<number> | Load: - /kit load <name>",
            RekitName.K
    );

    protected final NumberValue<Integer> kit = new NumberValue<>(
            new String[]{"KitNumber", "Number", "kit"},
            "The number of the kit -> [/kit1 | /k4]",
            1, 1, 9
    );

    protected final NumberValue<Integer> totemCount = new NumberValue<>(
            new String[]{"TotemCount", "TotemC", "Count"},
            "How many totems we have to have left to rekit",
            0, 0, 12
    );

    protected final StringValue kitName = new StringValue(
            new String[]{"LoadName", "name", "kit"},
            "lelgangfunds"
    );

    protected final StopWatch sentTimer = new StopWatch();

    public AutoRekit() {
        super("AutoRekit", new String[]{"AutoRekit"}, "Automatically sends a rekit message if we have no totems", Category.MISC);
        this.offerValues(mode, kit, totemCount, kitName);
        this.offerListeners(new ListenerTick(this));
    }

    @Override
    public void onEnable() {
        sentTimer.reset();
    }
}
