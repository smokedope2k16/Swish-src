package dev.angel.impl.gui.button.value;


import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.util.color.ColorUtil;
import dev.angel.api.util.math.RoundingUtil;
import dev.angel.api.util.render.RenderMethods;
import dev.angel.api.util.text.TextColor;
import dev.angel.api.value.NumberValue;
import dev.angel.impl.gui.Panel;
import dev.angel.impl.gui.SwishGui;
import dev.angel.impl.gui.item.Item;
import dev.angel.impl.module.other.clickgui.ClickGUI;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class NumberButton extends Item implements Minecraftable {
    private final NumberValue<Number> numberValue;
    private final Number min;
    private final Number max;
    private final int difference;

    private boolean dragging;

    public NumberButton(NumberValue<Number> numberValue) {
        super(numberValue.getLabel());
        this.numberValue = numberValue;
        this.min = numberValue.getMinimum();
        this.max = numberValue.getMaximum();
        this.difference = max.intValue() - min.intValue();
        setValue(numberValue);
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
                numberValue.getValue().floatValue() <= min.floatValue() ? x : x + (width + 6.9F) * partialMultiplier(),
                y + height - 0.5f,
                ClickGUI.get().getColor().getRGB());

        if (isHovering(mouseX, mouseY)) {
            RenderMethods.drawRect(
                    context,
                    x,
                    y,
                    x + (width + 6.9F) * partialMultiplier(),
                    y + height - 0.5f,
                    ColorUtil.changeAlpha(Color.BLACK, 30).getRGB());
        }

        if (dragging) {
            setSettingFromX(mouseX);
        }

        String value = String.format("%.2f", numberValue.getValue().floatValue());
        context.drawTextWithShadow(
                mc.textRenderer,
                String.format("%s:%s %s",
                        getLabel(),
                        TextColor.WHITE,
                        value),
                (int) (x + 2.3F),
                (int) (y + 4.0F),
                0xFFFFFFFF);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovering(mouseX, mouseY) && button == 0) {
            dragging = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void setSettingFromX(int mouseX) {
        float percent = (mouseX - x) / (width + 6.9F);
        if (numberValue.getValue() instanceof Double) {
            double result = (Double) numberValue.getMinimum() + (difference * percent);
            numberValue.setValue(MathHelper.clamp(RoundingUtil.roundDouble(RoundingUtil.roundToStep(result, (double) numberValue.getSteps()), 2), (double) numberValue.getMinimum(), (double) numberValue.getMaximum()));
        } else if (numberValue.getValue() instanceof Float) {
            float result = (Float) numberValue.getMinimum() + (difference * percent);
            numberValue.setValue(MathHelper.clamp(RoundingUtil.roundFloat(RoundingUtil.roundToStep(result, (float) numberValue.getSteps()), 2), (float) numberValue.getMinimum(), (float) numberValue.getMaximum()));
        } else if (numberValue.getValue() instanceof Integer) {
            numberValue.setValue(((Integer) numberValue.getMinimum() + (int)(difference * percent)));
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int releaseButton) {
        dragging = false;
    }

    @Override
    public float getHeight() {
        return 14;
    }

    private float middle() {
        return max.floatValue() - min.floatValue();
    }

    private float part() {
        return numberValue.getValue().floatValue() - min.floatValue();
    }

    private float partialMultiplier() {
        return part() / middle();
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