package com.kamildanak.minecraft.cornerstone.events;


import com.kamildanak.minecraft.cornerstone.Cornerstone;
import com.kamildanak.minecraft.cornerstone.data.ChunkPlotDataProvider;
import com.kamildanak.minecraft.cornerstone.data.Plot;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Cornerstone.modID)
public class BlockEventHandler {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Plot data = ChunkPlotDataProvider.getPlot(event.getPos(), event.getWorld().provider.getDimension());
        if (data == null) return;
        if (data.hasBuildPermission(event.getPlayer())) return;
        event.setCanceled(true);
    }


    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.PlaceEvent event) {
        Plot data = ChunkPlotDataProvider.getPlot(event.getPos(), event.getWorld().provider.getDimension());
        if (data == null) return;
        if (data.hasBuildPermission(event.getPlayer())) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.MultiPlaceEvent event) {
        for (BlockSnapshot snapshot : event.getReplacedBlockSnapshots()) {
            Plot data = ChunkPlotDataProvider.getPlot(snapshot.getPos(), snapshot.getDimId());
            if (data == null) continue;
            if (!data.hasBuildPermission(event.getPlayer())) {
                event.setCanceled(true);
                return;
            }
        }
    }
}
