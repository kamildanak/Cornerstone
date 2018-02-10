package com.kamildanak.minecraft.cornerstone.network.client;

import com.kamildanak.minecraft.cornerstone.data.Area;
import com.kamildanak.minecraft.cornerstone.data.ChunkPlotDataProvider;
import com.kamildanak.minecraft.cornerstone.data.Plot;
import com.kamildanak.minecraft.cornerstone.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class MessageBasicInfo extends AbstractMessage.AbstractClientMessage<MessageBasicInfo> {
    private int dimension;
    private Area area;
    private UUID ownerUUID;

    public MessageBasicInfo() {
    }

    public MessageBasicInfo(Plot plot, int dimension) {
        this.dimension = dimension;
        this.ownerUUID = plot.getOwnerUUID();
        area = plot.getArea();
    }

    @Override
    protected void read(PacketBuffer buffer) {
        dimension = buffer.readInt();
        int minX = buffer.readInt();
        int minY = buffer.readInt();
        int minZ = buffer.readInt();
        int maxX = buffer.readInt();
        int maxY = buffer.readInt();
        int maxZ = buffer.readInt();
        area = new Area(minX, minY, minZ, maxX, maxY, maxZ);
        ownerUUID = new UUID(buffer.readLong(), buffer.readLong());
    }

    @Override
    protected void write(PacketBuffer buffer) {
        buffer.writeInt(dimension);
        buffer.writeInt(area.getMinX());
        buffer.writeInt(area.getMinY());
        buffer.writeInt(area.getMinZ());
        buffer.writeInt(area.getMaxX());
        buffer.writeInt(area.getMaxY());
        buffer.writeInt(area.getMaxZ());
        buffer.writeLong(ownerUUID.getMostSignificantBits());
        buffer.writeLong(ownerUUID.getLeastSignificantBits());
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        Plot remotePlot = ChunkPlotDataProvider.getPlot(new BlockPos(area.getMinX(), area.getMinY(), area.getMinZ()), dimension);
        if (remotePlot == null) {
            ChunkPlotDataProvider.add(new Plot(area, ownerUUID), dimension);
        }
    }
}
