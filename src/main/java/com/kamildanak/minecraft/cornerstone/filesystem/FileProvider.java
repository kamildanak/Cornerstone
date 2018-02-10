package com.kamildanak.minecraft.cornerstone.filesystem;

import com.kamildanak.minecraft.cornerstone.Cornerstone;
import com.kamildanak.minecraft.cornerstone.data.PlayerData;

import java.io.File;

public class FileProvider {
    private static File worldLocation;

    public static void setLocation(File worldLocation) {
        FileProvider.worldLocation = worldLocation;
    }

    public static File getFile(int dim, int x, int z) {
        File saveLocation = getSaveLocation(dim);
        return new File(saveLocation, String.format("chunk_cluster-%d_%d.json", x, z));
    }


    public static File getSaveLocation(int dimension) {
        String saveFolder = getSaveFolder(dimension);
        File dimLocation = (saveFolder == null) ? worldLocation : new File(worldLocation, saveFolder);
        File saveLocation = new File(dimLocation, Cornerstone.saveLocation);
        //noinspection ResultOfMethodCallIgnored
        saveLocation.mkdirs();
        return saveLocation;
    }

    public static String getSaveFolder(int dimensionId) {
        return (dimensionId == 0 ? null : "DIM" + dimensionId);
    }

    public static void clear() {
        worldLocation = null;
    }

    public static File getFile(PlayerData playerData) {
        File saveLocation = getSaveLocation(playerData.dimension);
        return new File(saveLocation, playerData.uuid + ".json");
    }
}
