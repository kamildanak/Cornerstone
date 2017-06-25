package com.kamildanak.minecraft.cornerstone;

import com.kamildanak.minecraft.cornerstone.data.ChunkData;
import com.kamildanak.minecraft.cornerstone.settings.Settings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import javax.annotation.Nonnull;

import static com.kamildanak.minecraft.cornerstone.Utils.getWorldDir;

@Mod(modid = Cornerstone.modID, name = Cornerstone.modName, version = Cornerstone.version,
        acceptedMinecraftVersions = Cornerstone.mcVersion)
public class Cornerstone {
    public static final String saveLocation = "Cornerstone-data";
    public static final String modID = "cornerstone";
    static final String modName = "Cornerstone";
    static final String version = "{$modVersion}";
    static final String mcVersion = "{@mcVersion}";
    public static Settings settings;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        settings = new Settings();
        settings.loadConfig(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        registerEventHandler();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        settings.save();
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        MinecraftServer minecraftServer = event.getServer();
        configureChunkData(minecraftServer.getEntityWorld());
    }

    private void configureChunkData(@Nonnull World worldDir) {
        ChunkData.clear();
        ChunkData.setLocation(getWorldDir(worldDir));
    }

    private void registerEventHandler() {
        //MinecraftForge.EVENT_BUS.register(new ChunkEventHandler());
    }

}
