package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.api.util.keyboard.KeyPressAction;
import dev.angel.impl.events.KeyPressEvent;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        if (key != GLFW.GLFW_KEY_UNKNOWN) {
            KeyPressEvent keyPressEvent = new KeyPressEvent(key, KeyPressAction.get(action));
            Swish.getEventBus().dispatch(keyPressEvent);
            if (keyPressEvent.isCanceled()) info.cancel();
            //Logger.getLogger().log("mmm me press keyjaminm");
        }
    }
}
