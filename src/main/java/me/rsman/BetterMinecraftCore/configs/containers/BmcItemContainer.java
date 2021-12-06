package me.rsman.BetterMinecraftCore.configs.containers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.configs.ConfigLoader;
import me.rsman.BetterMinecraftCore.configs.models.BmcItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BmcItemContainer {
    private static BmcItemContainer instance;

    private static void setInstance(BmcItemContainer instance) {
        BmcItemContainer.instance = instance;
    }

    public static BmcItemContainer getInstance() {
        return instance;
    }

    public static void load(){
        setInstance(null);
        BetterMinecraftCore.getInstance().getLogger().info("§3Loading BMC items...");
        BmcItemContainer bmcItemContainerInstance = ConfigLoader.loadConfig("items/all", BmcItemContainer.class);
        if(bmcItemContainerInstance == null){
            BetterMinecraftCore.getInstance().getLogger().severe("§4Items cannot be loaded");
        } else {
            List<String> keys = new ArrayList<>();
            for (Map.Entry<String, BmcItem> entry : bmcItemContainerInstance.getItems().entrySet()) {
                BmcItem item = entry.getValue();
                item.setName(entry.getKey());
                bmcItemContainerInstance.getItems().put(entry.getKey(), item);
                keys.add(entry.getKey());
            }
            BetterMinecraftCore.getInstance().getLogger().info("§bLoaded §6" + keys.size() + " §bitems." );
            if(GlobalConfigContainer.getInstance().isVerbose()){
                BetterMinecraftCore.getInstance().getLogger().info("§bLoaded items: §6" + keys );
            }
        }
        setInstance(bmcItemContainerInstance);
    }

    public static void save(){
        BetterMinecraftCore.getInstance().getLogger().info("§3Saving BMC items...");
        BmcItemContainer bmcItemsContainerClone = BmcItemContainer.getInstance().cloneForConfig();
        ConfigLoader.saveConfig("items/all", bmcItemsContainerClone);
        BetterMinecraftCore.getInstance().getLogger().info("§bSaved BMC items." );
    }

    private HashMap<String, BmcItem> items;

    public BmcItemContainer() {}

    public HashMap<String, BmcItem> getItems() {
        return items;
    }

    public void setItems(HashMap<String, BmcItem> items) {
        this.items = items;
    }

    public BmcItemContainer cloneForConfig() {
        BmcItemContainer ic = new BmcItemContainer();
        HashMap<String, BmcItem> itemsClone = new HashMap<>();
        for (Map.Entry<String, BmcItem> itemToClone: items.entrySet()) {
            itemsClone.put(itemToClone.getKey(), itemToClone.getValue().cloneForConfig());
        }
        ic.setItems(itemsClone);
        return ic;
    }
}