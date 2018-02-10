package com.kamildanak.minecraft.cornerstone.events;


import com.kamildanak.minecraft.cornerstone.Cornerstone;
import com.kamildanak.minecraft.cornerstone.data.ChunkPlotDataProvider;
import com.kamildanak.minecraft.cornerstone.data.Plot;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Cornerstone.modID)
public class ExplosionEventHandler {
    @SubscribeEvent
    public static void onExplosionStart(ExplosionEvent.Start event) {
        Plot plot = ChunkPlotDataProvider.getPlot(new BlockPos(event.getExplosion().getPosition()),
                event.getWorld().provider.getDimension());
        if (plot != null && plot.explosionProtection()) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        event.getAffectedBlocks().removeIf(pos -> {
            Plot plot = ChunkPlotDataProvider.getPlot(pos, event.getWorld().provider.getDimension());
            return plot != null && plot.explosionProtection();
        });
    }
}
