package dev.angel.api.util.render;

import dev.angel.api.util.math.StopWatch;

public class DotUtil {
    private static final StopWatch dotTimer = new StopWatch();

    private static String dots = "";

    public static String getDots() {
        if (dotTimer.passed(500)) {
            dots += ".";
            dotTimer.reset();
        }

        if (dots.length() > 3) {
            dots = "";
        }

        return dots;
    }
}