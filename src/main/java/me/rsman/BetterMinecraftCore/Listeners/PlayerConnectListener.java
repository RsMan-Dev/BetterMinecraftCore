package me.rsman.BetterMinecraftCore.Listeners;

import me.rsman.BetterMinecraftCore.Managers.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectListener implements Listener {

    @EventHandler
    public void onConnect(PlayerJoinEvent event){
        PlayerManager.getBaseAttributes(event.getPlayer().getUniqueId().toString(), true);
        PlayerManager.alterPlayerAttributesWithEquippedStuff(event.getPlayer());
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event){
        PlayerManager.playersAttributes.remove(event.getPlayer().getUniqueId().toString());
    }
}
