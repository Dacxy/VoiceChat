/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.Mod$Instance
 *  net.minecraftforge.fml.common.SidedProxy
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent
 *  net.minecraftforge.fml.common.event.FMLServerStartedEvent
 *  net.minecraftforge.fml.common.event.FMLServerStartingEvent
 *  net.minecraftforge.fml.common.event.FMLServerStoppedEvent
 *  net.minecraftforge.fml.common.network.NetworkRegistry
 *  net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
 *  net.minecraftforge.fml.relauncher.Side
 *  org.apache.logging.log4j.Logger
 */
package net.gliby.voicechat;

import net.gliby.voicechat.client.VoiceChatClient;
import net.gliby.voicechat.common.VoiceChatServer;
import net.gliby.voicechat.common.networking.packets.MinecraftClientEntityDataPacket;
import net.gliby.voicechat.common.networking.packets.MinecraftClientEntityPositionPacket;
import net.gliby.voicechat.common.networking.packets.MinecraftClientVoiceAuthenticatedServer;
import net.gliby.voicechat.common.networking.packets.MinecraftClientVoiceEndPacket;
import net.gliby.voicechat.common.networking.packets.MinecraftClientVoicePacket;
import net.gliby.voicechat.common.networking.packets.MinecraftClientVoiceServerPacket;
import net.gliby.voicechat.common.networking.packets.MinecraftServerVoiceEndPacket;
import net.gliby.voicechat.common.networking.packets.MinecraftServerVoicePacket;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod(modid="gvc", name="Gliby's Voice Chat Mod", version="0.6.1", acceptedMinecraftVersions="[1.8]")
public class VoiceChat {
    @Mod.Instance
    public static VoiceChat instance;
    @SidedProxy(modId="gvc", clientSide="net.gliby.voicechat.client.VoiceChatClient", serverSide="net.gliby.voicechat.common.VoiceChatServer")
    public static VoiceChatServer proxy;
    public static SimpleNetworkWrapper DISPATCH;
    public static final String MOD_ID = "gvc";

    public static SimpleNetworkWrapper getDispatcher() {
        return DISPATCH;
    }

    public static VoiceChat getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return VoiceChatServer.getLogger();
    }

    public static VoiceChatClient getProxyInstance() {
        return proxy instanceof VoiceChatClient ? (VoiceChatClient)proxy : proxy;
    }

    public static VoiceChatServer getServerInstance() {
        return proxy;
    }

    public static synchronized VoiceChat getSynchronizedInstance() {
        return instance;
    }

    public static synchronized VoiceChatClient getSynchronizedProxyInstance() {
        return (VoiceChatClient)proxy;
    }

    public static synchronized VoiceChatClient getSynchronizedServerInstance() {
        return (VoiceChatClient)proxy;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.initMod(this, event);
    }

    @Mod.EventHandler
    public void initServer(FMLServerStartedEvent event) {
        proxy.initServer(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInitMod(this, event);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.registerNetwork();
        proxy.commonInit(event);
        proxy.preInitClient(event);
    }

    @Mod.EventHandler
    public void preInitServer(FMLServerStartingEvent event) {
        proxy.preInitServer(event);
    }

    @Mod.EventHandler
    public void aboutToStartServer(FMLServerAboutToStartEvent event) {
        proxy.aboutToStartServer(event);
    }

    private void registerNetwork() {
        DISPATCH = NetworkRegistry.INSTANCE.newSimpleChannel("GVC");
        DISPATCH.registerMessage(MinecraftServerVoicePacket.class, MinecraftServerVoicePacket.class, 1, Side.SERVER);
        DISPATCH.registerMessage(MinecraftServerVoiceEndPacket.class, MinecraftServerVoiceEndPacket.class, 2, Side.SERVER);
        DISPATCH.registerMessage(MinecraftClientVoiceEndPacket.class, MinecraftClientVoiceEndPacket.class, 9, Side.SERVER);
        DISPATCH.registerMessage(MinecraftClientVoicePacket.class, MinecraftClientVoicePacket.class, 3, Side.CLIENT);
        DISPATCH.registerMessage(MinecraftClientEntityDataPacket.class, MinecraftClientEntityDataPacket.class, 4, Side.CLIENT);
        DISPATCH.registerMessage(MinecraftClientEntityPositionPacket.class, MinecraftClientEntityPositionPacket.class, 5, Side.CLIENT);
        DISPATCH.registerMessage(MinecraftClientVoiceServerPacket.class, MinecraftClientVoiceServerPacket.class, 6, Side.CLIENT);
        DISPATCH.registerMessage(MinecraftClientVoiceAuthenticatedServer.class, MinecraftClientVoiceAuthenticatedServer.class, 7, Side.CLIENT);
    }

    @Mod.EventHandler
    public void stopServer(FMLServerStoppedEvent event) {
        proxy.stop();
        VoiceChat.getLogger().info("Stopped Voice Server.");
    }
}

