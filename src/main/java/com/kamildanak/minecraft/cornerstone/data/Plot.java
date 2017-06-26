package com.kamildanak.minecraft.cornerstone.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class Plot {
    private BlockPos blockPos;
    private UUID ownerUUID;
    private Area area;

    public Plot(BlockPos blockPos, Area area, UUID ownerUUID) {
        this.area = area;
        this.blockPos = blockPos;
        this.ownerUUID = ownerUUID;
    }

    public Plot(BlockPos blockPos, Area area, EntityPlayerMP entityPlayerMP) {
        this(blockPos, area, entityPlayerMP.getUniqueID());
    }

    public Plot(JsonElement jsonPlot) {

    }

    public JsonObject toJsonObject() {
        JsonObject element = new JsonObject();
        return null;
    }
}
