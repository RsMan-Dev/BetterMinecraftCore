package me.rsman.BetterMinecraftCore;

import me.rsman.BetterMinecraftCore.Managers.*;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandCompletionsManager;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager;
import me.rsman.BetterMinecraftCore.configs.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class BetterMinecraftCore extends JavaPlugin {
    private static BetterMinecraftCore instance;

    @Override
    public void onEnable() {
        instance = this;
        Logger pluginLogging = Logger.getLogger(instance.getName());
        // Plugin startup logic
        getLogger().info("§aStarting");

        if(!DBManager.initConnection()){
            getLogger().warning("Database connection not initialized, please setup config for mariaDB database, disabling plugin");
            getServer().getPluginManager().disablePlugin(this);
        }

        TasksManager.registerAllTasks();
        ListenersManager.registerAllEvents();
        EnchantManager.registerAllEnchantments();
        ItemManager.registerAllItemsWithConfig();
        CraftManager.initCrafts();

        pluginLogging.setLevel(Level.SEVERE);
        CommandManager.init();
        CommandCompletionsManager.init();
        pluginLogging.setLevel(Level.INFO);

        ConfigLoader.init();

        getServer().getOnlinePlayers().forEach((player) -> {
            PlayerManager.getBaseAttributes(player.getUniqueId().toString(), true);
            PlayerManager.alterPlayerAttributesWithEquippedStuff(player);
        });

        getLogger().info("§aStarted");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("!aShutting down");
        EnchantManager.unRegisterAllEnchantments();
    }


    public static BetterMinecraftCore getInstance() {
        return instance;
    }
}
