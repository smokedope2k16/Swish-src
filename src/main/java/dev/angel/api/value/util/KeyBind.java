package dev.angel.api.value.util;

import net.fabricmc.loader.impl.util.StringUtil;

import static org.lwjgl.glfw.GLFW.*;

public class KeyBind {
    private final int key;

    public KeyBind(int value) {
        key = value;
    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return getKeyString(key);
    }

    public static String getKeyString(int key) {
        return switch (key) {
            case GLFW_KEY_UNKNOWN -> "None";
            case GLFW_KEY_ESCAPE -> "Esc";
            case GLFW_KEY_GRAVE_ACCENT -> "Grave Accent";
            case GLFW_KEY_WORLD_1 -> "World 1";
            case GLFW_KEY_WORLD_2 -> "World 2";
            case GLFW_KEY_PRINT_SCREEN -> "Print Screen";
            case GLFW_KEY_PAUSE -> "Pause";
            case GLFW_KEY_INSERT -> "Insert";
            case GLFW_KEY_HOME -> "Home";
            case GLFW_KEY_PAGE_UP -> "Page Up";
            case GLFW_KEY_PAGE_DOWN -> "Page Down";
            case GLFW_KEY_END -> "End";
            case GLFW_KEY_TAB -> "Tab";
            case GLFW_KEY_LEFT_CONTROL -> "Left Control";
            case GLFW_KEY_RIGHT_CONTROL -> "Right Control";
            case GLFW_KEY_LEFT_ALT -> "Left Alt";
            case GLFW_KEY_RIGHT_ALT -> "Right Alt";
            case GLFW_KEY_LEFT_SHIFT -> "Left Shift";
            case GLFW_KEY_RIGHT_SHIFT -> "Right Shift";
            case GLFW_KEY_UP -> "Arrow Up";
            case GLFW_KEY_DOWN -> "Arrow Down";
            case GLFW_KEY_LEFT -> "Arrow Left";
            case GLFW_KEY_RIGHT -> "Arrow Right";
            case GLFW_KEY_APOSTROPHE -> "Apostrophe";
            case GLFW_KEY_BACKSPACE -> "Backspace";
            case GLFW_KEY_CAPS_LOCK -> "Caps Lock";
            case GLFW_KEY_MENU -> "Menu";
            case GLFW_KEY_LEFT_SUPER -> "Left Super";
            case GLFW_KEY_RIGHT_SUPER -> "Right Super";
            case GLFW_KEY_ENTER -> "Enter";
            case GLFW_KEY_KP_ENTER -> "Numpad Enter";
            case GLFW_KEY_NUM_LOCK -> "Num Lock";
            case GLFW_KEY_SPACE -> "Space";
            case GLFW_KEY_F1 -> "F1";
            case GLFW_KEY_F2 -> "F2";
            case GLFW_KEY_F3 -> "F3";
            case GLFW_KEY_F4 -> "F4";
            case GLFW_KEY_F5 -> "F5";
            case GLFW_KEY_F6 -> "F6";
            case GLFW_KEY_F7 -> "F7";
            case GLFW_KEY_F8 -> "F8";
            case GLFW_KEY_F9 -> "F9";
            case GLFW_KEY_F10 -> "F10";
            case GLFW_KEY_F11 -> "F11";
            case GLFW_KEY_F12 -> "F12";
            case GLFW_KEY_F13 -> "F13";
            case GLFW_KEY_F14 -> "F14";
            case GLFW_KEY_F15 -> "F15";
            case GLFW_KEY_F16 -> "F16";
            case GLFW_KEY_F17 -> "F17";
            case GLFW_KEY_F18 -> "F18";
            case GLFW_KEY_F19 -> "F19";
            case GLFW_KEY_F20 -> "F20";
            case GLFW_KEY_F21 -> "F21";
            case GLFW_KEY_F22 -> "F22";
            case GLFW_KEY_F23 -> "F23";
            case GLFW_KEY_F24 -> "F24";
            case GLFW_KEY_F25 -> "F25";
            default -> {
                String keyName = glfwGetKeyName(key, 0);
                yield keyName == null ? "None" : StringUtil.capitalize(keyName);
            }
        };
    }

    //TODO: ADD MORE BINDS
    public static int getKeyIndex(String s) {
        int bind = 0;
        switch (s.toUpperCase()) {
            case "NONE" -> bind = GLFW_KEY_UNKNOWN;
            case "GRAVEACCENT", "GRAVE ACCENT" -> bind = GLFW_KEY_GRAVE_ACCENT;
            case "WORLD1", "WORLD 1" -> bind = GLFW_KEY_WORLD_1;// dude what key even is this LMFAOO
            case "WORLD2", "WORLD 2" -> bind = GLFW_KEY_WORLD_2;
            case "PRINTSCREEN", "PRINT SCREEN" -> bind = GLFW_KEY_PRINT_SCREEN;
            case "RIGHTSHIFT", "RIGHT SHIFT" -> bind = GLFW_KEY_RIGHT_SHIFT;
            case "RIGHTCONTROL" ,"RIGHT CONTROL" -> bind = GLFW_KEY_RIGHT_CONTROL;
            case "NUMLOCK", "NUM LOCK" -> bind = GLFW_KEY_NUM_LOCK;
            case "LEFT ALT", "LEFTALT" -> bind = GLFW_KEY_LEFT_ALT;
            case "SPACE" -> bind = GLFW_KEY_SPACE;
            case "A" -> bind = GLFW_KEY_A;
            case "B" -> bind = GLFW_KEY_B;
            case "C" -> bind = GLFW_KEY_C;
            case "D" -> bind = GLFW_KEY_D;
            case "E" -> bind = GLFW_KEY_E;
            case "F" -> bind = GLFW_KEY_F;
            case "G" -> bind = GLFW_KEY_G;
            case "H" -> bind = GLFW_KEY_H;
            case "I" -> bind = GLFW_KEY_I;
            case "J" -> bind = GLFW_KEY_J;
            case "K" -> bind = GLFW_KEY_K;
            case "L" -> bind = GLFW_KEY_L;
            case "M" -> bind = GLFW_KEY_M;
            case "N" -> bind = GLFW_KEY_N;
            case "O" -> bind = GLFW_KEY_O;
            case "P" -> bind = GLFW_KEY_P;
            case "Q" -> bind = GLFW_KEY_Q;
            case "R" -> bind = GLFW_KEY_R;
            case "S" -> bind = GLFW_KEY_S;
            case "T" -> bind = GLFW_KEY_T;
            case "U" -> bind = GLFW_KEY_U;
            case "V" -> bind = GLFW_KEY_V;
            case "W" -> bind = GLFW_KEY_W;
            case "X" -> bind = GLFW_KEY_X;
            case "Y" -> bind = GLFW_KEY_Y;
            case "Z" -> bind = GLFW_KEY_Z;
            case "1" -> bind = GLFW_KEY_1;
            case "2" -> bind = GLFW_KEY_2;
            case "3" -> bind = GLFW_KEY_3;
            case "4" -> bind = GLFW_KEY_4;
            case "5" -> bind = GLFW_KEY_5;
            case "6" -> bind = GLFW_KEY_6;
            case "7" -> bind = GLFW_KEY_7;
            case "8" -> bind = GLFW_KEY_8;
            case "9" -> bind = GLFW_KEY_9;
            case "0" -> bind = GLFW_KEY_0;
        }
        return bind;
    }
}
