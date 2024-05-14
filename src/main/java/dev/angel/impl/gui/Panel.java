package dev.angel.impl.gui;

import dev.angel.api.interfaces.Labeled;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.util.render.RenderMethods;
import dev.angel.impl.gui.button.Button;
import dev.angel.impl.gui.item.Item;
import dev.angel.impl.module.other.clickgui.ClickGUI;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public abstract class Panel implements Labeled, Minecraftable {
    private final String label;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private final int width;
    private final int height;
    private boolean open;
    public boolean drag;
    private final ArrayList<Item> items;

    public Panel(String label, int x, int y, boolean open) {
        items = new ArrayList<>();
        this.label = label;
        this.x = x;
        this.y = y;
        width = 100;
        height = 18;
        this.open = open;
        setupItems();
    }

    public abstract void setupItems();

    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drag(mouseX, mouseY);

        float totalItemHeight = open ? getTotalItemHeight() - 2.0f : 0.0f;

        RenderMethods.drawRect(
                context,
                x,
                y - 1.5F,
                x + width,
                y + height - 6,
                ClickGUI.get().getColor().getRGB()
        );

        if (open) {
            RenderMethods.drawRect(
                    context,
                    x,
                    y + 12,
                    x + width,
                    y + height + totalItemHeight - 1.5F,
                    1996488704);
        }

        context.drawTextWithShadow(
                mc.textRenderer,
                getLabel(),
                (int) (x + 3.0f),
                (int) (y + 2.0f),
                -1);

        if (open) {
            float y = getY() + getHeight() - 3.0f;
            for (Item item : getItems()) {
                item.setLocation(x + 2.0f, y - 1.0f);
                item.setWidth(getWidth() - 4);
                item.render(context, mouseX, mouseY, partialTicks);
                y += item.getHeight() + 2.0f;
            }
        }
    }

    private void drag(int mouseX, int mouseY) {
        if (!drag) {
            return;
        }
        x = x2 + mouseX;
        y = y2 + mouseY;
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isHovering(mouseX, mouseY)) {
            x2 = (int) (x - mouseX);
            y2 = (int) (y - mouseY);
            SwishGui.getClickGui().getPanels().forEach(panel -> {
                if (panel.drag) {
                    panel.drag = false;
                }
            });
            drag = true;
        }

        if (button == 1 && isHovering(mouseX, mouseY)) {
            open = !open;
            return;
        }

        if (!open) {
            return;
        }

        getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, button));
    }

    public void addButton(Button button) {
        items.add(button);
    }

    public void mouseReleased(double mouseX, double mouseY, int releaseButton) {
        if (releaseButton == 0) {
            drag = false;
        }

        if (!open) {
            return;
        }

        getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }

    @Override
    public String getLabel() {
        return label;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getOpen() {
        return open;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    private boolean isHovering(double mouseX, double mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() - 1 && mouseY >= getY() - 1.5F && mouseY <= getY() + getHeight() - 6;
    }

    private float getTotalItemHeight() {
        float height = 0.0f;
        for (Item item : getItems()) {
            height += item.getHeight() + 2.0f;
        }
        return height;
    }

    public void setX(int dragX) {
        x = dragX;
    }

    public void setY(int dragY) {
        y = dragY;
    }

    public void onKeyTyped(int keyCode, int scanCode, int modifiers) {
        if (!open) {
            return;
        }
        getItems().forEach(item -> item.onKeyTyped(keyCode, scanCode, modifiers));
    }
}