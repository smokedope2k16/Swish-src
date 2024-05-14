package dev.angel.impl.gui.button.value;

import dev.angel.api.util.color.ColorUtil;
import dev.angel.api.util.render.DotUtil;
import dev.angel.api.util.render.RenderMethods;
import dev.angel.api.util.text.TextColor;
import dev.angel.api.value.Value;
import dev.angel.api.value.util.KeyBind;
import dev.angel.impl.gui.Panel;
import dev.angel.impl.gui.SwishGui;
import dev.angel.impl.gui.button.Button;
import dev.angel.impl.module.other.clickgui.ClickGUI;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class BindButton extends Button {

    private boolean listening;

    private final Value<KeyBind> bindValue;

    public BindButton(Value<KeyBind> bindValue) {
        super(bindValue.getLabel());
        this.bindValue = bindValue;
        this.x = getX() + 1f;
        setValue(this.bindValue);
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

        final String string = listening ? DotUtil.getDots() : bindValue.getValue().getName();
        context.drawTextWithShadow(
                renderer,
                getLabel() + ": " + TextColor.WHITE + string,
                (int) (x + 2.3F),
                (int) (y + 4.0F),
                0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onKeyTyped(int keyCode, int scanCode, int modifiers) {
        if (listening) {
            KeyBind bind = new KeyBind(keyCode);
            if (bind.getName().equalsIgnoreCase("Esc")) {
                return;
            }
            if (bind.getName().equalsIgnoreCase("Delete")) {
                bind = new KeyBind(-1);
            }
            bindValue.setValue(bind);
            toggle();
        }
    }

    @Override
    public void toggle() {
        listening = !listening;
    }

    @Override
    public boolean getState() {
        return !listening;
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