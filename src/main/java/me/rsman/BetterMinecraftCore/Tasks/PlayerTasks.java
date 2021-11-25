package me.rsman.BetterMinecraftCore.Tasks;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Managers.PlayerManager;

public class PlayerTasks {
    public static void updatePlayers(){
        //long startTime = System.nanoTime();
        BetterMinecraftCore.getInstance().getServer().getOnlinePlayers().forEach(PlayerManager::updatePlayerAttributes);
        //long endTime = System.nanoTime();
        //FishingChill.getInstance().getLogger().info("inventory analyzing system took " + ((endTime - startTime)));
    }
}
