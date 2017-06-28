package com.kamildanak.minecraft.cornerstone.blocks;

import com.kamildanak.minecraft.cornerstone.data.PlayerData;
import com.kamildanak.minecraft.cornerstone.gui.ModGUIs;
import com.kamildanak.minecraft.cornerstone.items.ItemCornerstone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCornerstone extends AbstractBlock {
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
        ModGUIs.CORNERSTONE_CREATE.open(playerIn, worldIn, pos);
        return true;
    }

    @Override
    public Item getItemToRegister() {
        return new ItemCornerstone(this);
    }

    @Override
    public void onBlockClicked(World world, BlockPos blockPos, EntityPlayer entityplayer) {
        PlayerData playerData = PlayerData.get(entityplayer.getUniqueID());
        if (!(entityplayer instanceof EntityPlayerMP)) return;
        for (BlockPos pos : playerData.getCornerstoneUnderConstruction()) {
            if ((pos.getX() == blockPos.getX()) && (pos.getY() == blockPos.getY()) &&
                    (pos.getZ() == blockPos.getZ()))

            {
                dropBlockAsItem(world, blockPos, world.getBlockState(blockPos), 0);
                world.setBlockToAir(blockPos);
                playerData.removeCornerstone(pos);
                break;
            }
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

    }
}
