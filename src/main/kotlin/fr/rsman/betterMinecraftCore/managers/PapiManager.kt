package fr.rsman.betterMinecraftCore.managers

import me.clip.placeholderapi.PlaceholderAPI
import fr.rsman.betterMinecraftCore.papi_expansion.PapiExpansion
import org.bukkit.entity.Player

object PapiManager {
    @Suppress("BooleanMethodIsAlwaysInverted")
    private val isPapiInstalled
        get() = fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.server.pluginManager.getPlugin("PlaceholderApi") != null

    fun parseText(player: Player?, text: String) : String {
        return if (isPapiInstalled)  PlaceholderAPI.setPlaceholders(player, text) else text
    }

    fun registerExpansion(){
        if(isPapiInstalled) PapiExpansion(fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance).register()
    }
}