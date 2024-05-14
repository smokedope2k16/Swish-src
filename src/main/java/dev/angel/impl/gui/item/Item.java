package dev.angel.impl.gui.item;

import dev.angel.api.interfaces.Labeled;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.value.Value;
import dev.angel.impl.gui.Panel;
import dev.angel.impl.gui.SwishGui;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class Item implements Labeled, Minecraftable {
    private final String label;
    private Value<?> value;
    protected float x;
    protected float y;
    protected float width;
    protected float height;

    protected TextRenderer renderer = mc.textRenderer;

    public Item(String label) {
        this.label = label;
    }

    public Item(String label, Value<?> property) {
        this.label = label;
        this.value = property;
    }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public void mouseReleased(double mouseX, double mouseY, int releaseButton) {
    }

    public void onKeyTyped(int keyCode, int scanCode, int modifiers) {
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Value<?> getValue() {
        return this.value;
    }

    public void setValue(Value<?> value) {
        this.value = value;
    }

    public boolean isHovering(double mouseX, double mouseY) {
        for (Panel panel : SwishGui.getClickGui().getPanels()) {
            if (!panel.drag) continue;
            return false;
        }
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + height;
    }
}