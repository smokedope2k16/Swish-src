package dev.angel.impl.module.render.fullbright;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.ColorValue;
import dev.angel.api.value.NumberValue;

import java.awt.*;
//TODO: ambience
public class Fullbright extends Module {
    private final ColorValue color = new ColorValue(
            new String[]{"Color", "lightcolor"},
            new Color(255, 255, 255, 255),
            false
    );

    public Fullbright() {
        super("Fullbright", new String[]{"Fullbright", "Gamma", "Bright"}, "Lightens up your game", Category.RENDER);
        this.offerValues(color);
    }

    public Color getColor() {
        return color.getColor();
    }
}