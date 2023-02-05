package fr.rsman.betterMinecraftCore

import fr.rsman.betterMinecraftCore.managers.*
import fr.rsman.betterMinecraftCore.configs.ConfigLoader
import fr.rsman.betterMinecraftCore.configs.containers.BmcCraftContainer
import fr.rsman.betterMinecraftCore.managers.command.CommandCompletionsManager
import fr.rsman.betterMinecraftCore.managers.command.CommandManager
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level
import java.util.logging.Logger

class BetterMinecraftCore : JavaPlugin() {
    override fun onEnable() {
        val pluginLogging = Logger.getLogger(this.name)
        instance = this
        logger.info("§aStarting")
        ConfigLoader.init()
        BmcCraftContainer.registerCrafts()
        DBManager.initConnection()
        TasksManager.registerAllTasks()
        ListenersManager.registerAllEvents()
        EnchantManager.registerAllEnchantments()
        //CraftManager.initCrafts();
        pluginLogging.level = Level.SEVERE
        CommandManager.init()
        CommandCompletionsManager.init()
        pluginLogging.level = Level.INFO
        server.onlinePlayers.forEach { player: Player? ->
            PlayerManager.getBaseAttributes(player!!.uniqueId.toString(), true)
            PlayerManager.alterPlayerAttributesWithEquippedStuff(player)
        }
        PapiManager.registerExpansion()
        logger.info("§aStarted")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("§aShutting down")
        EnchantManager.unRegisterAllEnchantments()
        server.resetRecipes()
    }

    companion object {
        @JvmStatic
        lateinit var instance: BetterMinecraftCore
            private set
    }
}