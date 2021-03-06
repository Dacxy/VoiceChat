/*
 * Decompiled with CFR 0_118.
 *
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.EntityJoinWorldEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package net.gliby.voicechat.client.networking.game;

import net.gliby.voicechat.client.VoiceChatClient;
import net.gliby.voicechat.client.sound.ClientStreamManager;
import net.gliby.voicechat.common.PlayerProxy;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class ClientEventHandler {
    VoiceChatClient voiceChat;

    public ClientEventHandler(VoiceChatClient voiceChatClient) {
        this.voiceChat = voiceChatClient;
    }

    @SubscribeEvent
    public void entityJoinWorld(final EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    if (event.getEntity() instanceof EntityOtherPlayerMP) {
                        PlayerProxy proxy;
                        EntityOtherPlayerMP player = (EntityOtherPlayerMP) event.getEntity();
                        if (!VoiceChatClient.getSoundManager().playersMuted.contains(player.getEntityId())) {
                            VoiceChatClient.getSoundManager();
                            for (Map.Entry<Integer, String> entry : ClientStreamManager.playerMutedData.entrySet()) {
                                Integer key = entry.getKey();
                                String value = entry.getValue();
                                if (!value.equals(player.getName())) continue;
                                VoiceChatClient.getSoundManager().playersMuted.remove(key);
                                VoiceChatClient.getSoundManager();
                                ClientStreamManager.playerMutedData.remove(key);
                                VoiceChatClient.getSoundManager().playersMuted.add(player.getEntityId());
                                VoiceChatClient.getSoundManager();
                                ClientStreamManager.playerMutedData.put(player.getEntityId(), player.getName());
                                break;
                            }
                        }
                        if ((proxy = VoiceChatClient.getSoundManager().playerData.get(player.getEntityId())) != null) {
                            proxy.setPlayer(player);
                            proxy.setName(player.getName());
                            proxy.usesEntity = true;
                        }
                    }
                }
            }, "Entity Join Process").start();
        }
    }

}

