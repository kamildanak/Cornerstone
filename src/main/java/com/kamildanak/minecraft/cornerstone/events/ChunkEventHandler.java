package com.kamildanak.minecraft.cornerstone.events;

import com.kamildanak.minecraft.cornerstone.Cornerstone;
import com.kamildanak.minecraft.cornerstone.data.ChunkData;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = Cornerstone.modID)
public class ChunkEventHandler {

    @SubscribeEvent
    public static void loadChunk(ChunkDataEvent.Load event) {
        ChunkData.get(event.getChunk(), event.getWorld());
    }

    @SubscribeEvent
    public static void saveChunk(ChunkDataEvent.Save event) {
        ChunkData chunkData = ChunkData.get(event.getChunk(), event.getWorld());
        try {
            chunkData.writeIfChanged(event.getWorld());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
