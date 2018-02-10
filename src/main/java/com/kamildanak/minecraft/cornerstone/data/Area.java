package com.kamildanak.minecraft.cornerstone.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.math.BlockPos;

public class Area {
    private int minX, minY, minZ;
    private int maxX, maxY, maxZ;

    public Area(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        if (minX > maxX) throw new IllegalArgumentException("minX>maxX");
        if (minY > maxY) throw new IllegalArgumentException("minY>maxY");
        if (minZ > maxZ) throw new IllegalArgumentException("minZ>maxZ");
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public Area(Area area) {
        this.minX = area.minX;
        this.minY = area.minY;
        this.minZ = area.minZ;
        this.maxX = area.maxX;
        this.maxY = area.maxY;
        this.maxZ = area.maxZ;
    }

    public Area(JsonObject jsonObject) {
        this(jsonObject.get("minX").getAsInt(),
                jsonObject.get("minY").getAsInt(),
                jsonObject.get("minZ").getAsInt(),
                jsonObject.get("maxX").getAsInt(),
                jsonObject.get("maxY").getAsInt(),
                jsonObject.get("maxZ").getAsInt());
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public JsonElement toJson() {
        JsonObject element = new JsonObject();
        element.addProperty("minX", minX);
        element.addProperty("minY", minY);
        element.addProperty("minZ", minZ);
        element.addProperty("maxX", maxX);
        element.addProperty("maxY", maxY);
        element.addProperty("maxZ", maxZ);
        return element;
    }

    public boolean contains(BlockPos pos) {
        return pos.getX() >= minX && pos.getY() >= minY && pos.getZ() >= minZ &&
                pos.getX() <= maxX && pos.getY() <= maxY && pos.getZ() <= maxZ;
    }

    public BlockPos getCorner() {
        return new BlockPos(minX, minY, minZ);
    }

    // Building zones
    // Max allowed height
    // Max allowed depth (absolute minY)
}
