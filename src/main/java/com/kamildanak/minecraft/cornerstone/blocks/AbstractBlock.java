package com.kamildanak.minecraft.cornerstone.blocks;

import com.kamildanak.minecraft.cornerstone.Cornerstone;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class AbstractBlock extends Block {
    AbstractBlock(String name, Material material) {
        super(material);

        this.setRegistryName(Cornerstone.modID, name);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }
}
