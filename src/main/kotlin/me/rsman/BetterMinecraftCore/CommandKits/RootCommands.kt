package me.rsman.BetterMinecraftCore.CommandKits

import me.rsman.BetterMinecraftCore.BetterMinecraftCore.Companion.instance
import co.aikar.commands.BaseCommand
import org.bukkit.entity.Player
import co.aikar.commands.CommandHelp
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.*
import me.rsman.BetterMinecraftCore.configs.ConfigLoader
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer
import java.util.Locale
import java.io.IOException
import org.bukkit.configuration.InvalidConfigurationException
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager
import me.rsman.BetterMinecraftCore.Managers.Command.Lang.MessageKeys
import me.rsman.BetterMinecraftCore.Managers.PlayerManager

@CommandAlias("bmc|betterminecraftcore")
class RootCommands : BaseCommand() {
    private val commandManager = CommandManager.get()
    @HelpCommand
    @CommandCompletion("")
    @Syntax("")
    fun doHelp(playerSender: Player?, help: CommandHelp) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        issuerSender.sendInfo(MessageKeys.BMC_HELP)
        help.showHelp()
    }

    @Subcommand("reload")
    @CommandPermission("bmc.admin")
    @Description("{@@bmc.command.description.reload}")
    fun onReload(playerSender: Player?) {
        ConfigLoader.init()
        BmcCraftContainer.registerCrafts()
        try {
            commandManager?.locales?.loadYamlLanguageFile("lang/command.yml", Locale.ENGLISH)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
        instance.server.onlinePlayers.forEach { player: Player ->
            PlayerManager.getBaseAttributes(player.uniqueId.toString(), true)
            PlayerManager.alterPlayerAttributesWithEquippedStuff(player)
        }
    }
}