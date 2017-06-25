package com.kamildanak.minecraft.cornerstone.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockCornerstone extends AbstractBlock {
    public BlockCornerstone(String name, Material material) {
        super(name, material);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
}
