package dev.angel.asm.mixins;

import com.mojang.authlib.GameProfile;
import dev.angel.asm.ducks.IChatHudLineVisible;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChatHudLine.Visible.class)
public class MixinChatHudLineVisible implements IChatHudLineVisible {
    @Shadow
    @Final
    private OrderedText content;
    
    @Unique
    private int id;
    
    @Unique 
    private GameProfile sender;
    
    @Unique private boolean startOfEntry;

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();

        content.accept((index, style, codePoint) -> {
            sb.appendCodePoint(codePoint);
            return true;
        });

        return sb.toString();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public GameProfile getSender() {
        return sender;
    }

    @Override
    public void setSender(GameProfile profile) {
        sender = profile;
    }

    @Override
    public boolean isStartOfEntry() {
        return startOfEntry;
    }

    @Override
    public void setStartOfEntry(boolean start) {
        startOfEntry = start;
    }
}
