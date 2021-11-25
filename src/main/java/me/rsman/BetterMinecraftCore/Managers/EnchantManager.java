package me.rsman.BetterMinecraftCore.Managers;

import me.rsman.BetterMinecraftCore.Enchantments.Aiming;
import me.rsman.BetterMinecraftCore.Enchantments.Protection;
import me.rsman.BetterMinecraftCore.Enchantments.Telekinesis;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class EnchantManager {
    public static final Map<String, Enchantment> enchants = new HashMap<String, Enchantment>(){{
        //vanilla
        for (Enchantment enchant: Enchantment.values()) {
            put(enchant.getKey().toString(), enchant);
        }

        //custom
        put(Protection.getEnchant().getKey().toString(), Protection.getEnchant());
        put(Telekinesis.getEnchant().getKey().toString(), Telekinesis.getEnchant());
        put(Aiming.getEnchant().getKey().toString(), Aiming.getEnchant());
    }};
    public static final Map<String, Map<String, Integer>> enchantsAttributesModifiers = new HashMap<String, Map<String, Integer>>(){{
        put(Protection.getEnchant().getKey().toString(), new HashMap<String, Integer>(){{
            put("defense", 5);
            put("health", 15);
        }});
    }};

    public static final Map<String, String> humanizedNames = new HashMap<String, String>() {{
        //vanilla
        put("minecraft:aqua_affinity", "Aqua Affinity");
        put("minecraft:bane_of_arthropods", "Bane of Arthropods");
        put("minecraft:blast_protection", "Blast Protection");
        put("minecraft:channeling", "Channeling");
        put("minecraft:binding_curse", "Curse of Binding");
        put("minecraft:vanishing_curse", "Curse of Vanishing");
        put("minecraft:depth_strider", "Depth Strider");
        put("minecraft:efficiency", "Efficiency");
        put("minecraft:feather_falling", "Feather Falling");
        put("minecraft:fire_aspect", "Fire Aspect");
        put("minecraft:fire_protection", "Fire Protection");
        put("minecraft:flame", "Flame");
        put("minecraft:fortune", "Fortune");
        put("minecraft:frost_walker", "Frost Walker");
        put("minecraft:impaling", "Impaling");
        put("minecraft:infinity", "Infinity");
        put("minecraft:knockback", "Knockback");
        put("minecraft:loyalty", "Loyalty");
        put("minecraft:luck_of_the_sea", "Luck of the Sea");
        put("minecraft:lure", "Lure");
        put("minecraft:mending", "Mending");
        put("minecraft:multishot", "Multishot");
        put("minecraft:piercing", "Piercing");
        put("minecraft:power", "Power");
        put("minecraft:projectile_protection", "Projectile Protection");
        put("minecraft:protection", "Protection");
        put("minecraft:punch", "Punch");
        put("minecraft:quick_charge", "Quick Charge");
        put("minecraft:respiration", "Respiration");
        put("minecraft:riptide", "Riptide");
        put("minecraft:sharpness", "Sharpness");
        put("minecraft:silk_touch", "Silk Touch");
        put("minecraft:smite", "Smite");
        put("minecraft:soul_speed", "Soul Speed");
        put("minecraft:sweeping", "Sweeping Edge");
        put("minecraft:unbreaking", "Unbreaking");
        put("minecraft:thorns", "Thorns");
        put("minecraft:looting", "Looting");


        //custom
        put(Protection.getEnchant().getKey().toString(), "Protection");
        put(Telekinesis.getEnchant().getKey().toString(), "Telekinesis");
        put(Aiming.getEnchant().getKey().toString(), "Aiming");
    }};


    public static void registerEnchantment(Enchantment enchantment){
        boolean registered = true;
        try{
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e){
            registered = false;
        }
    }
    public static void registerAllEnchantments(){
        for (Map.Entry<String, Enchantment> enchantment : enchants.entrySet()){
            if(!Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(enchantment))
                registerEnchantment(enchantment.getValue());
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

            for (Map.Entry<String, Enchantment> enchantment : enchants.entrySet()){
                if(enchantment.getKey().startsWith(NamespacedKey.minecraft("bmc_").toString())){
                    byKey.remove(enchantment.getValue().getKey());
                    byName.remove(enchantment.getValue().getName());
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
