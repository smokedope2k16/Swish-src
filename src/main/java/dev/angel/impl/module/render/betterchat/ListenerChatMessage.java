package dev.angel.impl.module.render.betterchat;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.ReceiveMessageEvent;
import net.minecraft.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListenerChatMessage extends ModuleListener<BetterChat, ReceiveMessageEvent> {
    public ListenerChatMessage(BetterChat module) {
        super(module, ReceiveMessageEvent.class);
    }

    @Override
    public void call(ReceiveMessageEvent event) {
        Text message = event.getMessage();
        if (module.timestamps.getValue()) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm");
            Text timestamp = Text.literal("%s<%s%s%s> ".formatted(
                    module.bracketColor.getValue(),
                    module.timeColor.getValue(),
                    simpleDateFormat.format(new Date()),
                    module.bracketColor.getValue()));
            message = Text.empty().append(timestamp).append(message);
        }

        event.setMessage(message);
    }
}
