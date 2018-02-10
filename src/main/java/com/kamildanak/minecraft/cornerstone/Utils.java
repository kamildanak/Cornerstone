package com.kamildanak.minecraft.cornerstone;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;

import javax.annotation.Nonnull;
import javax.vecmath.Point2i;
import java.io.File;

public class Utils {
    @Nonnull
    public static File getWorldDir(@Nonnull World world) {
        ISaveHandler handler = world.getSaveHandler();
        //if (!(handler instanceof SaveHandler)) return null;
        return handler.getWorldDirectory();
    }

    @Nonnull
    public static Point2i getChunkClusterPoint2i(@Nonnull BlockPos blockPos) {
        return getChunkClusterPoint2i(blockPos.getX(), blockPos.getZ());
    }

    public static Point2i getChunkClusterPoint2i(int x, int z) {
        int nx = (int) Math.floor(x / ((float) (16 * Cornerstone.settings.getChunkClusterSize())));
        int nz = (int) Math.floor(z / ((float) (16 * Cornerstone.settings.getChunkClusterSize())));
        return new Point2i(nx, nz);
    }

    public static void markBlockForUpdate(World world, BlockPos pos) {
        if (world == null) return;
        IBlockState blockState = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, blockState, blockState, 3);
    }
}
