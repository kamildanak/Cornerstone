package com.kamildanak.minecraft.cornerstone.blocks;

import com.kamildanak.minecraft.cornerstone.Cornerstone;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public abstract class AbstractBlockContainer extends BlockContainer implements IBlockItemRegister {
    protected AbstractBlockContainer(String name, Material materialIn) {
        super(materialIn);

        this.setRegistryName(Cornerstone.modID, name);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

    @Override
    public Item getItemToRegister() {
        return new ItemBlock(this).setRegistryName(this.getRegistryName());
    }
}
