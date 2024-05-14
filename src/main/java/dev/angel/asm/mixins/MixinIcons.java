package dev.angel.asm.mixins;

import dev.angel.api.util.render.IconUtil;
import net.minecraft.client.util.Icons;
import net.minecraft.resource.InputSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;
import java.util.List;

@Mixin(Icons.class)
public abstract class MixinIcons {

    @Inject(method = "getIcons", at = @At(value = "HEAD"), cancellable = true)
    private void getIconsHook(CallbackInfoReturnable<List<InputSupplier<InputStream>>> info) {
        info.setReturnValue(IconUtil.getAllPngResources());
    }

    //FUCK MAC
    /*
    @Inject(method = "getMacIcon", at = @At(value = "HEAD"), cancellable = true)
    private void getMacIconHook(CallbackInfoReturnable<InputSupplier<InputStream>> info) {
        info.setReturnValue(IconUtil.getResource("minecraft.icns"));
    }
    */
}
