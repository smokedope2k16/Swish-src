package dev.angel.asm.mixins;

import dev.angel.Swish;
import dev.angel.impl.events.Render2DEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.SubtitlesHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SubtitlesHud.class)
public abstract class MixinSubtitlesHud {

    @Inject(method = "render", at = @At(value = "HEAD"))
    public void renderHook(DrawContext context, CallbackInfo info) {
        Render2DEvent event = new Render2DEvent(context);
        Swish.getEventBus().dispatch(event);
    }

}
