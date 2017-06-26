package com.kamildanak.minecraft.cornerstone.events;

import com.kamildanak.minecraft.cornerstone.Cornerstone;
import com.kamildanak.minecraft.cornerstone.data.ClusterData;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = Cornerstone.modID)
public class ChunkEventHandler {

    @SubscribeEvent
    public static void loadChunk(ChunkDataEvent.Load event) {
        ClusterData.get(event.getChunk(), event.getWorld().provider.getDimension());
    }

    @SubscribeEvent
    public static void saveChunk(ChunkDataEvent.Save event) {
        ClusterData clusterData = ClusterData.get(event.getChunk(), event.getWorld().provider.getDimension());
        try {
            clusterData.writeIfChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
