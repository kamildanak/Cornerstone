package com.kamildanak.minecraft.cornerstone.events;

import com.kamildanak.minecraft.enderpay.economy.Account;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;

public class EventHandler {
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerSaveToFile(PlayerEvent.SaveToFile event) {
        Account account = Account.get(event.getEntityPlayer());
        try {
            account.writeIfChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onChunkLoad(ChunkEvent.Load event) {

    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onChunkUnload(ChunkEvent.Unload event) {

    }
}
