package me.rsman.BetterMinecraftCore.Listeners;

import com.sun.tools.javac.util.StringUtils;
import me.rsman.BetterMinecraftCore.Managers.Command.Lang.MessageKeys;
import me.rsman.BetterMinecraftCore.Managers.ConfigManager;
import me.rsman.BetterMinecraftCore.Managers.CraftManager;
import me.rsman.BetterMinecraftCore.Managers.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.CommandKits.CraftCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CraftCommandListener implements Listener {
  @EventHandler
  public void onCraftInventoryClose(InventoryCloseEvent event) {
    PersistentDataContainer container = event.getPlayer().getPersistentDataContainer();
    if (!container.has(CraftCommands.NAMESPACE_KEY, PersistentDataType.STRING)) return;

    String data = container.get(CraftCommands.NAMESPACE_KEY, PersistentDataType.STRING);
    event.getPlayer().getPersistentDataContainer().remove(CraftCommands.NAMESPACE_KEY);
    String[] splittedData = Objects.requireNonNull(data).split("\\|");
    if (splittedData.length != 4) return;
    String[] nameKeyPairArr = splittedData[0].split("\\.");
    String name = nameKeyPairArr[0];
    String key = nameKeyPairArr[1];
    String result = splittedData[1];

    Inventory inv = event.getInventory();
    if (inv.getType() != InventoryType.DISPENSER) return;

    int i = 0;
    String[][] schemes = new String[3][3];
    for (ItemStack item : inv.getStorageContents()){
      String itemName;
      if(item == null){
        itemName = "m.AIR";
      } else {
        if(ItemManager.getItemName(item).equals("")){
          itemName = "m." + item.getType();
        }else{
          itemName = ItemManager.getItemName(item);
        }
      }
      schemes[i / 3][i % 3] = itemName + (item != null ? " " + item.getAmount() : "");
      i++;
    }

    if(isShaped){
      String[] schemesFormatted = {"","",""};
      for (i=0; i<3; i++){
        schemesFormatted[i] = schemes[i][0] + " | " +  schemes[i][1] + " | " +  schemes[i][2];
      }
      ConfigManager.setKey("crafts/all", "recipes."+name+".shaped."+key+".shape", schemesFormatted);
      ConfigManager.setKey("crafts/all", "recipes."+name+".shaped."+key+".result", result +" "+ resultCount);
    } else {
      List<String> schemesFormatted = new ArrayList<>();
      for (i=0; i<9; i++){
        if(!schemes[i / 3][i % 3].equals("m.AIR")) schemesFormatted.add(schemes[i / 3][i % 3]);
      }
      ConfigManager.setKey("crafts/all", "recipes."+name+".shapeless."+key+".ingredients", schemesFormatted);
      ConfigManager.setKey("crafts/all", "recipes."+name+".shapeless."+key+".result", result +" "+ resultCount);
    }

    CraftManager.initCrafts();
  }
}
