package com.kamildanak.minecraft.cornerstone.network.server;

import com.kamildanak.minecraft.cornerstone.data.ChunkPlotDataProvider;
import com.kamildanak.minecraft.cornerstone.data.Plot;
import com.kamildanak.minecraft.cornerstone.network.AbstractMessage;
import com.kamildanak.minecraft.cornerstone.network.PacketDispatcher;
import com.kamildanak.minecraft.cornerstone.network.client.MessageBasicInfo;
import com.kamildanak.minecraft.cornerstone.network.client.MessageNoInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

public class MessageGetBasicInfo extends AbstractMessage.AbstractServerMessage<MessageGetBasicInfo> {
    private BlockPos pos;
    private int dimension;

    public MessageGetBasicInfo() {
    }

    public MessageGetBasicInfo(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    @Override
    protected void read(PacketBuffer buffer) {
        pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    @Override
    protected void write(PacketBuffer buffer) {
        buffer.writeInt(pos.getX());
        buffer.writeInt(pos.getY());
        buffer.writeInt(pos.getZ());
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        Plot remotePlot = ChunkPlotDataProvider.getPlot(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), dimension);
        if (remotePlot != null) {
            PacketDispatcher.sendTo(new MessageBasicInfo(remotePlot, dimension), (EntityPlayerMP) player);
        }
        PacketDispatcher.sendTo(new MessageNoInfo(pos, dimension), (EntityPlayerMP) player);
    }
}
