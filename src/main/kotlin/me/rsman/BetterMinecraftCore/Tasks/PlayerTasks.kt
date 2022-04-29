package me.rsman.BetterMinecraftCore.Tasks

import me.rsman.BetterMinecraftCore.BetterMinecraftCore.Companion.instance
import org.bukkit.entity.Player
import me.rsman.BetterMinecraftCore.Managers.PlayerManager

object PlayerTasks {
    @JvmStatic
    fun updatePlayers() {
        instance.server.onlinePlayers.forEach { player: Player -> PlayerManager.updatePlayerAttributes(player) }
    }
}