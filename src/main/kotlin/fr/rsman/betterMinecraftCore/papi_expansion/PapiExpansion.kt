package fr.rsman.betterMinecraftCore.papi_expansion

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import fr.rsman.betterMinecraftCore.managers.PlayerManager
import org.bukkit.OfflinePlayer
import kotlin.math.roundToInt

class PapiExpansion(val plugin: fr.rsman.betterMinecraftCore.BetterMinecraftCore): PlaceholderExpansion() {

    override fun getIdentifier() = "bmc"

    override fun getAuthor() = "RsMan"

    override fun getVersion() = plugin.description.version

    override fun persist() = true

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        val player = player?.player ?: return null
        val attrs: Map<String, Long> = PlayerManager.getAttributes(player.uniqueId.toString())
        for(attr in attrs.entries) if(params.lowercase() == attr.key)  return attr.value.toString()
        if(player.isOnline && player.player != null){
            when(params.lowercase()){
                "health_current" -> return player.player!!.health.toString()
                "health_current_rounded" -> return player.player!!.health.roundToInt().toString()
                "mana_current" -> return PlayerManager.playersAttributes[player.uniqueId.toString()]?.get("currentMana")?.toString()
            }
        }
        return null
    }
}