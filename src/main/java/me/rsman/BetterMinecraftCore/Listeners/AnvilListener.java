package me.rsman.BetterMinecraftCore.Listeners;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Managers.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilListener implements Listener {


    @EventHandler
    public void onAnvilFill(PrepareAnvilEvent e){
        ItemStack firstItem = e.getInventory().getItem(0);
        ItemStack secondItem = e.getInventory().getItem(1);
        ItemStack result = e.getInventory().getItem(3);

        if(!ItemManager.getItemName(firstItem).equals("")){
            e.setResult(null);
        }
    }
}
