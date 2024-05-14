package dev.angel.impl.gui.button.value;

import dev.angel.api.util.color.ColorUtil;
import dev.angel.api.util.render.RenderMethods;
import dev.angel.api.util.text.TextColor;
import dev.angel.api.value.EnumValue;
import dev.angel.impl.gui.Panel;
import dev.angel.impl.gui.SwishGui;
import dev.angel.impl.gui.button.Button;
import dev.angel.impl.module.other.clickgui.ClickGUI;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.awt.*;

public class EnumButton extends Button {

    private final EnumValue<?> value;

    public EnumButton(EnumValue<?> value) {
        super(value.getLabel());
        this.value = value;
        this.x = getX() + 1f;
        setValue(value);
    }

    @Override
    public void render(DrawContext context, final int mouseX, final int mouseY, final float delta) {

        RenderMethods.drawRect(
                context,
                x - 1.0f,
                y,
                x,
                y + height - 0.5f,
                ColorUtil.changeAlpha(ClickGUI.get().getColor(), 255).getRGB()
        );

        RenderMethods.drawRect(
                context,
                x,
                y,
                x + width + 6.9f,
                y + height - 0.5f,
                getState() ? ClickGUI.get().getColor().getRGB() : ClickGUI.get().getColorOff().getRGB());

        if (isHovering(mouseX, mouseY)) {
            if (getState()) {
                RenderMethods.drawRect(
                        context,
                        x,
                        y,
                        x + width + 6.9f,
                        y + height - 0.5f,
                        ColorUtil.changeAlpha(Color.BLACK, 30).getRGB());
            } else {
                RenderMethods.drawRect(
                        context,
                        x,
                        y,
                        x + width + 6.9f,
                        y + height - 0.5f,
                        ColorUtil.changeAlpha(Color.WHITE, 30).getRGB());
            }
        }

        context.drawTextWithShadow(renderer, String.format("%s:%s %s",
                        getLabel(),
                        TextColor.WHITE,
                        value.getFixedValue()),
                (int) (x + 2.3F), (int) (y + 4.0F), 0xFFFFFFFF);
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovering(mouseX, mouseY)) {
            if (button == 0) {
                value.increment();
            } else if (button == 1) {
                mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 0.3F));
                value.decrement();
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public float getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
    }

    @Override
    public boolean getState() {
        return true;
    }

    @Override
    public boolean isHovering(double mouseX, double mouseY) {
        for (Panel panel : SwishGui.getClickGui().getPanels()) {
            if (!panel.drag) continue;
            return false;
        }
        return mouseX >= getX() && mouseX <= getX() + (width + 6.9F) && mouseY >= getY() && mouseY <= getY() + height;
    }
}