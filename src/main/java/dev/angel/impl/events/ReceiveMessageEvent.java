package dev.angel.impl.events;


import dev.angel.api.event.events.Event;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;

public class ReceiveMessageEvent extends Event {

    private Text message;
    private final MessageIndicator indicator;
    private boolean modified;
    private final int id;

    public ReceiveMessageEvent(Text message, MessageIndicator indicator, int id) {
        this.message = message;
        this.indicator = indicator;
        this.modified = false;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isModified() {
        return modified;
    }

    public MessageIndicator getIndicator() {
        return indicator;
    }

    public Text getMessage() {
        return message;
    }

    public void setMessage(Text message) {
        this.message = message;
        this.modified = true;
    }
}