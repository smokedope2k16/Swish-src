package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.impl.events.ScreenEvent;
import dev.angel.impl.events.TickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftClient.class, priority = 1001)
public abstract class MixinMinecraft {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initHook(CallbackInfo info) {
        Swish.init();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void preTickHook(CallbackInfo info) {
        TickEvent pre = new TickEvent();
        Swish.getEventBus().dispatch(pre);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void postTickHook(CallbackInfo info) {
        TickEvent post = new TickEvent.Post();
        Swish.getEventBus().dispatch(post);
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreenHook(Screen screen, CallbackInfo info) {
        ScreenEvent event = new ScreenEvent(screen);
        Swish.getEventBus().dispatch(event);

        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void closeHook(CallbackInfo info) {
        Swish.getConfigManager().saveConfig();
        Swish.getFriendManager().saveFriends();
    }
}
