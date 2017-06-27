package com.kamildanak.minecraft.cornerstone.gui;

import com.kamildanak.minecraft.foamflower.gui.GuiHandler;
import com.kamildanak.minecraft.foamflower.inventory.DummyContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModGUIs {
    public static final GuiHandler CORNERSTONE_CREATE = new com.kamildanak.minecraft.foamflower.gui.GuiHandler("cornerstone-create") {
        @Override
        public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return new DummyContainer();
        }

        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return new GuiCornerstoneCreate(world, new BlockPos(x, y, z), player);
        }
    };

    public static final GuiHandler[] GUIS = {
            CORNERSTONE_CREATE
    };
}
