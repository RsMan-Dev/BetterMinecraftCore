package me.rsman.BetterMinecraftCore.utils;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class NBT {
    private static PersistentDataContainer getMainData(ItemMeta itemMeta){
        NamespacedKey namespacedKeyGlobal = new NamespacedKey(BetterMinecraftCore.getInstance(), "mainItemData");
        PersistentDataContainer data = itemMeta.getPersistentDataContainer().get(namespacedKeyGlobal, PersistentDataType.TAG_CONTAINER);
        PersistentDataAdapterContext dataContext = itemMeta.getPersistentDataContainer().getAdapterContext();
        if(data == null){
            itemMeta.getPersistentDataContainer().set(namespacedKeyGlobal, PersistentDataType.TAG_CONTAINER, dataContext.newPersistentDataContainer());
            data = itemMeta.getPersistentDataContainer().get(namespacedKeyGlobal, PersistentDataType.TAG_CONTAINER);
        }
        return data;
    }
    private static ItemMeta setMainData(ItemMeta itemMeta, PersistentDataContainer ctn){
        NamespacedKey namespacedKeyGlobal = new NamespacedKey(BetterMinecraftCore.getInstance(), "mainItemData");
        itemMeta.getPersistentDataContainer().set(namespacedKeyGlobal, PersistentDataType.TAG_CONTAINER, ctn);
        return itemMeta;
    }

    public static ItemStack set(ItemStack item, String key, PersistentDataType type, Object value){
        NamespacedKey namespacedKey = new NamespacedKey(BetterMinecraftCore.getInstance(), key);
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer itemData = NBT.getMainData(itemMeta);
        if(itemData == null){
            Bukkit.getLogger().warning("Cannot get item global data");
            return item;
        }
        itemData.set(namespacedKey, type, value);
        itemMeta = NBT.setMainData(itemMeta, itemData);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static Object get(ItemStack item, String key, PersistentDataType type){
        NamespacedKey namespacedKey = new NamespacedKey(BetterMinecraftCore.getInstance(), key);
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) return null;
        PersistentDataContainer itemData = NBT.getMainData(itemMeta);
        if(itemData == null){
            //Bukkit.getLogger().warning("Cannot get item global data");
            return null;
        }
        return itemData.get(namespacedKey, type);
    }

    public static boolean remove(ItemStack item, String key){
        NamespacedKey namespacedKey = new NamespacedKey(BetterMinecraftCore.getInstance(), key);
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) return false;
        PersistentDataContainer itemData = NBT.getMainData(itemMeta);
        if(itemData == null){
            Bukkit.getLogger().warning("Cannot get item global data");
            return false;
        }
        itemData.remove(namespacedKey);
        return true;
    }
}
