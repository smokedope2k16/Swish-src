package dev.angel.api.util.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.angel.api.interfaces.Minecraftable;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static net.minecraft.client.render.VertexFormats.POSITION_COLOR;

public class Render2DUtil implements Minecraftable {

    public static void drawLine(MatrixStack matrixStack, float x1, float y1, float x2, float y2, Color color) {
        RenderSystem.setShaderColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionProgram);

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

        bufferBuilder.vertex(matrix, x1, y1, 0).next();
        bufferBuilder.vertex(matrix, x2, y2, 0).next();

        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawRect(MatrixStack matrixStack, float x, float y, float width, float height, int color) {
        float alpha = (float) (color >> 24 & 0xFF) / 255.0f;
        float red = (float) (color >> 16 & 0xFF) / 255.0f;
        float green = (float) (color >> 8 & 0xFF) / 255.0f;
        float blue = (float) (color & 0xFF) / 255.0f;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, POSITION_COLOR);

        bufferBuilder.vertex(matrix, x, height, 0.0f).color(red, green, blue, alpha).next();
        bufferBuilder.vertex(matrix, width, height, 0.0f).color(red, green, blue, alpha).next();
        bufferBuilder.vertex(matrix, width, y, 0.0f).color(red, green, blue, alpha).next();
        bufferBuilder.vertex(matrix, x, y, 0.0f).color(red, green, blue, alpha).next();

        tessellator.draw();

        RenderSystem.disableBlend();
    }

    public static void drawOutline(MatrixStack matrixStack, float x, float y, float width, float height, Color color) {
        RenderSystem.setShaderColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionProgram);

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);

        bufferBuilder.vertex(matrix, x, y, 0).next();
        bufferBuilder.vertex(matrix, x + width, y, 0).next();
        bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
        bufferBuilder.vertex(matrix, x, y + height, 0).next();
        bufferBuilder.vertex(matrix, x, y, 0).next();

        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

}
