package com.kamildanak.minecraft.realestate;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = RealEstate.modID, name = RealEstate.modName, version = RealEstate.version,
        acceptedMinecraftVersions = RealEstate.mcVersion)
public class RealEstate {
    static final String modID = "realestate";
    static final String modName = "{$modName}";
    static final String version = "{$modVersion}";
    static final String mcVersion = "{@mcVersion}";

    @Mod.Instance(modID)
    @SuppressWarnings("unused")
    public static RealEstate instance;

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void onServerStart(FMLServerStartingEvent event) {

    }
}
