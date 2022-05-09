import me.clip.placeholderapi.PlaceholderAPI
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.PapiExpansion.PapiExpansion
import org.bukkit.entity.Player

object PapiManager {
    val isPapiInstalled
        get() = BetterMinecraftCore.instance.server.pluginManager.getPlugin("PlaceholderApi") != null

    fun parseText(player: Player?, text: String) : String {
        return if (isPapiInstalled)  PlaceholderAPI.setPlaceholders(player, text) else text
    }

    fun registerExpansion(){
        if(isPapiInstalled) PapiExpansion(BetterMinecraftCore.instance).register()
    }
}