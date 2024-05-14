package dev.angel.api.util.text;

public class StringUtil {

    public static String capitalise(String str) {
        if (isNullOrEmpty(str)) {
            return "";
        }

        return Character.toUpperCase(str.charAt(0)) + (str.length() != 1 ? str.substring(1).toLowerCase() : "");
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
