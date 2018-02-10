package com.kamildanak.minecraft.cornerstone.events;

import com.kamildanak.minecraft.cornerstone.Cornerstone;
import com.kamildanak.minecraft.cornerstone.data.ChunkPlotDataProvider;
import com.kamildanak.minecraft.cornerstone.data.Plot;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Cornerstone.modID)
public class EntityEventHandler {

    @SubscribeEvent
    public static void as1(PlayerInteractEvent.EntityInteractSpecific event) {
        Plot plot = ChunkPlotDataProvider.getPlot(event.getPos(), event.getWorld().provider.getDimension());
        if (plot == null) return;
        if (!plot.hasBuildPermission(event.getEntityPlayer())) event.setCanceled(true);
    }


    @SubscribeEvent
    public static void as2(PlayerInteractEvent.LeftClickBlock event) {
        Plot plot = ChunkPlotDataProvider.getPlot(event.getPos(), event.getWorld().provider.getDimension());
        if (plot == null) return;
        if (!plot.hasBuildPermission(event.getEntityPlayer())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void as3(PlayerInteractEvent.RightClickBlock event) {
        Plot plot = ChunkPlotDataProvider.getPlot(event.getPos(), event.getWorld().provider.getDimension());
        if (plot == null) return;
        if (!plot.hasBuildPermission(event.getEntityPlayer())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void as4(PlayerInteractEvent.RightClickItem event) {
        Plot plot = ChunkPlotDataProvider.getPlot(event.getPos(), event.getWorld().provider.getDimension());
        if (plot == null) return;
        if (!plot.hasBuildPermission(event.getEntityPlayer())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void as5(LivingDestroyBlockEvent event) {
        Plot plot = ChunkPlotDataProvider.getPlot(event.getPos(), event.getEntity().dimension);
        if (plot == null) return;
        if (plot.entityProtection()) event.setCanceled(true);
    }
}
