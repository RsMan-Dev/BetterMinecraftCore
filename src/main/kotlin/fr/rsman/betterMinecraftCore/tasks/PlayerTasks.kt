package fr.rsman.betterMinecraftCore.tasks

import fr.rsman.betterMinecraftCore.BetterMinecraftCore.Companion.instance
import org.bukkit.entity.Player
import fr.rsman.betterMinecraftCore.managers.PlayerManager

object PlayerTasks {
    @JvmStatic
    fun updatePlayers() {
        instance.server.onlinePlayers.forEach { player: Player -> PlayerManager.updatePlayerAttributes(player) }
    }
}