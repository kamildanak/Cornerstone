package com.kamildanak.minecraft.cornerstone.blocks;

import com.kamildanak.minecraft.cornerstone.Cornerstone;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public abstract class AbstractBlock extends Block {
    AbstractBlock(String name, Material material) {
        super(material);

        this.setRegistryName(Cornerstone.modID, name);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

    public Item getItemToRegister() {
        return new ItemBlock(this).setRegistryName(this.getRegistryName());
    }
}
