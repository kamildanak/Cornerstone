package com.kamildanak.minecraft.cornerstone.proxy;


import com.kamildanak.minecraft.cornerstone.network.PacketDispatcher;
import com.kamildanak.minecraft.cornerstone.network.client.MessageBasicInfo;
import com.kamildanak.minecraft.cornerstone.network.client.MessageNoInfo;
import com.kamildanak.minecraft.cornerstone.network.client.MessagePlayerData;
import com.kamildanak.minecraft.cornerstone.network.server.MessageActivateCornerstone;
import com.kamildanak.minecraft.cornerstone.network.server.MessageGetBasicInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Proxy {
    public void preInit() {
    }

    public void init() {
    }

    public void registerPackets() {
        PacketDispatcher.registerMessage(MessagePlayerData.class);
        PacketDispatcher.registerMessage(MessageActivateCornerstone.class);
        PacketDispatcher.registerMessage(MessageNoInfo.class);
        PacketDispatcher.registerMessage(MessageBasicInfo.class);
        PacketDispatcher.registerMessage(MessageGetBasicInfo.class);
    }

    /**
     * Returns a side-appropriate EntityPlayer for use during message handling
     */
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().player;
    }

    /**
     * Returns the current thread based on side during message handling,
     * used for ensuring that the message is being handled by the main thread
     */
    public IThreadListener getThreadFromContext(MessageContext ctx) {
        return ctx.getServerHandler().player.getServerWorld();
    }

    public boolean isSinglePlayer() {
        return false;
    }
}
