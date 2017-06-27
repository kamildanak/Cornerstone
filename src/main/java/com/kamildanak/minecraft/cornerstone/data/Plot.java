package com.kamildanak.minecraft.cornerstone.data;

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

    public Plot(JsonObject jsonPlot) {
        this(jsonToBlockPos(jsonPlot.get("blockPos").getAsJsonObject()),
                new Area(jsonPlot.get("area").getAsJsonObject()),
                UUID.fromString(jsonPlot.get("ownerUUID").getAsString()));
    }

    private static BlockPos jsonToBlockPos(JsonObject jsonObject) {
        return new BlockPos(jsonObject.get("x").getAsInt(),
                jsonObject.get("y").getAsInt(),
                jsonObject.get("z").getAsInt());
    }

    private static JsonObject blockPosToJson(BlockPos blockPos) {
        JsonObject element = new JsonObject();
        element.addProperty("x", blockPos.getX());
        element.addProperty("y", blockPos.getY());
        element.addProperty("z", blockPos.getZ());
        return element;
    }

    public JsonObject toJsonObject() {
        JsonObject element = new JsonObject();
        element.add("blockPos", blockPosToJson(blockPos));
        element.addProperty("ownerUUID", ownerUUID.toString());
        element.add("area", area.toJson());
        return element;
    }

    public Area getArea() {
        return area;
    }


    public BlockPos getBlockPos() {
        return blockPos;
    }

    public BlockPos getCorner() {
        return new BlockPos(area.getMinX(), area.getMinY(), area.getMinZ());
    }
}
