package fr.rsman.betterMinecraftCore.listeners

import fr.rsman.betterMinecraftCore.managers.PlayerManager
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerConnectListener : Listener {
    @EventHandler
    fun onConnect(event: PlayerJoinEvent) {
        PlayerManager.getBaseAttributes(event.player.uniqueId.toString(), true)
        PlayerManager.alterPlayerAttributesWithEquippedStuff(event.player)
    }

    @EventHandler
    fun onDisconnect(event: PlayerQuitEvent) {
        PlayerManager.playersAttributes.remove(event.player.uniqueId.toString())
    }
}