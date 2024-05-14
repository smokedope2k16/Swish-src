package dev.angel.api.util.entity;

import dev.angel.Swish;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.util.identifier.SwishIdentifier;
import dev.angel.impl.module.other.hud.HUD;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class CapeUtil implements Minecraftable {
    private static final String[] CHACHOOX_UUIDS = {
            "6886dd45-3773-4d9f-8b06-ae0cf9b237ff",
            //"c1a2424d-e3e2-40e3-b981-954b9977cc4a",
            "91dd06a5-229c-4c0f-9f83-39857afb71cd",
            //"842f5a25-b957-4896-8a0d-1e903bb07498",
            "cf539dff-9a6a-4abe-9f78-98a1d89313c0"
    };

    private static final String[] MONEYMAKER_UUIDS =
            new String[]{
                    "d303ae13-79c6-438f-8c90-806e808b4044",
                    "1bc857e5-1471-466a-a038-1216c76ed318",
                    "0554301e-026c-4504-9d91-91e628da9ff1",
                    "d83976e1-1d89-45ef-972b-1a8703e812f8",
                    "6625e3db-ac14-4037-9bc9-861f7848c702", //nigga done stealin my accs/ my badington
                    "c1a2424d-e3e2-40e3-b981-954b9977cc4a",//theflagisraied
                    "842f5a25-b957-4896-8a0d-1e903bb07498"//redlight
            };

    private static final String[] CRYSTALPVPNN_UUIDS = {
            "cecb9bd2-ec7c-44ef-9288-313dd1b7c083",
            "b1907c3f-0bc0-46e3-ba73-102e0925cfd7"
    };

    private static final String[] CPV_UUIDS = {
            "75aa6787-f7de-456b-9074-8dce5a611100",
            "b8e7bc40-0982-477b-8368-6b619963eb04"
    };

    private static final Identifier CHACHOOX_CAPE = new SwishIdentifier("capes/chachooxcape.png");
    private static final Identifier MONEYMAKER_CAPE = new SwishIdentifier("capes/moneymakercape.png");
    private static final Identifier CRYSTALPVPNN_CAPE = new SwishIdentifier("capes/crystalpvpnncape.png");
    private static final Identifier CPV_CAPE = new SwishIdentifier("capes/cpvcape.png");

    public static Identifier getCape(UUID id) {
        String stringId = id.toString();
        for (String string : CHACHOOX_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return CHACHOOX_CAPE;
            }
        }
        for (String string : MONEYMAKER_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return MONEYMAKER_CAPE;
            }
        }
        for (String string : CRYSTALPVPNN_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return CRYSTALPVPNN_CAPE;
            }
        }
        for (String string : CPV_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return CPV_CAPE;
            }
        }
        return null;
    }

    public static boolean isCapesEnabled() {
        return Swish.getModuleManager().get(HUD.class).cape();
    }
}
