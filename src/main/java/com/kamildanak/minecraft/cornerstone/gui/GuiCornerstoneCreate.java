package com.kamildanak.minecraft.cornerstone.gui;

import com.kamildanak.minecraft.cornerstone.network.PacketDispatcher;
import com.kamildanak.minecraft.cornerstone.network.server.MessageActivateCornerstone;
import com.kamildanak.minecraft.foamflower.gui.GuiScreenPlus;
import com.kamildanak.minecraft.foamflower.gui.elements.GuiEditBigInteger;
import com.kamildanak.minecraft.foamflower.gui.elements.GuiExButton;
import com.kamildanak.minecraft.foamflower.gui.elements.GuiLabel;
import com.kamildanak.minecraft.foamflower.gui.layouts.LinearLayout;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.math.BigInteger;

public class GuiCornerstoneCreate extends GuiScreenPlus {
    private GuiEditBigInteger inputHeight;

    public GuiCornerstoneCreate(World world, BlockPos blockPos, EntityPlayer player) {
        super(146, 110, "cornerstone:textures/gui/cornerstone-create.png");

        GuiExButton buttonCreate = new GuiExButton(9, 36, 60, 20, "gui.cornerstone:gui_cornerstone_create.create") {
            @Override
            public void onClick() {
                PacketDispatcher.sendToServer(new MessageActivateCornerstone(blockPos, inputHeight.getValue().intValue()));
                mc.player.closeScreen();
            }
        };
        GuiExButton buttonCancel = new GuiExButton(76, 36, 60, 20, "gui.cornerstone:gui_cornerstone_create.cancel") {
            @Override
            public void onClick() {
                mc.player.closeScreen();
            }
        };

        LinearLayout buttons = new LinearLayout(this, 0, 0, true, buttonCreate, buttonCancel);
        addChild(new LinearLayout(this, 5, 5, false,
                new GuiLabel(0, 0, "Plot height:"),
                inputHeight = new GuiEditBigInteger(0, 0, 120, 20,
                        BigInteger.ZERO, BigInteger.valueOf(256 - blockPos.getY())),
                //new GuiCheckBox(this,"Fire protection", false),
                //new GuiCheckBox(this,"Fluid protection", false),
                new GuiLabel(0, 0, "Rent cost: 452 credits"),
                buttons));
    }
}
