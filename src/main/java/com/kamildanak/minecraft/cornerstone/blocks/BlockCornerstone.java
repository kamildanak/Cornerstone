package com.kamildanak.minecraft.cornerstone.blocks;

import com.kamildanak.minecraft.cornerstone.gui.ModGUIs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCornerstone extends AbstractBlock {
    public BlockCornerstone(String name, Material material) {
        super(name, material);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ModGUIs.CORNERSTONE_CREATE.open(playerIn, worldIn, pos);
        return true;
    }
}
