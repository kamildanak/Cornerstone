package com.kamildanak.minecraft.cornerstone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {
    public static final Block cornerstone = new BlockCornerstone("cornerstone", Material.ROCK);
    public static final Block[] BLOCKS = {
            cornerstone
    };
}
