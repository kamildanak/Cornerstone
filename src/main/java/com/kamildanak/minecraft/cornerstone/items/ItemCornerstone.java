package com.kamildanak.minecraft.cornerstone.items;

import com.kamildanak.minecraft.cornerstone.blocks.BlockCornerstone;
import com.kamildanak.minecraft.cornerstone.data.PlayerData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemCornerstone extends ItemBlock {
    public ItemCornerstone(BlockCornerstone block) {
        super(block);
        this.setRegistryName(block.getRegistryName());
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand,
                                      @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        BlockPos placePos = (block.isReplaceable(worldIn, pos)) ? pos : pos.offset(facing);
        PlayerData playerData = PlayerData.get(player.getUniqueID(), worldIn.provider.getDimension());
        if (!playerData.canPlaceCornerstoneAt(placePos)) return EnumActionResult.FAIL;
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
