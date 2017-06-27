package com.kamildanak.minecraft.cornerstone.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RendererEventHandler {
    Minecraft mc;

    public RendererEventHandler(Minecraft mc) {
        this.mc = mc;
    }

    public static void render(double partialTicks, EntityPlayer entityplayer) {
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

        /*double x = entityplayer.posX;
        double y = entityplayer.posY;
        double z = entityplayer.posZ;
        addLine(bufferbuilder, x+0.5, y-0.5, z+0.5, x+0.5+10, y-0.5, z+0.5, dx, dy, dz);
        addLine(bufferbuilder, x+0.5, y-0.5, z+0.5, x+0.5-10, y-0.5, z+0.5, dx, dy, dz);
        addLine(bufferbuilder, x+0.5, y-0.5, z+0.5, x+0.5, y-0.5, z+0.5+10, dx, dy, dz);
        addLine(bufferbuilder, x+0.5, y-0.5, z+0.5, x+0.5, y-0.5, z+0.5-10, dx, dy, dz);*/

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
