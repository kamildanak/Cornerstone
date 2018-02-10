package com.kamildanak.minecraft.cornerstone.proxy;

import com.kamildanak.minecraft.cornerstone.gui.hud.HintHUD;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@SuppressWarnings("unused")
public class ProxyClient extends Proxy {
    private Minecraft mc;

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        mc = Minecraft.getMinecraft();
        MinecraftForge.EVENT_BUS.register(new HintHUD(Minecraft.getMinecraft()));
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (ctx.side.isClient() ? mc.player : super.getPlayerEntity(ctx));
    }

    @Override
    public IThreadListener getThreadFromContext(MessageContext ctx) {
        return (ctx.side.isClient() ? mc : super.getThreadFromContext(ctx));
    }

    @Override
    public boolean isSinglePlayer() {
        return Minecraft.getMinecraft().isSingleplayer();
    }
}
