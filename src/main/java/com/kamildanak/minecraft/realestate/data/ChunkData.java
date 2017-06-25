package com.kamildanak.minecraft.realestate.data;

import com.kamildanak.minecraft.realestate.RealEstate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.vecmath.Point2i;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static com.kamildanak.minecraft.realestate.Utils.getWorldDir;

public class ChunkData {
    private static HashMap<Point2i, ChunkData> objects = new HashMap<>();
    private static File location;
    private final Point2i chunkClusterPosition;
    private final String saveFolder;
    private boolean changed;

    private ChunkData(@Nonnull Point2i chunkClusterPosition, String saveFolder) {
        super();
        this.chunkClusterPosition = chunkClusterPosition;
        this.changed = true;
        this.saveFolder = saveFolder;
    }

    @Nonnull
    public static ChunkData get(@Nonnull BlockPos blockPos, World world) {
        Point2i chunkClusterPosition = getChunkClusterPoint2i(blockPos);
        ChunkData chunkData = objects.get(chunkClusterPosition);
        if (chunkData != null) return chunkData;
        if (location == null) location = getWorldDir(world);
        //noinspection ResultOfMethodCallIgnored
        location.mkdirs();

        chunkData = new ChunkData(chunkClusterPosition, world.provider.getSaveFolder());
        objects.put(chunkClusterPosition, chunkData);

        File file = chunkData.getFile();
        if (!file.exists()) return chunkData;

        try {
            chunkData.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chunkData;
    }

    public static void clear() {
        ChunkData.location = null;
        ChunkData.objects.clear();
    }

    @Nonnull
    private static Point2i getChunkClusterPoint2i(@Nonnull BlockPos blockPos) {
        int x = (int) Math.floor(blockPos.getX() / ((float) (16 * RealEstate.settings.getChunkClusterSize())));
        int y = (int) Math.floor(blockPos.getY() / ((float) (16 * RealEstate.settings.getChunkClusterSize())));
        return new Point2i(x, y);
    }

    public static void setLocation(File location) {
        ChunkData.location = location;
    }

    private void read() throws IOException {

    }

    private File getFile() {
        File dimLocation = new File(location, saveFolder);
        File saveLocation = new File(dimLocation, RealEstate.saveLocation);
        return new File(saveLocation, String.format("chunk_cluster-%d_%d.json",
                chunkClusterPosition.getX(), chunkClusterPosition.getY()));
    }
}
