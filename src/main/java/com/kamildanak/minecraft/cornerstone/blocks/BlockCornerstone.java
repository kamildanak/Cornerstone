package com.kamildanak.minecraft.cornerstone.blocks;

import com.kamildanak.minecraft.cornerstone.data.ChunkPlotDataProvider;
import com.kamildanak.minecraft.cornerstone.data.PlayerData;
import com.kamildanak.minecraft.cornerstone.gui.ModGUIs;
import com.kamildanak.minecraft.cornerstone.items.ItemCornerstone;
import com.kamildanak.minecraft.cornerstone.tileentity.TileEntityCornerstone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCornerstone extends AbstractBlockContainer {
    public BlockCornerstone(String name, Material material) {
        super(name, material);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHardness(0.3F);
        setResistance(6000000.0F);
        setBlockUnbreakable();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        playerIn.getGameProfile().getId();
        TileEntity te = worldIn.getTileEntity(pos);
        if (worldIn.isRemote && te instanceof TileEntityCornerstone && !((TileEntityCornerstone) te).isActivated() &&
                PlayerData.get(playerIn.getUniqueID(),
                        worldIn.provider.getDimension()).getCornerstoneUnderConstruction().size() >= 4) {
            ModGUIs.CORNERSTONE_CREATE.open(playerIn, worldIn, pos);
        }
        return true;
    }

    @Override
    public Item getItemToRegister() {
        return new ItemCornerstone(this);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState state, EntityLivingBase entityLiving,
                                @Nonnull ItemStack stack) {
        if (entityLiving instanceof EntityPlayerMP) {
            TileEntityCornerstone e = new TileEntityCornerstone((EntityPlayer) entityLiving);
            world.setTileEntity(blockPos, e);
            PlayerData.get(entityLiving.getUniqueID(), world.provider.getDimension()).addCornerstone(blockPos);
        }
    }

    @Override
    public void onBlockClicked(World world, BlockPos blockPos, EntityPlayer entityplayer) {
        TileEntityCornerstone tileEntity = (TileEntityCornerstone) world.getTileEntity(blockPos);
        if (tileEntity == null)
            return;
        if (!tileEntity.isActivated() || tileEntity.getOwnerUUID().equals(entityplayer.getUniqueID())) {
            dropBlockAsItem(world, blockPos, world.getBlockState(blockPos), 0);
            world.setBlockToAir(blockPos);
        }
    }


    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
    public void breakBlock(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (worldIn.isRemote) return;
        TileEntityCornerstone tileEntityCornerstone = (TileEntityCornerstone) worldIn.getTileEntity(pos);

        if (tileEntityCornerstone == null)
            return;

        if (tileEntityCornerstone.isActivated()) {
            for (BlockPos connectedPos : tileEntityCornerstone.getConnectedCornerstones()) {
                TileEntityCornerstone tileEntityCornerstone2 = (TileEntityCornerstone) worldIn.getTileEntity(connectedPos);
                if (tileEntityCornerstone2 == null) continue;
                tileEntityCornerstone2.setConnectedCornerstones(new BlockPos[4]);
                tileEntityCornerstone2.setActivated(false);
                ChunkPlotDataProvider.remove(connectedPos, worldIn.provider.getDimension());
            }
        } else {
            PlayerData.get(tileEntityCornerstone.getOwnerUUID(), worldIn.provider.getDimension()).removeCornerstone(pos);
        }
        worldIn.removeTileEntity(pos);
        super.breakBlock(worldIn, pos, state);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta) {
        return new TileEntityCornerstone();
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
