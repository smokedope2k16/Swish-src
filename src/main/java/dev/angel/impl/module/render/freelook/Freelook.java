package dev.angel.impl.module.render.freelook;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.keyboard.KeyPressAction;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import dev.angel.asm.ducks.ICamera;
import dev.angel.impl.events.KeyPressEvent;
import net.minecraft.client.option.Perspective;

public class Freelook extends Module {

    private final Value<Boolean> holdToggle = new Value<>(
            new String[]{"HoldToggle", "Hold", "HoldtoToggle"},
            "Uses holding keybind instead of normal keybinds",
            true
    );

    private final NumberValue<Float> verticalSpeed = new NumberValue<>(
            new String[]{"VerticalSpeed", "Vertical"},
            "How fast to move the camera vertically",
            0.15f, 0.1f, 1.0f, 0.001f
    );

    private final NumberValue<Float> horizontalSpeed = new NumberValue<>(
            new String[]{"HorizontalSpeed", "Horizontal"},
            "How fast to move the camera horizontally",
            0.15f, 0.1f, 1.0f, 0.001f
    );

    public Freelook() {
        super("Freelook", new String[]{"Freelook"}, "Look freely in 3rd person", Category.RENDER);
        this.offerValues(holdToggle, verticalSpeed, horizontalSpeed);
        this.offerListeners(new ListenerTick(this), new ListenerGui(this));
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            ICamera cam = (ICamera) mc.player;
            cam.setRotations(mc.player.getPitch(), mc.player.getYaw());
            mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
        }
    }

    @Override
    public void onDisable() {
        mc.options.setPerspective(Perspective.FIRST_PERSON);
    }

    public void handleHold(KeyPressEvent event) {
        if (holdToggle.getValue()) {
            if (event.getAction() == KeyPressAction.PRESS) {
                enable();
                event.setCanceled(true);
            } else if (event.getAction() == KeyPressAction.RELEASE) {
                disable();
                event.setCanceled(true);
            }
        }
    }

    public float getVertical() {
        return verticalSpeed.getValue();
    }

    public float getHorizontal() {
        return horizontalSpeed.getValue();
    }
}
