package com.kamildanak.minecraft.cornerstone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;

public class BlockCornerstone extends Block {

    public BlockCornerstone(String name) {
        super(Material.ROCK);
        register(name);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    private void register(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        ForgeRegistries.BLOCKS.register(this);
        ForgeRegistries.ITEMS.register(new ItemBlock(this).setUnlocalizedName(name).setRegistryName(name).setMaxDamage(0));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

}
