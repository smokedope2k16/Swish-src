package dev.angel.asm.mixins;

import com.mojang.authlib.GameProfile;
import dev.angel.asm.ducks.IChatHudLine;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChatHudLine.class)
public class MixinChatHudLine implements IChatHudLine {
    @Shadow
    @Final
    private Text content;

    @Unique
    private int id;

    @Unique private GameProfile sender;

    @Override
    public String getText() {
        return content.getString();
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
}