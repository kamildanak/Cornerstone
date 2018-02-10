package com.kamildanak.minecraft.cornerstone;

import com.kamildanak.minecraft.cornerstone.data.ChunkPlotDataProvider;
import com.kamildanak.minecraft.cornerstone.data.PlayerData;
import com.kamildanak.minecraft.cornerstone.events.RendererEventHandler;
import com.kamildanak.minecraft.cornerstone.filesystem.FileProvider;
import com.kamildanak.minecraft.cornerstone.gui.ModGUIs;
import com.kamildanak.minecraft.cornerstone.proxy.Proxy;
import com.kamildanak.minecraft.cornerstone.settings.Settings;
import com.kamildanak.minecraft.cornerstone.tileentity.TileEntityCornerstone;
import com.kamildanak.minecraft.foamflower.gui.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.io.File;

@Mod(modid = Cornerstone.modID, name = Cornerstone.modName, version = Cornerstone.version,
        acceptedMinecraftVersions = Cornerstone.mcVersion)
public class Cornerstone {
    public static final String saveLocation = "Cornerstone-data";
    public static final String modID = "cornerstone";
    static final String modName = "Cornerstone";
    static final String version = "{$modVersion}";
    static final String mcVersion = "{@mcVersion}";
    public static Settings settings;
    @SidedProxy(clientSide = "com.kamildanak.minecraft.cornerstone.proxy.ProxyClient",
            serverSide = "com.kamildanak.minecraft.cornerstone.proxy.Proxy")
    @SuppressWarnings("unused")
    public static Proxy proxy;
    public static MinecraftServer minecraftServer;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        settings = new Settings();
        settings.loadConfig(event);
        proxy.preInit();
        proxy.registerPackets();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        registerEventHandler();
        for (GuiHandler guiHandler : ModGUIs.GUIS) {
            GuiHandler.register(this);
        }

        GameRegistry.registerTileEntity(TileEntityCornerstone.class, "containerCornerstone");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        settings.save();
    }

    @Mod.EventHandler
    public void onFMLServerAboutToStartEvent(FMLServerAboutToStartEvent event) {
        minecraftServer = event.getServer();
        configureChunkData(minecraftServer.getDataDirectory());
        PlayerData.clear();
    }

    @Mod.EventHandler
    public void onServerStop(FMLServerStoppingEvent event) {
        minecraftServer = null;
    }

    private void configureChunkData(@Nonnull File worldDir) {
        ChunkPlotDataProvider.clear();
        FileProvider.setLocation(worldDir);
    }

    private void registerEventHandler() {
        MinecraftForge.EVENT_BUS.register(new RendererEventHandler(Minecraft.getMinecraft()));
    }

}
