package com.kamildanak.minecraft.cornerstone.data;

import com.kamildanak.minecraft.cornerstone.Cornerstone;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {
    private static HashMap<UUID, PlayerData> objects = new HashMap<>();
    private ArrayList<BlockPos> cornerstoneUnderConstruction;

    private PlayerData() {
        cornerstoneUnderConstruction = new ArrayList<>();
    }

    @Nonnull
    public static PlayerData get(@Nonnull UUID playerUUID) {
        PlayerData playerData = objects.get(playerUUID);
        if (playerData != null) return playerData;
        playerData = new PlayerData();
        objects.put(playerUUID, playerData);
        return playerData;
    }

    public void addCornerstone(BlockPos blockPos) {
        cornerstoneUnderConstruction.add(blockPos);
    }

    public ArrayList<BlockPos> getCornerstoneUnderConstruction() {
        return cornerstoneUnderConstruction;
    }

    public boolean canPlaceCornerstoneAt(BlockPos posN) {
        BlockPos pos0, pos1, pos2;
        switch (cornerstoneUnderConstruction.size()) {
            case 0:
                return true;
            case 1:
                pos0 = cornerstoneUnderConstruction.get(0);
                if (pos0.getX() == posN.getX())
                    return Math.abs(pos0.getZ() - posN.getZ()) < Cornerstone.settings.getMaxPlotLength();
                if (pos0.getZ() == posN.getZ())
                    return Math.abs(pos0.getX() - posN.getX()) < Cornerstone.settings.getMaxPlotLength();
                return false;
            case 2:
                pos0 = cornerstoneUnderConstruction.get(0);
                pos1 = cornerstoneUnderConstruction.get(1);
                if (pos0.getX() == pos1.getX()) {
                    if (pos0.getZ() == posN.getZ())
                        return Math.abs(pos0.getX() - posN.getX()) < Cornerstone.settings.getMaxPlotLength();
                    if (pos1.getZ() == posN.getZ())
                        return Math.abs(pos1.getX() - posN.getX()) < Cornerstone.settings.getMaxPlotLength();
                }
                if (pos0.getZ() == pos1.getZ()) {
                    if (pos0.getX() == posN.getX())
                        return Math.abs(pos0.getZ() - posN.getZ()) < Cornerstone.settings.getMaxPlotLength();
                    if (pos1.getX() == posN.getX())
                        return Math.abs(pos1.getZ() - posN.getZ()) < Cornerstone.settings.getMaxPlotLength();
                }
                return false;
            case 3:
                pos0 = cornerstoneUnderConstruction.get(0);
                pos1 = cornerstoneUnderConstruction.get(1);
                pos2 = cornerstoneUnderConstruction.get(2);
                if (pos0.getX() == pos1.getX() && pos0.getZ() == pos2.getZ())
                    return posN.getX() == pos2.getX() && posN.getZ() == pos1.getZ();
                if (pos0.getZ() == pos1.getZ() && pos0.getX() == pos2.getX())
                    return posN.getZ() == pos2.getZ() && posN.getX() == pos1.getX();
                return false;
        }
        return false;
    }

    public void removeCornerstone(BlockPos pos) {
        cornerstoneUnderConstruction.removeIf(blockPos ->
                (blockPos.getX() == pos.getX()) && (blockPos.getY() == pos.getY()) && (blockPos.getZ() == pos.getZ()));
    }
}
