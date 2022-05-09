package me.rsman.BetterMinecraftCore.PapiExpansion

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.Managers.PlayerManager
import org.bukkit.OfflinePlayer
import kotlin.math.roundToInt

class PapiExpansion(val plugin: BetterMinecraftCore): PlaceholderExpansion() {

    override fun getIdentifier() = "bmc"

    override fun getAuthor() = "RsMan"

    override fun getVersion() = plugin.description.version

    override fun persist() = true

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        if(player != null && player.player != null){
            val player = player.player ?: return null
            val attrs: Map<String, Long> = PlayerManager.getAttributes(player.uniqueId.toString())
            for(attr in attrs.entries) if(params.lowercase() == attr.key)  return attr.value.toString()
            if(params.lowercase() == "health_current" && player.isOnline && player.player != null)
                return player.player!!.health.toString()
            if(params.lowercase() == "health_current_rounded" && player.isOnline && player.player != null)
                return player.player!!.health.roundToInt().toString()
            if(params.lowercase() == "mana_current" && player.isOnline && player.player != null)
                return PlayerManager.playersAttributes[player.uniqueId.toString()]?.get("currentMana")?.toString()
        }
        return null
    }
}