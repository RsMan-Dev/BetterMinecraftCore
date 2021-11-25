package me.rsman.BetterMinecraftCore;

import me.rsman.BetterMinecraftCore.Managers.*;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandCompletionsManager;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterMinecraftCore extends JavaPlugin {
    private static BetterMinecraftCore instance;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        getLogger().info("Starting");

        if(!DBManager.initConnection()){
            getLogger().warning("Database connection not initialized, please setup config for mariaDB database, disabling plugin");
            getServer().getPluginManager().disablePlugin(this);
        }

        TasksManager.registerAllTasks();
        ListenersManager.registerAllEvents();
        EnchantManager.registerAllEnchantments();
        ItemManager.registerAllItemsWithConfig();
        CraftManager.initCrafts();
        CommandManager.init();
        CommandCompletionsManager.init();

        getServer().getOnlinePlayers().forEach((player) -> {
            PlayerManager.getBaseAttributes(player.getUniqueId().toString(), true);
            PlayerManager.alterPlayerAttributesWithEquippedStuff(player);
        });

        getLogger().info("Started");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Shut down");
        EnchantManager.unRegisterAllEnchantments();
    }


    public static BetterMinecraftCore getInstance() {
        return instance;
    }
}
