package me.rsman.BetterMinecraftCore.CommandKits

import co.aikar.commands.BaseCommand
import org.bukkit.entity.Player
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.*
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager
import me.rsman.BetterMinecraftCore.Managers.Command.Lang.MessageKeys
import me.rsman.BetterMinecraftCore.Managers.PlayerManager

@CommandAlias("bmc|betterminecraftcore")
@Subcommand("player|p")
class PlayerCommands : BaseCommand() {
    //all config wiped out, using config for database data is useless.
    private val commandManager = CommandManager.get()
    @Subcommand("getAttribute")
    @CommandCompletion("@attribute @players")
    @CommandPermission("hc.player.attribute.get")
    @Description("{@@bmc.command.description.attribute.get}")
    @Syntax("<attr> <player>")
    fun onGetAttr(playerSender: Player?, @Values("@attribute") attr: String?, @Optional @Values("@players") player: OnlinePlayer?) {
        val playerChecked = if (player != null) player.getPlayer() else playerSender!!
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        issuerSender.sendInfo(MessageKeys.PLAYER_ATTRIBUTE, "{player}", playerChecked.displayName, "{attr}", attr, "{val}", PlayerManager.getAttributes(playerChecked.uniqueId.toString())[attr].toString() + "")
    }

    @Subcommand("setBaseAttribute")
    @CommandCompletion("@attribute <value> @players")
    @CommandPermission("hc.player.attribute.set")
    @Description("{@@bmc.command.description.attribute.set}")
    @Syntax("<attr> <value> <player>")
    fun onSetBaseAttr(playerSender: Player?, @Values("@attribute") attr: String?, value: Long, @Optional @Values("@players") player: OnlinePlayer?) {
        val playerChecked = if (player != null) player.getPlayer() else playerSender!!
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        PlayerManager.setBaseAttribute(playerChecked.uniqueId.toString(), attr ?: return , value)
        issuerSender.sendInfo(MessageKeys.PLAYER_BASE_ATTRIBUTE_SET, "{player}", playerChecked.displayName, "{attr}", attr, "{val}", value.toString() + "")
    }
}