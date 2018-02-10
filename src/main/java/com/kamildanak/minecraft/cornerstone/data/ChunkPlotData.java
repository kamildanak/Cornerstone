package com.kamildanak.minecraft.cornerstone.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kamildanak.minecraft.cornerstone.filesystem.FileProvider;
import com.kamildanak.minecraft.cornerstone.filesystem.JsonFileReader;
import com.kamildanak.minecraft.cornerstone.filesystem.JsonFileWriter;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;

public class ChunkPlotData {
    private boolean changed;
    private ArrayList<Plot> plots;
    private int dim;
    private int x;
    private int z;

    public ChunkPlotData(int dim, int x, int z) {
        this.changed = true;
        this.plots = new ArrayList<>();
        this.dim = dim;
        this.x = x;
        this.z = z;
    }

    @Nullable
    public Plot get(BlockPos pos) {
        for (Plot plot : plots) {
            if (plot.getArea().contains(pos)) return plot;
        }
        return null;
    }

    public void add(Plot plot) {
        plots.add(plot);
        changed = true;
    }

    public void remove(Plot plot) {
        plots.remove(plot);
        changed = true;
    }

    public void read() throws IOException {
        changed = false;
        this.plots = new ArrayList<>();
        JsonObject jsonObject = new JsonFileReader(FileProvider.getFile(dim, x, z)).readJson();
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
        try (JsonFileWriter file = new JsonFileWriter(FileProvider.getFile(dim, x, z))) {
            file.write(obj);
        }
        changed = false;
    }
}
