package me.rsman.BetterMinecraftCore.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.CommandKits.CraftCommands;

public class CraftCommandListener implements Listener {
  @EventHandler
  public void onCraftInventoryClose(InventoryCloseEvent event) {
    PersistentDataContainer container = event.getPlayer().getPersistentDataContainer();
    if (!container.has(CraftCommands.NAMESPACE_KEY, PersistentDataType.STRING)) return;

    String data = container.get(CraftCommands.NAMESPACE_KEY, PersistentDataType.STRING);
    event.getPlayer().getPersistentDataContainer().remove(CraftCommands.NAMESPACE_KEY);
    String[] splittedData = data.split("|");
    if (splittedData.length != 2) return;

    String nameKeyPair = splittedData[0];
    String result = splittedData[1];

    PlayerInventory inv = event.getPlayer().getInventory();
    if (inv.getType() != InventoryType.DISPENSER) return;

    

    BetterMinecraftCore.getInstance().getLogger().info(new StringBuilder().append("Crafting command: ").append(data).toString());
  }
}
