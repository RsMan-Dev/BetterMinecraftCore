package me.rsman.BetterMinecraftCore.configs.containers;

import me.rsman.BetterMinecraftCore.configs.models.BmcItem;

import java.util.HashMap;

public class BmcItemContainer {
    private HashMap<String, BmcItem> items;

    public BmcItemContainer() {}

    public HashMap<String, BmcItem> getItems() {
        return items;
    }

    public void setItems(HashMap<String, BmcItem> items) {
        this.items = items;
    }
}