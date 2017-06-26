package com.kamildanak.minecraft.cornerstone.filesystem;

import com.kamildanak.minecraft.cornerstone.Cornerstone;
import com.kamildanak.minecraft.cornerstone.data.ClusterData;

import java.io.File;

public class FileProvider {
    private static File worldLocation;

    public static void setLocation(File worldLocation) {
        FileProvider.worldLocation = worldLocation;
    }

    public static File getFile(ClusterData clusterData) {
        File saveLocation = getSaveLocation(clusterData.dimension);
        return new File(saveLocation, String.format("chunk_cluster-%d_%d.json",
                clusterData.chunkClusterPosition.getX(),
                clusterData.chunkClusterPosition.getY()));
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
}
