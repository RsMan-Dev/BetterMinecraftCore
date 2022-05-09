package me.rsman.BetterMinecraftCore.Managers

import org.bukkit.entity.Player
import me.rsman.BetterMinecraftCore.configs.containers.GlobalConfigContainer
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent

object ActionBarManager {
    private var actionBarDisplayFormat: String? = null
        private get() {
            if (field != null) return field
            field = GlobalConfigContainer.instance!!.action_bar_display_format
            return field
        }

    fun updateActionBar(player: Player, attributes: Map<String, Long>, mana: Long) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                *TextComponent.fromLegacyText(
                        PapiManager.parseText(player, actionBarDisplayFormat!!.replace("&", "§")
                                .replace("{health}", Math.round(player.health).toString() + "")
                                .replace("{health_max}", attributes["health"].toString() + "")
                                .replace("{defense}", attributes["defense"].toString() + "")
                                .replace("{mana}", mana.toString() + "")
                                .replace("{mana_max}", attributes["mana"].toString() + ""))
                )
        )
    }
}