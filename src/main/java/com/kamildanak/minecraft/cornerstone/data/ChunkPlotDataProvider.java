package com.kamildanak.minecraft.cornerstone.data;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChunkPlotDataProvider {
    private static Int2ObjectArrayMap<ChunkPlotDataProvider> dim2ChunkPlotDataProviderMap = new Int2ObjectArrayMap<>();
    private final Long2ObjectMap<ChunkPlotData> id2ChunkDataMap;
    private final Long2ObjectMap<Plot> corner2PlotMap;
    private final int dimension;

    private ChunkPlotDataProvider(int dimension) {
        this.id2ChunkDataMap = new Long2ObjectOpenHashMap<ChunkPlotData>(8192);
        this.corner2PlotMap = new Long2ObjectOpenHashMap<>(1000);
        this.dimension = dimension;
    }

    public static ChunkPlotDataProvider get(int dimension) {
        return dim2ChunkPlotDataProviderMap.computeIfAbsent(dimension, dim -> new ChunkPlotDataProvider(dim));
    }

    @Nullable
    public static Plot getPlot(BlockPos pos, int dimension) {
        ChunkPlotDataProvider chunkPlotDataProvider = get(dimension);
        return chunkPlotDataProvider.get(pos).get(pos);
    }

    public static void add(Plot plot, int dimension) {
        ChunkPlotDataProvider chunkPlotDataProvider = get(dimension);
        Area area = plot.getArea();
        int minZ = area.getMinZ() >> 4;
        int minX = area.getMinX() >> 4;
        int maxZ = area.getMaxZ() >> 4;
        int maxX = area.getMaxX() >> 4;
        for (int z = minZ; z <= maxZ; z++) {
            for (int x = minX; x <= maxX; x++) {
                chunkPlotDataProvider.get(x, z).add(plot);
            }
        }
        chunkPlotDataProvider.corner2PlotMap.put(plot.getCornerMin().toLong(), plot);
        PlayerData.get(plot.getOwnerUUID(), dimension).addPlot(plot);
    }

    public static ChunkPlotData get(Chunk chunk, int dimension) {
        return get(dimension).get(chunk.x, chunk.z);
    }

    public static void remove(BlockPos corner, int dimension) {
        ChunkPlotDataProvider chunkPlotDataProvider = get(dimension);
        Plot plot = chunkPlotDataProvider.get(corner).get(corner);
        if (plot == null) return;
        Area area = plot.getArea();
        int minZ = area.getMinZ() >> 4;
        int minX = area.getMinX() >> 4;
        int maxZ = area.getMaxZ() >> 4;
        int maxX = area.getMaxX() >> 4;
        for (int z = minZ; z <= maxZ; z++) {
            for (int x = minX; x <= maxX; x++) {
                chunkPlotDataProvider.get(x, z).remove(plot);
            }
        }
        chunkPlotDataProvider.corner2PlotMap.remove(plot.getCornerMin().toLong());
        PlayerData.get(plot.getOwnerUUID(), dimension).removePlot(plot);
    }

    public static void clear() {
        dim2ChunkPlotDataProviderMap.clear();
    }

    @Nonnull
    public ChunkPlotData get(int x, int z) {
        long i = ChunkPos.asLong(x, z);
        return this.id2ChunkDataMap.computeIfAbsent(i, l -> new ChunkPlotData(this.dimension, x, z));
    }

    @Nonnull
    public ChunkPlotData get(BlockPos pos) {
        return get(pos.getX() >> 4, pos.getZ() >> 4);
    }
}
