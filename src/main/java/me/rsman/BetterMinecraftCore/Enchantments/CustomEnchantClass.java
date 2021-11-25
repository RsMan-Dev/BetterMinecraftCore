package me.rsman.BetterMinecraftCore.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public abstract class CustomEnchantClass extends Enchantment {
    public CustomEnchantClass(NamespacedKey key) {
        super(key);
    }
    public abstract boolean isApplicable(ItemStack item);
    public abstract int getMinimumLevel();
}
