package dev.angel.api.util.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.angel.api.interfaces.Minecraftable;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class RenderMethods implements Minecraftable {

    public static void enable2D() {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.depthMask(true);
    }

    public static void disable2D() {
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(false);
    }

    public static void color(int hex) {
        float alpha = (hex >> 24 & 255) / 255.0F;
        float red = (hex >> 16 & 255) / 255.0F;
        float green = (hex >> 8 & 255) / 255.0F;
        float blue = (hex & 255) / 255.0F;
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    public static void resetColor() {
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static void draw(DrawContext context, float x, float y, float width, float height, int color) {
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        float i;
        if (x < width) {
            i = x;
            x = width;
            width = i;
        }

        if (y < height) {
            i = y;
            y = height;
            height = i;
        }

        float alpha = (float) ColorHelper.Argb.getAlpha(color) / 255.0F;
        float red = (float) ColorHelper.Argb.getRed(color) / 255.0F;
        float green = (float) ColorHelper.Argb.getGreen(color) / 255.0F;
        float blue = (float) ColorHelper.Argb.getBlue(color) / 255.0F;
        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(RenderLayer.getGui());
        vertexConsumer.vertex(matrix4f, x, y, 0).color(red, green, blue, alpha).next();
        vertexConsumer.vertex(matrix4f, x, height, 0).color(red, green, blue, alpha).next();
        vertexConsumer.vertex(matrix4f, width, height, 0).color(red, green, blue, alpha).next();
        vertexConsumer.vertex(matrix4f, width, y, 0).color(red, green, blue, alpha).next();
        context.getVertexConsumers().draw();
    }

    public static void drawRect(DrawContext context, float x, float y, float width, float height, int color) {
        enable2D();
        draw(context, x, y, width, height, color);
        disable2D();
    }

    public static void drawBorderedRect(DrawContext context, float x, float y, float x1, float y1, float width, int color, int outlineColor) {
        enable2D();
        draw(context, x + width, y + width, x1 - width, y1 - width, color);
        draw(context, x + width, y, x1 - width, y + width, outlineColor);
        draw(context, x, y, x + width, y1, outlineColor);
        draw(context, x1 - width, y, x1, y1, outlineColor);
        draw(context, x + width, y1 - width, x1 - width, y1, outlineColor);
        disable2D();
    }

    public static void drawBorderedRect(DrawContext context, float x, float y, float width, float height, int color, int outlineColor) {
        enable2D();
        x *= 2;
        width *= 2;
        y *= 2;
        height *= 2;
        context.getMatrices().scale(0.5F, 0.5F, 0.5F);
        drawVerticalLine(context, x, y, height - 1, outlineColor);
        drawVerticalLine(context, width - 1, y, height, outlineColor);
        drawHorizontalLine(context, x, width - 1, y, outlineColor);
        drawHorizontalLine(context, x, width - 2, height - 1, outlineColor);
        draw(context, x + 1, y + 1, width - 1, height - 1, color);
        context.getMatrices().scale(2.0F, 2.0F, 2.0F);
        disable2D();
    }

    public static void drawHorizontalLine(DrawContext context, float x, float x2, float y, int color) {
        if (x2 < x) {
            float fixedX = x;
            x = x2;
            x2 = fixedX;
        }

        draw(context, x, y, x2 + 1, y + 1, color);
    }

    public static void drawVerticalLine(DrawContext context, float x, float y, float y2, int color) {
        if (y2 < y) {
            float fixedY = y;
            y = y2;
            y2 = fixedY;
        }

        draw(context, x, y + 1, x + 1, y2, color);
    }

    public static void enable3D() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
    }

    public static void disable3D() {
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
    }

    public static void drawPlane(MatrixStack matrixStack, Box box) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionProgram);

        float minX = (float) box.minX;
        float minY = (float) box.minY;
        float minZ = (float) box.minZ;

        float maxX = (float) box.maxX;
        float maxY = (float) box.maxY;
        float maxZ = (float) box.maxZ;

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, minX, minY, minZ).next();
        bufferBuilder.vertex(matrix, maxX, minY, minZ).next();
        bufferBuilder.vertex(matrix, maxX, minY, maxZ).next();
        bufferBuilder.vertex(matrix, minX, minY, maxZ).next();
        bufferBuilder.vertex(matrix, minX, minY, minZ).next();
        tessellator.draw();

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, minX, maxY, minZ).next();
        bufferBuilder.vertex(matrix, maxX, maxY, minZ).next();
        bufferBuilder.vertex(matrix, maxX, maxY, maxZ).next();
        bufferBuilder.vertex(matrix, minX, maxY, maxZ).next();
        bufferBuilder.vertex(matrix, minX, maxY, minZ).next();
        tessellator.draw();

        bufferBuilder.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, minX, minY, minZ).next();
        bufferBuilder.vertex(matrix, minX, maxY, minZ).next();
        bufferBuilder.vertex(matrix, maxX, minY, minZ).next();
        bufferBuilder.vertex(matrix, maxX, maxY, minZ).next();
        bufferBuilder.vertex(matrix, maxX, minY, maxZ).next();
        bufferBuilder.vertex(matrix, maxX, maxY, maxZ).next();
        bufferBuilder.vertex(matrix, minX, minY, maxZ).next();
        bufferBuilder.vertex(matrix, minX, maxY, maxZ).next();
        tessellator.draw();
    }

    public static void drawBox(MatrixStack matrixStack, Box bb) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionProgram);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION);
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ)
                .next();
        tessellator.draw();
    }

    public static void drawOutline(MatrixStack matrixStack, Box bb) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionProgram);

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
                VertexFormats.POSITION);
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ)
                .next();
        tessellator.draw();
    }

    public static void drawCross(MatrixStack matrixStack, Box bb) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionProgram);

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
                VertexFormats.POSITION);
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ)
                .next();
        tessellator.draw();
    }
}
