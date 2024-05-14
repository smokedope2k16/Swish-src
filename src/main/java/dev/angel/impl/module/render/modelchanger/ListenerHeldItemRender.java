package dev.angel.impl.module.render.modelchanger;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.HeldItemRenderEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;

public class ListenerHeldItemRender extends ModuleListener<ModelChanger, HeldItemRenderEvent> {
    public ListenerHeldItemRender(ModelChanger module) {
        super(module, HeldItemRenderEvent.class);
    }

    @Override
    public void call(HeldItemRenderEvent event) {
        final MatrixStack matrix = event.getMatrix();

        matrix.scale(module.scaleX.getValue(), module.scaleY.getValue(), module.scaleZ.getValue());

        if (event.getHand() == Hand.MAIN_HAND) {
            matrix.translate(module.translateX.getValue(), module.translateY.getValue(), module.translateZ.getValue());
        } else {
            matrix.translate(-module.translateX.getValue(), module.translateY.getValue(), module.translateZ.getValue());
        }
    }
}
