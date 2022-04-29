package me.rsman.BetterMinecraftCore

import org.bukkit.plugin.java.JavaPlugin
import me.rsman.BetterMinecraftCore.configs.ConfigLoader
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer
import me.rsman.BetterMinecraftCore.Managers.DBManager
import me.rsman.BetterMinecraftCore.Managers.TasksManager
import me.rsman.BetterMinecraftCore.Managers.ListenersManager
import me.rsman.BetterMinecraftCore.Managers.EnchantManager
import me.rsman.BetterMinecraftCore.Managers.Command.CommandCompletionsManager
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager
import org.bukkit.entity.Player
import me.rsman.BetterMinecraftCore.Managers.PlayerManager
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