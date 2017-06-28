package com.kamildanak.minecraft.cornerstone.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.UUID;

public class TileEntityCornerstone extends TileEntity {
    private boolean activated;
    private String ownerName;
    private UUID ownerUUID;

    public TileEntityCornerstone() {
        activated = false;
        ownerName = "";
        ownerUUID = new UUID(0, 0);
    }

    public TileEntityCornerstone(EntityPlayer entityPlayer) {
        this();
        ownerName = entityPlayer.getName();
        ownerUUID = entityPlayer.getUniqueID();
    }

    public void markBlockForUpdate(BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, blockState, blockState, 3);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        activated = nbttagcompound.getBoolean("activated");
        ownerName = nbttagcompound.getString("ownerName");
        ownerUUID = nbttagcompound.getUniqueId("ownerUUID");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setBoolean("activated", activated);
        nbttagcompound.setString("ownerName", ownerName);
        nbttagcompound.setUniqueId("ownerUUID", ownerUUID);
        return super.writeToNBT(nbttagcompound);
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound updateTag = super.getUpdateTag();
        writeToNBT(updateTag);
        return updateTag;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new SPacketUpdateTileEntity(pos, 1, var1);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String name) {
        ownerName = name;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }
}
