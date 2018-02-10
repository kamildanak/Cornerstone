package com.kamildanak.minecraft.cornerstone.events;

import com.kamildanak.minecraft.cornerstone.Cornerstone;
import com.kamildanak.minecraft.cornerstone.data.PlayerData;
import com.kamildanak.minecraft.cornerstone.tileentity.TileEntityCornerstone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.IOException;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Cornerstone.modID)
public class PlayerEventHandler {
    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        checkIfCornerstonesExists(event.player, event.player.dimension);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        checkIfCornerstonesExists(event.player, event.player.dimension);
    }

    @SubscribeEvent
    public static void onPlayerSaveToFile(net.minecraftforge.event.entity.player.PlayerEvent.SaveToFile event) {
        for (int dimension : DimensionManager.getStaticDimensionIDs()) {
            try {
                PlayerData playerData = PlayerData.get(UUID.fromString(event.getPlayerUUID()), dimension);
                playerData.writeIfChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkIfCornerstonesExists(EntityPlayer player, int toDim) {
        PlayerData playerData = PlayerData.get(player.getUniqueID(), toDim);
        playerData.getCornerstoneUnderConstruction().removeIf(blockPos ->
        {
            TileEntity tileEntity = player.world.getTileEntity(blockPos);
            return tileEntity instanceof TileEntityCornerstone &&
                    ((TileEntityCornerstone) tileEntity).getOwnerUUID() == player.getUniqueID();
        });
        playerData.sendUpdateToPlayer();
    }
}
