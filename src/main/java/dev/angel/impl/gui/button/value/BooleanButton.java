package dev.angel.impl.gui.button.value;

import dev.angel.api.util.color.ColorUtil;
import dev.angel.api.util.render.RenderMethods;
import dev.angel.api.value.Value;
import dev.angel.impl.gui.Panel;
import dev.angel.impl.gui.SwishGui;
import dev.angel.impl.gui.button.Button;
import dev.angel.impl.module.other.clickgui.ClickGUI;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class BooleanButton extends Button {

    private final Value<Boolean> booleanValue;

    public BooleanButton(Value<Boolean> value) {
        super(value.getLabel());
        this.booleanValue = value;
        this.x = getX() + 1f;
        setValue(booleanValue);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

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
                getState() ? ClickGUI.get().getColor().getRGB() : 290805077);

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

        context.drawTextWithShadow(
                mc.textRenderer,
                getLabel(),
                (int) (x + 2.3F),
                (int) (y + 4.0F),
                0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void toggle() {
        booleanValue.setValue(!booleanValue.getValue());
    }

    @Override
    public boolean getState() {
        return booleanValue.getValue();
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