package com.kamildanak.minecraft.cornerstone.data;

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

    // Building zones
    // Max allowed height
    // Max allowed depth (absolute minY)
}
