package com.kamildanak.minecraft.cornerstone.network.client;

import com.kamildanak.minecraft.cornerstone.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

public class MessageNoInfo extends AbstractMessage.AbstractClientMessage<MessageNoInfo> {
    private BlockPos pos;
    private int dimension;

    public MessageNoInfo() {
    }

    public MessageNoInfo(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    @Override
    protected void read(PacketBuffer buffer) {
        pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        dimension = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) {
        buffer.writeInt(pos.getX());
        buffer.writeInt(pos.getY());
        buffer.writeInt(pos.getZ());
        buffer.writeInt(dimension);
    }

    @Override
    public void process(EntityPlayer player, Side side) {

    }
}
