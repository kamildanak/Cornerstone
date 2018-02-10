package com.kamildanak.minecraft.cornerstone.network.server;

import com.kamildanak.minecraft.cornerstone.data.Area;
import com.kamildanak.minecraft.cornerstone.data.ChunkPlotDataProvider;
import com.kamildanak.minecraft.cornerstone.data.PlayerData;
import com.kamildanak.minecraft.cornerstone.data.Plot;
import com.kamildanak.minecraft.cornerstone.network.AbstractMessage;
import com.kamildanak.minecraft.cornerstone.tileentity.TileEntityCornerstone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

import static com.kamildanak.minecraft.cornerstone.Utils.markBlockForUpdate;

public class MessageActivateCornerstone extends AbstractMessage.AbstractServerMessage<MessageActivateCornerstone> {
    private BlockPos blockPos;
    private int height;

    @SuppressWarnings("unused")
    public MessageActivateCornerstone() {
    }

    public MessageActivateCornerstone(BlockPos blockPos, int height) {
        this.blockPos = blockPos;
        this.height = height;
    }

    @Override
    protected void read(PacketBuffer buffer) {
        blockPos = BlockPos.fromLong(buffer.readLong());
        height = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) {
        buffer.writeLong(blockPos.toLong());
        buffer.writeInt(height);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        World world = player.world;
        PlayerData playerData = PlayerData.get(player.getUniqueID(), world.provider.getDimension());
        ArrayList<BlockPos> cornerstones = playerData.getCornerstoneUnderConstruction();
        TileEntityCornerstone[] tec = new TileEntityCornerstone[4];
        if (cornerstones.size() != 4) return;
        for (int i = 0; i < 4; i++) {
            TileEntity te = world.getTileEntity(cornerstones.get(i));
            if (!(te instanceof TileEntityCornerstone)) return;
            else tec[i] = (TileEntityCornerstone) te;
        }
        for (int i = 0; i < 4; i++) {
            tec[i].setActivated(true);
            tec[i].setConnectedCornerstones(cornerstones.toArray(new BlockPos[4]));
            markBlockForUpdate(world, cornerstones.get(i));
        }
        playerData.removeCornerstones();

        int minX, minY, minZ, maxX, maxY, maxZ;
        BlockPos pos = cornerstones.get(0);
        minX = maxX = pos.getX();
        minY = pos.getY();
        maxY = minY + height;
        minZ = maxZ = pos.getZ();
        for (int i = 1; i < 4; i++) {
            pos = cornerstones.get(i);
            if (pos.getX() < minX) minX = pos.getX();
            if (pos.getY() < minY) minY = pos.getY();
            if (pos.getZ() < minZ) minZ = pos.getZ();
            if (pos.getX() > maxX) maxX = pos.getX();
            if (pos.getY() > maxY) maxY = pos.getY();
            if (pos.getZ() > maxZ) maxZ = pos.getZ();
        }
        Area area = new Area(minX, minY, minZ, maxX, maxY, maxZ);
        Plot plot = new Plot(area, player);
        ChunkPlotDataProvider.add(plot, world.provider.getDimension());
    }
}
