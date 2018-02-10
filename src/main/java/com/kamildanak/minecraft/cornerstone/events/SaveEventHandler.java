package com.kamildanak.minecraft.cornerstone.events;

import com.kamildanak.minecraft.cornerstone.Cornerstone;
import com.kamildanak.minecraft.cornerstone.data.ChunkPlotDataProvider;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.FileNotFoundException;
import java.io.IOException;

@Mod.EventBusSubscriber(modid = Cornerstone.modID)
public class SaveEventHandler {

    @SubscribeEvent
    public static void loadChunk(ChunkDataEvent.Load event) {
        try {
            ChunkPlotDataProvider.get(event.getChunk(), event.getWorld().provider.getDimension()).read();
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void saveChunk(ChunkDataEvent.Save event) {
        try {
            ChunkPlotDataProvider.get(event.getChunk(), event.getWorld().provider.getDimension()).writeIfChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
