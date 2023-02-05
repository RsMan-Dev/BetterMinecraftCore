package fr.rsman.betterMinecraftCore.managers.command

import co.aikar.commands.*
import fr.rsman.betterMinecraftCore.configs.ConfigLoader.getFile
import fr.rsman.betterMinecraftCore.commandKits.RootCommands
import fr.rsman.betterMinecraftCore.commandKits.ItemCommands
import fr.rsman.betterMinecraftCore.commandKits.PlayerCommands
import fr.rsman.betterMinecraftCore.commandKits.CraftCommands
import java.util.Locale
import java.io.IOException
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.*
import org.bukkit.plugin.Plugin

class CommandManager(plugin: Plugin) : PaperCommandManager(plugin) {
    companion object {
        var instance: CommandManager? = null
        fun init() {
            if (instance == null) {
                instance = CommandManager(fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance)
                instance!!.registerCommand(RootCommands())
                instance!!.registerCommand(ItemCommands())
                instance!!.registerCommand(PlayerCommands())
                instance!!.registerCommand(CraftCommands())
            }
        }

        fun get(): CommandManager? {
            if (instance == null) {
                init()
            }
            return instance
        }
    }

    init {
        enableUnstableAPI("help")
        this.setFormat(MessageType.SYNTAX, ChatColor.AQUA, ChatColor.BLUE)
        this.setFormat(MessageType.INFO, ChatColor.AQUA, ChatColor.BLUE)
        this.setFormat(MessageType.HELP, ChatColor.AQUA, ChatColor.BLUE, ChatColor.GRAY)
        this.setFormat(MessageType.ERROR, ChatColor.RED, ChatColor.YELLOW)
        try {
            getLocales().loadYamlLanguageFile(getFile("lang/command"), Locale.ENGLISH)
        } catch (e: IOException) {
            plugin.logger.severe("Failed to load ACF core language file")
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            plugin.logger.severe("Failed to load ACF core language file")
            e.printStackTrace()
        }
        getLocales().defaultLocale = Locale.ENGLISH
    }
}