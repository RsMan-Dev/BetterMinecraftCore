package fr.rsman.betterMinecraftCore.managers

import fr.rsman.betterMinecraftCore.listeners.mythic_mobs.MythicMobsMobKillDropListener
import org.bukkit.event.Listener

object MythicMobsManager {
    @Suppress("BooleanMethodIsAlwaysInverted")
    val isMythicMobsInstalled
        get() = fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.server.pluginManager.getPlugin("MythicMobs") != null


    fun getListeners() : List<Listener> = if(isMythicMobsInstalled) listOf(
            MythicMobsMobKillDropListener()
    ) else listOf()
}