package com.kamildanak.minecraft.cornerstone.blocks;

import net.minecraft.block.material.Material;

public class ModBlocks {
    public static final AbstractBlock cornerstone = new BlockCornerstone("cornerstone", Material.ROCK);
    public static final AbstractBlock[] BLOCKS = {
            cornerstone
    };
}
