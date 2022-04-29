package me.rsman.BetterMinecraftCore.Managers.Command

import co.aikar.commands.*
import me.rsman.BetterMinecraftCore.configs.ConfigLoader.getFile
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.CommandKits.RootCommands
import me.rsman.BetterMinecraftCore.CommandKits.ItemCommands
import me.rsman.BetterMinecraftCore.CommandKits.PlayerCommands
import me.rsman.BetterMinecraftCore.CommandKits.CraftCommands
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
                instance = CommandManager(BetterMinecraftCore.instance)
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