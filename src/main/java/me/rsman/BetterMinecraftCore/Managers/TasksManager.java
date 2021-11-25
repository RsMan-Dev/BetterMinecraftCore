package me.rsman.BetterMinecraftCore.Managers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Tasks.PlayerTasks;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class TasksManager {
    public static void registerAllTasks(){
        BukkitScheduler scheduler = BetterMinecraftCore.getInstance().getServer().getScheduler();
        Plugin plugin = BetterMinecraftCore.getInstance();

        scheduler.runTaskTimerAsynchronously(plugin, PlayerTasks::updatePlayers, 0, 10);
    }
}
