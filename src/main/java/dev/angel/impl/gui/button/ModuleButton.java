package dev.angel.impl.gui.button;

import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.module.Module;
import dev.angel.api.util.color.ColorUtil;
import dev.angel.api.util.render.RenderMethods;
import dev.angel.api.util.text.StringUtil;
import dev.angel.api.value.BindValue;
import dev.angel.api.value.EnumValue;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import dev.angel.impl.gui.button.value.BindButton;
import dev.angel.impl.gui.button.value.BooleanButton;
import dev.angel.impl.gui.button.value.EnumButton;
import dev.angel.impl.gui.button.value.NumberButton;
import dev.angel.impl.gui.item.Item;
import dev.angel.impl.module.other.clickgui.ClickGUI;
import dev.angel.impl.module.other.colours.Colours;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton extends Button implements Minecraftable {
    private final Module module;
    private final List<Item> items = new ArrayList<>();
    private boolean subOpen;

    @SuppressWarnings("unchecked")
    public ModuleButton(Module module) {
        super(module.getLabel());
        this.module = module;
        if (!module.getValues().isEmpty()) {
            for (Value<?> properties : module.getValues()) {
                if (properties.getValue() instanceof Boolean) {
                    items.add(new BooleanButton((Value<Boolean>) properties));
                }
                if (properties instanceof EnumValue) {
                    items.add(new EnumButton((EnumValue<?>) properties));
                }
                if (properties instanceof NumberValue) {
                    items.add(new NumberButton((NumberValue<Number>) properties));
                }
                if (properties instanceof BindValue && !properties.getLabel().equalsIgnoreCase("Keybind")) {
                    items.add(new BindButton((BindValue) properties));
                }
            }
        }

        if (!module.getCategory().getLabel().equalsIgnoreCase("Other")) {
            items.add(new BindButton(module.getValue("Keybind")));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        final MatrixStack matrix = context.getMatrices();
        final String moduleDesc = module.getDescription() + ClickGUI.get().period();

        matrix.translate(0.0f, 0.0f, 1.0f);
        if (isHovering(mouseX, mouseY) && !StringUtil.isNullOrEmpty(moduleDesc)) {
            if (ClickGUI.get().descriptionBackground()) {
                RenderMethods.drawBorderedRect(context,
                        -1,
                        context.getScaledWindowHeight() - 12,
                        renderer.getWidth(moduleDesc + 9),
                        context.getScaledWindowHeight() + mc.textRenderer.fontHeight + 10,
                        ColorUtil.changeAlpha(new Color(0), 80).getRGB(),
                        Colours.get().getColour().getRGB());
            }
            context.drawTextWithShadow(
                    renderer,
                    moduleDesc,
                    2,
                    context.getScaledWindowHeight() - 10,
                    -1);
        }
        matrix.translate(0.0f, 0.0f, -1.0f);

        super.render(context, mouseX, mouseY, delta);

        if (!items.isEmpty()) {
            if (subOpen) {
                float height = 1.0f;
                for (Item item : items) {
                    item.setLocation(x + 1.0f, y + (height += 15.5f));
                    item.setHeight(15);
                    item.setWidth(getWidth() - 9);

                    final String itemDesc = item.getValue().getDescription() + ClickGUI.get().period();
                    matrix.translate(0.0f, 0.0f, 1.0f);
                    if (item.isHovering(mouseX, mouseY) && !StringUtil.isNullOrEmpty(itemDesc)) {
                        if (ClickGUI.get().descriptionBackground()) {
                            RenderMethods.drawBorderedRect(context,
                                    -1,
                                    context.getScaledWindowHeight() - 12,
                                    renderer.getWidth(itemDesc + 9),
                                    context.getScaledWindowHeight() + mc.textRenderer.fontHeight + 10,
                                    ColorUtil.changeAlpha(new Color(0), 80).getRGB(),
                                    Colours.get().getColour().getRGB());
                        }
                        context.drawTextWithShadow(
                                renderer,
                                itemDesc,
                                2,
                                context.getScaledWindowHeight() - 10,
                                -1);
                    }
                    matrix.translate(0.0f, 0.0f, -1.0f);
                    item.render(context, mouseX, mouseY, delta);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!items.isEmpty()) {
            if (button == 1 && isHovering(mouseX, mouseY)) {
                subOpen = !subOpen;
                mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 0.3F));
            }
            if (subOpen) {
                for (Item item : items) {
                    item.mouseClicked(mouseX, mouseY, button);
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int releaseButton) {
        items.forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
        super.mouseReleased(mouseX, mouseY, releaseButton);
    }

    @Override
    public void onKeyTyped(int keyCode, int scanCode, int modifiers) {
        if (!subOpen) {
            return;
        }
        items.forEach(item -> item.onKeyTyped(keyCode, scanCode, modifiers));
    }

    @Override
    public float getHeight() {
        if (subOpen) {
            float height = 14;
            for (Item item : items) {
                height += item.getHeight() + 1.5f;
            }
            return height + 2;
        }
        return 14;
    }

    @Override
    public void toggle() {
        module.toggle();
    }

    @Override
    public boolean getState() {
        return module.isEnabled();
    }
}