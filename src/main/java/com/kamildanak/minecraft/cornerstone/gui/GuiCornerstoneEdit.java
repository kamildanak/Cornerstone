package com.kamildanak.minecraft.cornerstone.gui;

import com.kamildanak.minecraft.foamflower.gui.GuiScreenPlus;
import com.kamildanak.minecraft.foamflower.gui.elements.GuiExButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class GuiCornerstoneEdit extends GuiScreenPlus {
    public GuiCornerstoneEdit(World world, BlockPos blockPos, EntityPlayer player) {
        super(146, 110, "cornerstone:textures/gui/cornerstone-create.png");
        addChild(new GuiExButton(9, 36, 60, 20, "gui.cornerstone:gui_cornerstone_create.save") {
            @Override
            public void onClick() {
                //PacketDispatcher.sendToServer(new MessageActivateCornerstone(blockPos, inputHeight.getValue().intValue()));
                mc.player.closeScreen();
            }
        });
        addChild(new GuiExButton(76, 36, 60, 20, "gui.cornerstone:gui_cornerstone_create.cancel") {
            @Override
            public void onClick() {
                mc.player.closeScreen();
            }
        });
    }
}
