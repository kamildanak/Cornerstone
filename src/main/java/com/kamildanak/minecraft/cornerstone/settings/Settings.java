package com.kamildanak.minecraft.cornerstone.settings;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Settings implements ISettings {
    private Configuration config;

    private int chunkClusterSize;
    private int searchNeighbours;

    public Settings() {

    }

    private void loadConfig(Configuration config) {
        chunkClusterSize = config.getInt("chunkClusterSize", "dataStorage",
                8, 1, 128, "How many chunks (16x16 blocks) should have common plot interval tree");
        searchNeighbours = config.getInt("searchNeighbours", "dataStorage",
                2, 2, 4, "Number of chunk clusters to search [2->(x,y),(x-1,y),(x,y-1),(x-1,y-1)]");
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
}
