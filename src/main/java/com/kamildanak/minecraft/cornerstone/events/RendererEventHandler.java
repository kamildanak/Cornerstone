package com.kamildanak.minecraft.cornerstone.events;

import com.kamildanak.minecraft.cornerstone.Cornerstone;
import com.kamildanak.minecraft.cornerstone.data.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class RendererEventHandler {
    private Minecraft mc;

    public RendererEventHandler(Minecraft mc) {
        this.mc = mc;
    }

    private static void render(double partialTicks, EntityPlayer entityplayer) {
        ArrayList<BlockPos> cornerstones = PlayerData.get(entityplayer.getUniqueID()).getCornerstoneUnderConstruction();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.disableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.glLineWidth(1.0F);

        double dx = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * partialTicks;
        double dy = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * partialTicks;
        double dz = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * partialTicks;

        GlStateManager.glLineWidth(2.0F);
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

        int maxLen = Cornerstone.settings.getMaxPlotLength();
        ArrayList<Double> pointsX = new ArrayList<>();
        ArrayList<Double> pointsZ = new ArrayList<>();
        for (BlockPos pos : cornerstones) {
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();
            boolean drawX = true;
            boolean drawZ = true;
            for (BlockPos pos2 : cornerstones) {
                if (pos == pos2) continue;
                if (pos.getX() == pos2.getX()) {
                    drawZ = false;
                    addLine(bufferbuilder, x + 0.5, y + 0.5, z + 0.5,
                            x + 0.5, y + 0.5, pos2.getZ() + 0.5, dx, dy, dz);
                }
                if (pos.getZ() == pos2.getZ()) {
                    drawX = false;
                    addLine(bufferbuilder, x + 0.5, y + 0.5, z + 0.5,
                            pos2.getX() + 0.5, y + 0.5, z + 0.5, dx, dy, dz);
                }
            }
            if (cornerstones.size() >= 3) {
                if (pointsX.contains(x)) {
                    pointsX.remove(x);
                } else {
                    pointsX.add(x);
                }
                if (pointsZ.contains(z)) {
                    pointsZ.remove(z);
                } else {
                    pointsZ.add(z);
                }
                continue;
            }
            if (drawX)
                addLine(bufferbuilder, x + 1.5 - maxLen, y + 0.5, z + 0.5,
                        x - 0.5 + maxLen, y + 0.5, z + 0.5, dx, dy, dz);
            if (drawZ)
                addLine(bufferbuilder, x + 0.5, y + 0.5, z + 1.5 - maxLen,
                        x + 0.5, y + 0.5, z - 0.5 + maxLen, dx, dy, dz);
        }
        if (cornerstones.size() >= 3 && pointsX.size() == 1 && pointsZ.size() == 1) {
            for (BlockPos pos : cornerstones) {
                double x = pos.getX();
                double y = pos.getY();
                double z = pos.getZ();
                if (pos.getX() == pointsX.get(0)) {
                    addLine(bufferbuilder, x + 0.5, y + 0.5, z + 0.5,
                            x + 0.5, y + 0.5, pointsZ.get(0) + 0.5, dx, dy, dz);
                }
                if (pos.getZ() == pointsZ.get(0)) {
                    addLine(bufferbuilder, x + 0.5, y + 0.5, z + 0.5,
                            pointsX.get(0) + 0.5, y + 0.5, z + 0.5, dx, dy, dz);
                }
            }
        }

        tessellator.draw();

        GlStateManager.glLineWidth(1.0F);
        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
    }

    private static void addLine(BufferBuilder bufferBuilder,
                                double sx, double sy, double sz, double ex, double ey, double ez,
                                double dx, double dy, double dz) {
        bufferBuilder.pos(sx - dx, sy - dy, sz - dz)
                .color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
        bufferBuilder.pos(ex - dx, ey - dy, ez - dz)
                .color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
        bufferBuilder.pos(ex - dx, ey - dy, ez - dz)
                .color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
        bufferBuilder.pos(sx - dx, sy - dy, sz - dz)
                .color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
    }

    @SubscribeEvent
    public void renderMesh(RenderWorldLastEvent event) {
        render(event.getPartialTicks(), mc.player);
    }
}
