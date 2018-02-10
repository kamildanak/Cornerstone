package com.kamildanak.minecraft.cornerstone.data;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class Plot {
    private UUID ownerUUID;
    private Area area;

    public Plot(Area area, UUID ownerUUID) {
        this.area = area;
        this.ownerUUID = ownerUUID;
    }

    public Plot(Area area, EntityPlayer entityPlayer) {
        this(area, entityPlayer.getUniqueID());
    }

    public Plot(JsonObject jsonPlot) {
        this(new Area(jsonPlot.get("area").getAsJsonObject()),
                UUID.fromString(jsonPlot.get("ownerUUID").getAsString()));
    }

    public JsonObject toJsonObject() {
        JsonObject element = new JsonObject();
        element.addProperty("ownerUUID", ownerUUID.toString());
        element.add("area", area.toJson());
        return element;
    }

    public Area getArea() {
        return area;
    }

    public BlockPos getCornerMin() {
        return new BlockPos(area.getMinX(), area.getMinY(), area.getMinZ());
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public boolean hasBuildPermission(EntityPlayer entityPlayer) {
        return getOwnerUUID().equals(entityPlayer.getUniqueID());
    }

    public boolean explosionProtection() {
        return true;
    }

    public boolean entityProtection() {
        return true;
    }

    public BlockPos getCornerMax() {
        return new BlockPos(area.getMaxX(), area.getMaxY(), area.getMaxZ());
    }
}
