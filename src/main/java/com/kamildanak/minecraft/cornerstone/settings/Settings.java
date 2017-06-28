package com.kamildanak.minecraft.cornerstone.settings;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Settings implements ISettings {
    private Configuration config;

    private int chunkClusterSize;
    private int searchNeighbours;
    private int maxPlotLength;

    public Settings() {

    }

    private void loadConfig(Configuration config) {
        chunkClusterSize = config.getInt("chunkClusterSize", "dataStorage",
                8, 1, 128, "How many chunks (16x16 blocks) should have common plot interval tree");
        searchNeighbours = config.getInt("searchNeighbours", "dataStorage",
                2, 2, 4, "Number of chunk clusters to search [2->(x,y),(x-1,y),(x,y-1),(x-1,y-1)]");
        maxPlotLength = config.getInt("maxPlotLength", "general",
                Math.min(128, 16 * chunkClusterSize), 5, 16 * chunkClusterSize,
                "Maximum plot length, cannot be more than 16*chunkClusterSize");
    }

    public void loadConfig(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        loadConfig(config);
    }

    public void save() {
        config.save();
    }

    public int getChunkClusterSize() {
        return chunkClusterSize;
    }

    public int getSearchNeighbours() {
        return searchNeighbours;
    }

    public int getMaxPlotLength() {
        return maxPlotLength;
    }
}
