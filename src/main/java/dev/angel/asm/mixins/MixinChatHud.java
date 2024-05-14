package dev.angel.asm.mixins;

import com.google.common.collect.Lists;
import dev.angel.Swish;
import dev.angel.asm.ducks.IChatHud;
import dev.angel.asm.ducks.IChatHudLine;
import dev.angel.impl.events.ReceiveMessageEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@SuppressWarnings({"SameParameterValue", "ConstantConditions"})
@Mixin(ChatHud.class)
public abstract class MixinChatHud implements IChatHud {
    @Shadow
    private final List<ChatHudLine.Visible> visibleMessages = Lists.newArrayList();

    @Shadow
    @Final
    private List<ChatHudLine> messages;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void addMessage(Text message, @Nullable MessageSignatureData signature, int ticks, @Nullable MessageIndicator indicator, boolean refresh);

    @Unique
    private boolean skipOnAddMessage;

    @Unique
    private int nextId;

    @Override
    public void swishMessage(Text message, int id) {
        nextId = id;
        addMessage(message, null, this.client.inGameHud.getTicks(), new MessageIndicator(Formatting.DARK_PURPLE.getColorValue(), null, null, null), false);
        nextId = 0;
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void addMessageAfterNewChatHudLineVisibleHook(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo info) {
        ((IChatHudLine) (Object) visibleMessages.get(0)).setId(nextId);
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void addMessageAfterNewChatHudLineHook(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo info) {
        ((IChatHudLine) (Object) messages.get(0)).setId(nextId);
    }

    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", cancellable = true)
    private void addMessageHook(Text message, @Nullable MessageSignatureData signature, int ticks, @Nullable MessageIndicator indicator, boolean refresh, CallbackInfo info) {
        if (skipOnAddMessage) {
            return;
        }

        ReceiveMessageEvent receiveMessageEvent = new ReceiveMessageEvent(message, indicator, nextId);
        Swish.getEventBus().dispatch(receiveMessageEvent);

        if (receiveMessageEvent.isCanceled()) {
            info.cancel();
        } else {
            visibleMessages.removeIf(msg -> ((IChatHudLine) (Object) msg).getId() == nextId && nextId != 0);

            for (int i = messages.size() - 1; i > -1; i--) {
                if (((IChatHudLine) (Object) messages.get(i)).getId() == nextId && nextId != 0) {
                    messages.remove(i);
                }
            }
        }

        if (receiveMessageEvent.isModified()) {
            info.cancel();
            skipOnAddMessage = true;
            addMessage(receiveMessageEvent.getMessage(), signature, ticks, receiveMessageEvent.getIndicator(), refresh);
            skipOnAddMessage = false;
        }
    }
}
