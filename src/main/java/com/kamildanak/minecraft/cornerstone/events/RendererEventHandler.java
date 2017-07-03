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
        ArrayList<BlockPos> cornerstones = new ArrayList<>();
        cornerstones.addAll(PlayerData.get(entityplayer.getUniqueID()).getCornerstoneUnderConstruction());
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
        ArrayList<Integer> pointsX = new ArrayList<>();
        ArrayList<Integer> pointsZ = new ArrayList<>();
        for (BlockPos pos : cornerstones) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            boolean drawX = true;
            boolean drawZ = true;
            for (BlockPos pos2 : cornerstones) {
                if (pos == pos2) continue;
                if (pos.getX() == pos2.getX()) {
                    drawZ = false;
                    addLine(bufferbuilder, new int[]{x, y, z}, new int[]{x, y, pos2.getZ()}, dx, dy, dz);
                }
                if (pos.getZ() == pos2.getZ()) {
                    drawX = false;
                    addLine(bufferbuilder, new int[]{x, y, z}, new int[]{pos2.getX(), y, z}, dx, dy, dz);
                }
            }
            if (cornerstones.size() >= 3) {
                if (pointsX.contains(x)) {
                    pointsX.removeIf(integer -> integer == x);
                } else {
                    pointsX.add(x);
                }
                if (pointsZ.contains(z)) {
                    pointsZ.removeIf(integer -> integer == z);
                } else {
                    pointsZ.add(z);
                }
                continue;
            }
            if (drawX)
                addLine(bufferbuilder, new int[]{x - maxLen, y, z}, new int[]{x + maxLen, y, z}, dx, dy, dz);
            if (drawZ)
                addLine(bufferbuilder, new int[]{x, y, z - maxLen}, new int[]{x, y, z + maxLen}, dx, dy, dz);
        }
        if (cornerstones.size() >= 3 && pointsX.size() == 1 && pointsZ.size() == 1) {
            for (BlockPos pos : cornerstones) {
                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();
                if (pos.getX() == pointsX.get(0)) {
                    addLine(bufferbuilder, new int[]{x, y, z}, new int[]{x, y, pointsZ.get(0)}, dx, dy, dz);
                }
                if (pos.getZ() == pointsZ.get(0)) {
                    addLine(bufferbuilder, new int[]{x, y, z}, new int[]{pointsX.get(0), y, z}, dx, dy, dz);
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

    private static void addLine(BufferBuilder bufferBuilder, int[] s, int[] e, double dx, double dy, double dz) {
        if (s.length != 3 || e.length != 3) return;
        int d = (s[0] == e[0]) ? 2 : (s[2] == e[2]) ? 0 : -1;
        if (d == -1) return;
        if (s[d] < e[d]) {
            int tmp = e[d];
            e[d] = s[d];
            s[d] = tmp;
        }
        s[d]++;
        for (int i = 0; i < 2; i++) {
            addLine(bufferBuilder, s[0], s[1], s[2], e[0], e[1], e[2], dx, dy, dz);
            addLine(bufferBuilder, s[0], s[1] + 1, s[2], e[0], e[1] + 1, e[2], dx, dy, dz);
            s[(d == 0) ? 2 : 0]++;
            e[(d == 0) ? 2 : 0]++;
        }
    }

    @SubscribeEvent
    public void renderMesh(RenderWorldLastEvent event) {
        render(event.getPartialTicks(), mc.player);
    }
}
