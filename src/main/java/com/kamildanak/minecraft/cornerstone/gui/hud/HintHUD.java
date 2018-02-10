package com.kamildanak.minecraft.cornerstone.gui.hud;

import com.kamildanak.minecraft.cornerstone.blocks.ModBlocks;
import com.kamildanak.minecraft.cornerstone.data.ChunkPlotDataProvider;
import com.kamildanak.minecraft.cornerstone.data.Plot;
import com.kamildanak.minecraft.cornerstone.network.PacketDispatcher;
import com.kamildanak.minecraft.cornerstone.network.server.MessageGetBasicInfo;
import com.kamildanak.minecraft.foamflower.gui.elements.GuiElement;
import com.kamildanak.minecraft.foamflower.gui.elements.GuiLabel;
import com.kamildanak.minecraft.foamflower.gui.hud.HUD;
import com.kamildanak.minecraft.foamflower.gui.layouts.CenteredLayout;
import com.kamildanak.minecraft.foamflower.gui.layouts.LinearLayout;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class HintHUD extends HUD {
    private Minecraft mc;
    private GuiElement root;
    private LinearLayout layout;
    private GuiLabel label;
    private HashMap<BlockPos, Long> requestedCornerstoneInfo;

    public HintHUD(Minecraft mc) {
        super(mc);
        this.mc = mc;
        ScaledResolution resolution = new ScaledResolution(mc);
        root = new CenteredLayout(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight(), true, false);
        root.gui = this;
        root.addChild(new LinearLayout(this, 0, 0, false,
                label = new GuiLabel(0, 0, "Hello World!")
        ));
        requestedCornerstoneInfo = new HashMap<>();
    }

    @Override
    public GuiElement getRoot() {
        return root;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    @SuppressWarnings("unused")
    public void onRenderInfo(RenderGameOverlayEvent.Post event) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;

        if (mc == null || mc.player == null || mc.world == null) return;
        RayTraceResult mop = this.mc.objectMouseOver;
        if (mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK) return;
        IBlockState blockState = mc.world.getBlockState(mop.getBlockPos());
        if (!blockState.getBlock().hasTileEntity(blockState)) return;
        if (!blockState.getBlock().equals(ModBlocks.cornerstone)) return;
        Plot plot = ChunkPlotDataProvider.getPlot(mop.getBlockPos(), mc.world.provider.getDimension());
        if (plot == null) {
            if (requestedCornerstoneInfo.containsKey(mop.getBlockPos())) {
                if (requestedCornerstoneInfo.get(mop.getBlockPos()) + 1000 * 10 < System.currentTimeMillis()) {
                    PacketDispatcher.sendToServer(new MessageGetBasicInfo(mop.getBlockPos(), mc.world.provider.getDimension()));
                    requestedCornerstoneInfo.replace(mop.getBlockPos(), System.currentTimeMillis());
                    label.setCaption("Loading data");
                }
                label.setCaption("No plot");
            } else {
                PacketDispatcher.sendToServer(new MessageGetBasicInfo(mop.getBlockPos(), mc.world.provider.getDimension()));
                requestedCornerstoneInfo.put(mop.getBlockPos(), System.currentTimeMillis());
                label.setCaption("Loading data");
            }
        } else {
            label.setCaption("Owner:" + plot.getOwnerUUID());
        }

        ScaledResolution resolution = new ScaledResolution(mc);
        root.h = resolution.getScaledHeight();
        root.w = resolution.getScaledWidth();
        super.render();
    }
}
