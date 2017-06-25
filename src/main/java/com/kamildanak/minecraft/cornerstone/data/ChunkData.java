package com.kamildanak.minecraft.cornerstone.data;

import com.google.gson.JsonObject;
import com.kamildanak.minecraft.cornerstone.Cornerstone;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import javax.vecmath.Point2i;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import static com.kamildanak.minecraft.cornerstone.Utils.getWorldDir;

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
    public static ChunkData get(@Nonnull Chunk chunk, @Nonnull World world) {
        return get(getChunkClusterPoint2i(chunk.getPos().getXStart(), chunk.getPos().getZStart()), world);
    }

    @Nonnull
    public static ChunkData get(@Nonnull BlockPos blockPos, World world) {
        return get(getChunkClusterPoint2i(blockPos), world);
    }

    @Nonnull
    private static ChunkData get(@Nonnull Point2i chunkClusterPosition, World world) {
        ChunkData chunkData = objects.get(chunkClusterPosition);
        if (chunkData != null) return chunkData;

        chunkData = new ChunkData(chunkClusterPosition, world.provider.getSaveFolder());
        objects.put(chunkClusterPosition, chunkData);

        File file = chunkData.getFile(world);
        if (!file.exists()) return chunkData;

        try {
            chunkData.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chunkData;
    }

    private static File getSaveLocation(World world) {
        if (location == null) location = getWorldDir(world);
        String saveFolder = world.provider.getSaveFolder();
        File dimLocation = (saveFolder == null) ? location : new File(location, saveFolder);
        File saveLocation = new File(dimLocation, Cornerstone.saveLocation);
        //noinspection ResultOfMethodCallIgnored
        saveLocation.mkdirs();
        return saveLocation;
    }

    public static void clear() {
        ChunkData.location = null;
        ChunkData.objects.clear();
    }

    @Nonnull
    private static Point2i getChunkClusterPoint2i(@Nonnull BlockPos blockPos) {
        return getChunkClusterPoint2i(blockPos.getX(), blockPos.getZ());
    }

    private static Point2i getChunkClusterPoint2i(int x, int z) {
        int nx = (int) Math.floor(x / ((float) (16 * Cornerstone.settings.getChunkClusterSize())));
        int nz = (int) Math.floor(z / ((float) (16 * Cornerstone.settings.getChunkClusterSize())));
        return new Point2i(nx, nz);
    }

    public static void setLocation(File location) {
        ChunkData.location = location;
    }

    private void read() throws IOException {

    }

    private File getFile(World world) {
        File saveLocation = getSaveLocation(world);
        return new File(saveLocation, String.format("chunk_cluster-%d_%d.json",
                chunkClusterPosition.getX(), chunkClusterPosition.getY()));
    }

    public void writeIfChanged(World world) throws IOException {
        if (changed) write(world);
    }

    private void write(World world) throws IOException {
        write(getFile(world));
    }

    private void write(File location) throws IOException {
        JsonObject obj = new JsonObject();
        //obj.addProperty("balance", balance);
        //obj.addProperty("lastLogin", lastLogin);
        //obj.addProperty("lastCountActivity", lastCountActivity);
        try (FileWriter file = new FileWriter(location)) {
            String str = obj.toString();
            file.write(str);
        }
        changed = false;
    }
}
