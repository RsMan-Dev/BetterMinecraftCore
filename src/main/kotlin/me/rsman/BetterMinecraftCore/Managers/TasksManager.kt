package me.rsman.BetterMinecraftCore.Managers

import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import java.lang.Runnable
import me.rsman.BetterMinecraftCore.Tasks.PlayerTasks
import org.bukkit.plugin.Plugin

object TasksManager {
    fun registerAllTasks() {
        val scheduler = BetterMinecraftCore.instance.server.scheduler
        val plugin: Plugin = BetterMinecraftCore.instance
        scheduler.runTaskTimerAsynchronously(plugin, Runnable { PlayerTasks.updatePlayers() }, 0, 10)
    }
}