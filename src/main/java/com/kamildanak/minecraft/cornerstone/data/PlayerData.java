package com.kamildanak.minecraft.cornerstone.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kamildanak.minecraft.cornerstone.Cornerstone;
import com.kamildanak.minecraft.cornerstone.filesystem.FileProvider;
import com.kamildanak.minecraft.cornerstone.filesystem.JsonFileReader;
import com.kamildanak.minecraft.cornerstone.filesystem.JsonFileWriter;
import com.kamildanak.minecraft.cornerstone.network.PacketDispatcher;
import com.kamildanak.minecraft.cornerstone.network.client.MessagePlayerData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {
    private static HashMap<Integer, HashMap<UUID, PlayerData>> objects = new HashMap<>();
    public UUID uuid;
    public int dimension;
    private ArrayList<BlockPos> cornerstoneUnderConstruction;
    private ArrayList<Plot> plots;
    private boolean changed;

    private PlayerData(UUID uuid, int dimension, boolean read) {
        this.uuid = uuid;
        this.dimension = dimension;
        cornerstoneUnderConstruction = new ArrayList<>();
        plots = new ArrayList<>();
        if (!read) return;
        try {
            this.read();
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nonnull
    public static PlayerData get(@Nonnull UUID playerUUID, int dimension) {
        HashMap<UUID, PlayerData> map = objects.computeIfAbsent(dimension, k -> new HashMap<>());
        return map.computeIfAbsent(playerUUID, k -> new PlayerData(playerUUID, dimension, true));
    }

    public static void clear() {
        objects.clear();
    }

    private static JsonArray blockPosArrayToJson(ArrayList<BlockPos> blockPosArrayList) {
        JsonArray jsonArray = new JsonArray();
        for (BlockPos pos : blockPosArrayList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("x", pos.getX());
            jsonObject.addProperty("y", pos.getY());
            jsonObject.addProperty("z", pos.getZ());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public static void writeAll() throws IOException {
        for (HashMap<UUID, PlayerData> map : objects.values()) {
            for (PlayerData playerData : map.values()) {
                playerData.writeIfChanged();
            }
        }
    }

    public void addCornerstone(BlockPos blockPos) {
        cornerstoneUnderConstruction.add(blockPos);
        sendUpdateToPlayer();
        changed = true;
    }

    public ArrayList<BlockPos> getCornerstoneUnderConstruction() {
        return cornerstoneUnderConstruction;
    }

    public ArrayList<Plot> getPlotsCorners() {
        return plots;
    }

    public void addPlot(Plot plot) {
        plots.add(plot);
        sendUpdateToPlayer();
        changed = true;
    }

    public boolean canPlaceCornerstoneAt(BlockPos posN) {
        BlockPos pos0, pos1, pos2;
        switch (cornerstoneUnderConstruction.size()) {
            case 0:
                return true;
            case 1:
                pos0 = cornerstoneUnderConstruction.get(0);
                if (pos0.getY() != posN.getY()) return false;
                if (pos0.getX() == posN.getX())
                    return Math.abs(pos0.getZ() - posN.getZ()) < Cornerstone.settings.getMaxPlotLength();
                if (pos0.getZ() == posN.getZ())
                    return Math.abs(pos0.getX() - posN.getX()) < Cornerstone.settings.getMaxPlotLength();
                return false;
            case 2:
                pos0 = cornerstoneUnderConstruction.get(0);
                pos1 = cornerstoneUnderConstruction.get(1);
                if (pos0.getY() != posN.getY() || pos1.getY() != posN.getY()) return false;
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
                if (posN.getX() == pos0.getX() && posN.getZ() == pos1.getZ())
                    return Math.abs(pos1.getX() - posN.getX()) < Cornerstone.settings.getMaxPlotLength() &&
                            Math.abs(pos0.getZ() - posN.getZ()) < Cornerstone.settings.getMaxPlotLength();
                if (posN.getZ() == pos0.getZ() && posN.getX() == pos1.getX())
                    return Math.abs(pos1.getZ() - posN.getZ()) < Cornerstone.settings.getMaxPlotLength() &&
                            Math.abs(pos0.getX() - posN.getX()) < Cornerstone.settings.getMaxPlotLength();
                return false;
            case 3:
                pos0 = cornerstoneUnderConstruction.get(0);
                pos1 = cornerstoneUnderConstruction.get(1);
                pos2 = cornerstoneUnderConstruction.get(2);
                if (pos0.getY() != posN.getY() || pos1.getY() != posN.getY() || pos2.getY() != posN.getY())
                    return false;
                if (posN.getX() == pos0.getX() && posN.getZ() == pos1.getZ())
                    return Math.abs(pos1.getX() - posN.getX()) < Cornerstone.settings.getMaxPlotLength() &&
                            Math.abs(pos0.getZ() - posN.getZ()) < Cornerstone.settings.getMaxPlotLength();
                if (posN.getZ() == pos0.getZ() && posN.getX() == pos1.getX())
                    return Math.abs(pos1.getZ() - posN.getZ()) < Cornerstone.settings.getMaxPlotLength() &&
                            Math.abs(pos0.getX() - posN.getX()) < Cornerstone.settings.getMaxPlotLength();
                if (posN.getX() == pos2.getX() && posN.getZ() == pos1.getZ())
                    return Math.abs(pos1.getX() - posN.getX()) < Cornerstone.settings.getMaxPlotLength() &&
                            Math.abs(pos2.getZ() - posN.getZ()) < Cornerstone.settings.getMaxPlotLength();
                if (posN.getZ() == pos2.getZ() && posN.getX() == pos1.getX())
                    return Math.abs(pos1.getZ() - posN.getZ()) < Cornerstone.settings.getMaxPlotLength() &&
                            Math.abs(pos2.getX() - posN.getX()) < Cornerstone.settings.getMaxPlotLength();
                if (posN.getX() == pos0.getX() && posN.getZ() == pos2.getZ())
                    return Math.abs(pos2.getX() - posN.getX()) < Cornerstone.settings.getMaxPlotLength() &&
                            Math.abs(pos0.getZ() - posN.getZ()) < Cornerstone.settings.getMaxPlotLength();
                if (posN.getZ() == pos0.getZ() && posN.getX() == pos2.getX())
                    return Math.abs(pos2.getZ() - posN.getZ()) < Cornerstone.settings.getMaxPlotLength() &&
                            Math.abs(pos0.getX() - posN.getX()) < Cornerstone.settings.getMaxPlotLength();
                return false;
        }
        return false;
    }

    public void removeCornerstone(BlockPos pos) {
        cornerstoneUnderConstruction.removeIf(blockPos ->
                (blockPos.getX() == pos.getX()) && (blockPos.getY() == pos.getY()) && (blockPos.getZ() == pos.getZ()));
        sendUpdateToPlayer();
        changed = true;
    }

    public void removePlot(Plot plot) {
        plots.removeIf(p -> p.getCornerMin() == plot.getCornerMin());
        sendUpdateToPlayer();
        changed = true;
    }

    public void sendUpdateToPlayer() {
        if (Cornerstone.minecraftServer == null) return;
        EntityPlayerMP playerByUUID = Cornerstone.minecraftServer.getPlayerList().getPlayerByUUID(uuid);
        if (playerByUUID == null) return;
        PacketDispatcher.sendTo(new MessagePlayerData(this, dimension), playerByUUID);
    }

    private void write() throws IOException {
        JsonObject obj = new JsonObject();

        obj.add("cornerstonesUC", blockPosArrayToJson(cornerstoneUnderConstruction));
        obj.add("plots", plotArrayToJson(plots));
        try (JsonFileWriter file = new JsonFileWriter(FileProvider.getFile(this))) {
            file.write(obj);
        }
        changed = false;
    }

    private JsonElement plotArrayToJson(ArrayList<Plot> plots) {
        JsonArray jsonArray = new JsonArray();
        for (Plot plot : plots) {
            JsonObject jsonObject = new JsonObject();
            BlockPos corner = plot.getCornerMin();
            BlockPos cornerUp = plot.getCornerMax();
            jsonObject.addProperty("minX", corner.getX());
            jsonObject.addProperty("minY", corner.getY());
            jsonObject.addProperty("minZ", corner.getZ());
            jsonObject.addProperty("maxX", cornerUp.getX());
            jsonObject.addProperty("maxY", cornerUp.getY());
            jsonObject.addProperty("maxZ", cornerUp.getZ());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private void read() throws IOException {
        changed = false;
        this.cornerstoneUnderConstruction = new ArrayList<>();
        JsonObject jsonObject = new JsonFileReader(FileProvider.getFile(this)).readJson();
        JsonElement cornerstonesElement = jsonObject.get("cornerstonesUC");
        if (cornerstonesElement != null) {
            JsonArray cornerstonesUC = cornerstonesElement.getAsJsonArray();
            for (JsonElement jsonCornerstone : cornerstonesUC) {
                JsonObject object = jsonCornerstone.getAsJsonObject();
                this.cornerstoneUnderConstruction.add(
                        new BlockPos(object.get("x").getAsInt(), object.get("y").getAsInt(), object.get("z").getAsInt()));
            }
        }

        JsonElement plotsElement = jsonObject.get("plots");
        if (plotsElement == null) return;
        JsonArray plots = plotsElement.getAsJsonArray();
        for (JsonElement plot : plots) {
            JsonObject object = plot.getAsJsonObject();
            this.plots.add(new Plot(new Area(object.get("minX").getAsInt(), object.get("minY").getAsInt(), object.get("minZ").getAsInt(),
                    object.get("maxX").getAsInt(), object.get("maxY").getAsInt(), object.get("maxZ").getAsInt()
            ), this.uuid));
        }
    }

    public void writeIfChanged() throws IOException {
        if (changed) write();
    }

    public void removeCornerstones() {
        cornerstoneUnderConstruction = new ArrayList<>();
    }
}
