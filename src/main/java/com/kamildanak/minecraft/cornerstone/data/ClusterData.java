package com.kamildanak.minecraft.cornerstone.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kamildanak.minecraft.cornerstone.filesystem.FileProvider;
import com.kamildanak.minecraft.cornerstone.filesystem.JsonFileReader;
import com.kamildanak.minecraft.cornerstone.filesystem.JsonFileWriter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import javax.vecmath.Point2i;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.kamildanak.minecraft.cornerstone.Utils.getChunkClusterPoint2i;

public class ClusterData {
    private static HashMap<Point2i, ClusterData> objects = new HashMap<>();

    public final Point2i chunkClusterPosition;
    public final int dimension;
    private boolean changed;
    private ArrayList<Plot> plots;

    private ClusterData(@Nonnull Point2i chunkClusterPosition, int dimension) {
        super();
        this.chunkClusterPosition = chunkClusterPosition;
        this.changed = true;
        this.dimension = dimension;
        this.plots = new ArrayList<>();
    }

    public static void add(Plot plot, int dimension) {
        ClusterData clusterData = get(plot.getCorner(), dimension);
        clusterData.plots.add(plot);
    }

    @Nonnull
    private static ClusterData get(@Nonnull Point2i chunkClusterPosition, int dimension) {
        ClusterData clusterData = objects.get(chunkClusterPosition);
        if (clusterData != null) return clusterData;

        clusterData = new ClusterData(chunkClusterPosition, dimension);
        objects.put(chunkClusterPosition, clusterData);
        try {
            clusterData.read();
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clusterData;
    }

    @Nonnull
    public static ClusterData get(@Nonnull Chunk chunk, int dimension) {
        return get(getChunkClusterPoint2i(chunk.getPos().getXStart(), chunk.getPos().getZStart()), dimension);
    }

    @Nonnull
    public static ClusterData get(@Nonnull BlockPos blockPos, int dimension) {
        return get(getChunkClusterPoint2i(blockPos), dimension);
    }

    public static void clear() {
        ClusterData.objects.clear();
    }

    private void read() throws IOException {
        changed = false;
        this.plots = new ArrayList<>();
        JsonObject jsonObject = new JsonFileReader(FileProvider.getFile(this)).readJson();
        JsonArray jsonPlots = jsonObject.get("plots").getAsJsonArray();
        for (JsonElement jsonPlot : jsonPlots) {
            this.plots.add(new Plot(jsonPlot.getAsJsonObject()));
        }
    }

    public void writeIfChanged() throws IOException {
        if (changed) write();
    }

    private void write() throws IOException {
        JsonObject obj = new JsonObject();
        JsonArray jsonPlots = new JsonArray();
        for (Plot plot : plots) {
            jsonPlots.add(plot.toJsonObject());
        }
        obj.add("plots", jsonPlots);
        try (JsonFileWriter file = new JsonFileWriter(FileProvider.getFile(this))) {
            file.write(obj);
        }
        changed = false;
    }
}
