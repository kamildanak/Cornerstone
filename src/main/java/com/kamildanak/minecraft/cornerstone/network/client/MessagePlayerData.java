package com.kamildanak.minecraft.cornerstone.network.client;

import com.kamildanak.minecraft.cornerstone.data.PlayerData;
import com.kamildanak.minecraft.cornerstone.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;
import java.util.ArrayList;

public class MessagePlayerData extends AbstractMessage.AbstractClientMessage<MessagePlayerData> {
    private int dim;
    private ArrayList<BlockPos> cornerstoneUnderConstruction;


    @SuppressWarnings("unused")
    public MessagePlayerData() {
    }

    public MessagePlayerData(PlayerData playerData, int dim) {
        this.dim = dim;
        this.cornerstoneUnderConstruction = new ArrayList<>();
        this.cornerstoneUnderConstruction = playerData.getCornerstoneUnderConstruction();
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        cornerstoneUnderConstruction = new ArrayList<>();
        dim = buffer.readInt();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            cornerstoneUnderConstruction.add(BlockPos.fromLong(buffer.readLong()));
        }
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(dim);
        buffer.writeInt(cornerstoneUnderConstruction.size());
        for (BlockPos pos : cornerstoneUnderConstruction) {
            buffer.writeLong(pos.toLong());
        }
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        ArrayList<BlockPos> cornerstones = PlayerData.get(player.getUniqueID(), dim).getCornerstoneUnderConstruction();
        cornerstones.clear();
        cornerstones.addAll(cornerstoneUnderConstruction);
    }
}