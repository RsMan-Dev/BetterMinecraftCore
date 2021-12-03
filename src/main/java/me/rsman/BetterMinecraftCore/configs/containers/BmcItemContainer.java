package me.rsman.BetterMinecraftCore.configs.containers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.configs.ConfigLoader;
import me.rsman.BetterMinecraftCore.configs.models.BmcItem;

import java.util.HashMap;
import java.util.Map;

public class BmcItemContainer {
    private static BmcItemContainer instance;

    public static BmcItemContainer getInstance() {
        return instance;
    }

    public static void load(){
        BetterMinecraftCore.getInstance().getLogger().info("§3Loading BMC items...");
        BmcItemContainer BMCItemContainerInstance = ConfigLoader.loadConfig("items/all.yml", BmcItemContainer.class);
        if(BMCItemContainerInstance == null){
            BetterMinecraftCore.getInstance().getLogger().severe("Items cannot be loaded");
        } else {
            for (Map.Entry<String, BmcItem> entry : BMCItemContainerInstance.getItems().entrySet()) {
                BmcItem item = entry.getValue();
                item.setName(entry.getKey());
                BMCItemContainerInstance.getItems().put(entry.getKey(), item);
                BetterMinecraftCore.getInstance().getLogger().info("§bLoaded item: " + entry.getKey());
            }
        }
    }

    private HashMap<String, BmcItem> items;

    public BmcItemContainer() {}

    public HashMap<String, BmcItem> getItems() {
        return items;
    }

    public void setItems(HashMap<String, BmcItem> items) {
        this.items = items;
    }
}