package dev.angel.asm.mixins;

import dev.angel.impl.module.render.betterchat.BetterChat;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen {
    @Shadow
    protected TextFieldWidget chatField;

    @Inject(method = "init", at = @At(value = "TAIL"))
    private void onInit(CallbackInfo info) {
        if (BetterChat.get().infiniteChatMessage()) {
            chatField.setMaxLength(Integer.MAX_VALUE);
        }
    }
}