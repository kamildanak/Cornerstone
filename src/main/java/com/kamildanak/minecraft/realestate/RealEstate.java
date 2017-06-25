package com.kamildanak.minecraft.realestate;

import com.kamildanak.minecraft.enderpay.events.EventHandler;
import com.kamildanak.minecraft.realestate.blocks.BlockCornerstone;
import com.kamildanak.minecraft.realestate.data.ChunkData;
import com.kamildanak.minecraft.realestate.settings.Settings;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import javax.annotation.Nonnull;

import static com.kamildanak.minecraft.realestate.Utils.getWorldDir;

@Mod(modid = RealEstate.modID, name = RealEstate.modName, version = RealEstate.version,
        acceptedMinecraftVersions = RealEstate.mcVersion)
public class RealEstate {
    public static final String saveLocation = "RealEstate-data";
    static final String modID = "realestate";
    static final String modName = "{$modName}";
    static final String version = "{$modVersion}";
    static final String mcVersion = "{@mcVersion}";
    public static Settings settings;
    @Mod.Instance(modID)
    @SuppressWarnings("unused")
    public static RealEstate instance;
    private static Block blockCornerstone;

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
        settings = new Settings();
        settings.loadConfig(event);
        blockCornerstone = new BlockCornerstone("Cornerstone");
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent event) {
        registerEventHandler();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void postInit(FMLPostInitializationEvent event) {
        settings.save();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void onServerStart(FMLServerStartingEvent event) {
        MinecraftServer minecraftServer = event.getServer();
        configureChunkData(minecraftServer.getEntityWorld());
    }

    private void configureChunkData(@Nonnull World worldDir) {
        ChunkData.clear();
        ChunkData.setLocation(getWorldDir(worldDir));
    }

    private void registerEventHandler() {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}
