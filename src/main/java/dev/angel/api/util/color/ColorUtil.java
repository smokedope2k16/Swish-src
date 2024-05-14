package dev.angel.api.util.color;

import java.awt.*;

public class ColorUtil {
    public static Color changeAlpha(Color origColor, int alpha) {
        return new Color(origColor.getRed(), origColor.getGreen(), origColor.getBlue(), alpha);
    }
}
