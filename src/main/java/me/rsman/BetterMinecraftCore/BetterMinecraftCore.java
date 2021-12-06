package me.rsman.BetterMinecraftCore;

import me.rsman.BetterMinecraftCore.Managers.*;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandCompletionsManager;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager;
import me.rsman.BetterMinecraftCore.configs.ConfigLoader;
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class BetterMinecraftCore extends JavaPlugin {
    private static BetterMinecraftCore instance;

    @Override
    public void onEnable() {
        Logger pluginLogging = Logger.getLogger(this.getName());
        instance = this;

        getLogger().info("§aStarting");

        ConfigLoader.init();

        BmcCraftContainer.registerCrafts();

        DBManager.initConnection();

        TasksManager.registerAllTasks();
        ListenersManager.registerAllEvents();
        EnchantManager.registerAllEnchantments();
        //CraftManager.initCrafts();

        pluginLogging.setLevel(Level.SEVERE);
        CommandManager.init();
        CommandCompletionsManager.init();
        pluginLogging.setLevel(Level.INFO);


        getServer().getOnlinePlayers().forEach((player) -> {
            PlayerManager.getBaseAttributes(player.getUniqueId().toString(), true);
            PlayerManager.alterPlayerAttributesWithEquippedStuff(player);
        });

        getLogger().info("§aStarted");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("§aShutting down");
        EnchantManager.unRegisterAllEnchantments();
        getServer().resetRecipes();
    }


    public static BetterMinecraftCore getInstance() {
        return instance;
    }
}
