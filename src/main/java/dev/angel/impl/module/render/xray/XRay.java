package dev.angel.impl.module.render.xray;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;

//TODO: blocklist and softreload
public class XRay extends Module {
    public XRay() {
        super("XRay", new String[]{"Xray", "ray"}, "Allows us to see blocks through walls", Category.RENDER);
        this.offerListeners(new ListenerTesselator(this), new ListenerRenderBlockEntity(this), new ListenerRenderBlockSide(this),
                new ListenerCubeOpacity(this), new ListenerAmbientOcclusion(this));
    }

    @Override
    public void onEnable() {
        mc.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        mc.worldRenderer.reload();
    }
}
