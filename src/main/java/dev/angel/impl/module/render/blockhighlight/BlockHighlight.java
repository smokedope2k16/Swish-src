package dev.angel.impl.module.render.blockhighlight;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.ColorValue;
import dev.angel.api.value.Value;

import java.awt.*;

public class BlockHighlight extends Module {

    public final Value<Boolean> box = new Value<>(
            new String[]{"Box", "b"},
            "Draws a box",
            false
    );

    public final ColorValue color = new ColorValue(
            new String[]{"BoxColor", "BoxColour", "BoxClolor", "BoxColr"},
            new Color(255, 255, 255, 40),
            false
    );

    public final ColorValue outlineColor = new ColorValue(
            new String[]{"OutlineColor", "OutlineColour", "OutlineClolor", "OutlineColr"},
            new Color(255, 255, 255, 170),
            false
    );

    public BlockHighlight() {
        super("BlockHighlight", new String[]{"blockhighlight", "highlight"}, "Highlights the block you're looking at", Category.RENDER);
        this.offerValues(box, outlineColor, color);
        this.offerListeners(new ListenerRender(this));
    }
}
