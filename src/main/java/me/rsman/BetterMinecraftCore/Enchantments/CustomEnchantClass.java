package me.rsman.BetterMinecraftCore.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class CustomEnchantClass extends Enchantment {
    public Map<String, Long> attributeModifiers;    public CustomEnchantClass(NamespacedKey key) {
        super(key);
    }
    public abstract boolean isApplicable(ItemStack item);
    public abstract int getMinimumLevel();
    public abstract boolean hasAttributesModifiers();
    public abstract Map<String, Long> getAttributesModifiers();
}
