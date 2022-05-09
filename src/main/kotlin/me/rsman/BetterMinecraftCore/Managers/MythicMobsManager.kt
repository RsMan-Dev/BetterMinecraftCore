package me.rsman.BetterMinecraftCore.Managers

import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.Listeners.MythicMobs.MythicMobsMobKillDropListener
import org.bukkit.event.Listener

object MythicMobsManager {
    val isMythicMobsInstalled
        get() = BetterMinecraftCore.instance.server.pluginManager.getPlugin("MythicMobs") != null


    fun getListeners() : List<Listener> = listOf(
            MythicMobsMobKillDropListener()
    )
}