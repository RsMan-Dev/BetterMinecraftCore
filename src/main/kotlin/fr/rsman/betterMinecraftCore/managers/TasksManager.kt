package fr.rsman.betterMinecraftCore.managers

import java.lang.Runnable
import fr.rsman.betterMinecraftCore.tasks.PlayerTasks
import org.bukkit.plugin.Plugin

object TasksManager {
    fun registerAllTasks() {
        val scheduler = fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.server.scheduler
        val plugin: Plugin = fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance
        scheduler.runTaskTimerAsynchronously(plugin, Runnable { PlayerTasks.updatePlayers() }, 0, 10)
    }
}