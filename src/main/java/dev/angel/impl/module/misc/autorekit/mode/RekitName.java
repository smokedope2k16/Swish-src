package dev.angel.impl.module.misc.autorekit.mode;

public enum RekitName {
    KIT("kit"),
    K("k"),
    LOAD("kit load ");

    final String kitName;
    RekitName(String kitName) {
        this.kitName = kitName;
    }

    public String getKitName() {
        return kitName;
    }
}