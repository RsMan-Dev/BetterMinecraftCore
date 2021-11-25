package me.rsman.BetterMinecraftCore.Listeners;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Enchantments.CustomEnchantClass;
import me.rsman.BetterMinecraftCore.Managers.EnchantManager;
import me.rsman.BetterMinecraftCore.Managers.ItemManager;
import me.rsman.BetterMinecraftCore.Managers.ItemTypeChecker;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

import java.util.*;

public class EnchantListener implements Listener {
    @EventHandler
    public void addCustomEnchantFromTable(EnchantItemEvent e){
        Map<Enchantment,Map<Enchantment, Integer>> enchantsToReplace = new HashMap<>();
        for (Map.Entry<Enchantment, Integer> enchantToAdd: e.getEnchantsToAdd().entrySet()) {
           if(enchantToAdd.getKey().getKey().toString().equals("minecraft:protection")){
               enchantsToReplace.put(enchantToAdd.getKey(), new HashMap<Enchantment, Integer>(){{put(EnchantManager.enchants.get("minecraft:bmc_protection"),enchantToAdd.getValue());}});
           }
        }
        for (Map.Entry<Enchantment,Map<Enchantment, Integer>> enchantToReplace: enchantsToReplace.entrySet()) {
            e.getEnchantsToAdd().remove(enchantToReplace.getKey());
            Map.Entry<Enchantment, Integer> enchant = enchantToReplace.getValue().entrySet().iterator().next();
            e.getEnchantsToAdd().put(enchant.getKey(), enchant.getValue());
        }
        List<CustomEnchantClass> applicableEnchants = new ArrayList<>();
        for(Map.Entry<String, Enchantment> customEnchant : EnchantManager.enchants.entrySet()){
            if(customEnchant.getValue() instanceof CustomEnchantClass){
                if(((CustomEnchantClass) customEnchant.getValue()).isApplicable(e.getItem()) && ((CustomEnchantClass) customEnchant.getValue()).getMinimumLevel() < e.getExpLevelCost()){
                    applicableEnchants.add((CustomEnchantClass)customEnchant.getValue());
                }
            }
        }
        while(Math.random() > 0.5 && applicableEnchants.size() > 0){
            int enchantKey = (int)Math.round(Math.random() * (applicableEnchants.size()-1));
            int deltaLv = e.getExpLevelCost() - applicableEnchants.get(enchantKey).getMinimumLevel();
            int deltaMax = 30 - applicableEnchants.get(enchantKey).getMinimumLevel();
            int level = Math.max(1,
                            Math.min(applicableEnchants.get(enchantKey).getMaxLevel(),
                                Math.round((float)(deltaLv/deltaMax)*applicableEnchants.get(enchantKey).getMaxLevel())
                            )
                        );
            e.getEnchantsToAdd().put(applicableEnchants.get(enchantKey), level);
            applicableEnchants.remove(enchantKey);
        }
        for (Map.Entry<Enchantment, Integer> enchantToAdd: e.getEnchantsToAdd().entrySet()) {
            e.getItem().addUnsafeEnchantment(enchantToAdd.getKey(), enchantToAdd.getValue());
        }
        BetterMinecraftCore.getInstance().getLogger().info(e.getEnchantsToAdd().toString());
        ItemManager.updateItemLore(e.getItem());
    }
}
