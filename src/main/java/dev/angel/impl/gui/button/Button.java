package dev.angel.impl.gui.button;

import dev.angel.api.interfaces.Labeled;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.util.color.ColorUtil;
import dev.angel.api.util.render.RenderMethods;
import dev.angel.impl.gui.item.Item;
import dev.angel.impl.module.other.clickgui.ClickGUI;
import dev.angel.impl.module.other.colours.Colours;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.awt.*;

public class Button extends Item implements Labeled, Minecraftable {
    private boolean state;

    public Button(String label) {
        super(label);
        height = 15;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderMethods.drawRect(
                context,
                x,
                y,
                x + width,
                y + height,
                getState() ? ClickGUI.get().getColor().getRGB() : ClickGUI.get().getColorOff().getRGB());

        if (isHovering(mouseX, mouseY)) {
            if (getState()) {
                RenderMethods.drawRect(
                        context,
                        x,
                        y,
                        x + width,
                        y + height,
                        ColorUtil.changeAlpha(Color.BLACK, 30).getRGB());
            } else {
                RenderMethods.drawRect(
                        context,
                        x,
                        y,
                        x + width,
                        y + height,
                        Colours.get().getColourCustomAlpha(30).getRGB());
            }
        }

        context.drawTextWithShadow(
                renderer,
                getLabel(),
                (int) (x + 2.0f),
                (int) (y + 4.0f),
                0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isHovering(mouseX, mouseY)) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 0.3F));
            state = !state;
            toggle();
        }

        return true;
    }

    public void toggle() {}

    public boolean getState() {
        return state;
    }

    @Override
    public float getHeight() {
        return 14;
    }

}