package dev.angel.impl.module.render.betterchat;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.text.TextColor;
import dev.angel.api.value.ColorValue;
import dev.angel.api.value.EnumValue;
import dev.angel.api.value.Value;
import net.minecraft.util.Formatting;

import java.awt.*;

// TODO: timestamps & player heads
public class BetterChat extends Module {

    protected final Value<Boolean> infinite = new Value<>(
            new String[]{"Infinite", "Inf"},
            "Removes minecraft chatmessage limit",
            true
    );

    protected final Value<Boolean> timestamps = new Value<>(
            new String[]{"Timestamps", "ChatTimeStamps", "Time"},
            "Puts timestamps in chat before messages",
            false
    );

    protected final EnumValue<Formatting> bracketColor = new EnumValue<>(
            new String[]{"BracketColor", "BracketColour", "brackcolor"},
            "Color of the bracket [<]",
            Formatting.DARK_PURPLE
    );


    protected final EnumValue<Formatting> timeColor = new EnumValue<>(
            new String[]{"TimeColor", "TimeColour", "timcolor"},
            "Color of the bracket [10:00]",
            Formatting.LIGHT_PURPLE
    );

    private static BetterChat get;

    public BetterChat() {
        super("BetterChat", new String[]{"BetterChat", "Chat", "ChatTweaks"}, "Modifies minecraft chatbox", Category.RENDER);
        this.offerValues(infinite, timestamps, bracketColor, timeColor);
        this.offerListeners(new ListenerChatMessage(this));
    }

    public static BetterChat get() {
        return get == null ? (get = new BetterChat()) : get;
    }

    public boolean infiniteChatMessage() {
        return isEnabled() && infinite.getValue();
    }
}
