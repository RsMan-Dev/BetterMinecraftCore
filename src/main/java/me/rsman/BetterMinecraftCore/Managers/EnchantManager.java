package me.rsman.BetterMinecraftCore.Managers;

import me.rsman.BetterMinecraftCore.enums.EEnchants;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public final class EnchantManager {

    public static void registerEnchantment(Enchantment enchantment){
        try{
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception ignored){}
    }
    public static void registerAllEnchantments(){
        for (EEnchants enchantment : EEnchants.values()){
            if(!Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(enchantment.getEnchant()))
                registerEnchantment(enchantment.getEnchant());
        }
    }
    public static void unRegisterAllEnchantments(){
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");
            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);

            Field nameField = Enchantment.class.getDeclaredField("byName");
            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            for (EEnchants enchantment : EEnchants.values()){
                if(enchantment.getKey().startsWith(NamespacedKey.minecraft("bmc_").toString())){
                    byKey.remove(enchantment.getEnchant().getKey());
                    byName.remove(enchantment.getEnchant().getName());
                }
            }
        } catch (Exception ignored) { }
    }
    public static boolean hasEnchantment(ItemStack item, Enchantment enchantment){
        return item.getEnchantments().containsKey(enchantment);
    }
    public static void addEnchantment(ItemStack item, Enchantment enchantment, int level){
        item.addUnsafeEnchantment(enchantment, level);
        ItemManager.updateItemLore(item);
    }
    public static void removeEnchantment(ItemStack item, Enchantment enchantment){
        item.removeEnchantment(enchantment);
        ItemManager.updateItemLore(item);
    }
    public static Integer getEnchantmentLevel(ItemStack item, Enchantment enchantment){
        return item.getEnchantments().get(enchantment);
    }
    public static boolean isCustom(Enchantment enchantment){
        return enchantment.getKey().toString().startsWith(NamespacedKey.minecraft("bmc_").toString());
    }
}
