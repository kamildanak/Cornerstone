package com.kamildanak.minecraft.realestate;

import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;

import javax.annotation.Nonnull;
import java.io.File;

public class Utils {
    @Nonnull
    public static File getWorldDir(@Nonnull World world) {
        ISaveHandler handler = world.getSaveHandler();
        //if (!(handler instanceof SaveHandler)) return null;
        return handler.getWorldDirectory();
    }
}
