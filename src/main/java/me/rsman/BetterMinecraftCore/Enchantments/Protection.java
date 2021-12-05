package me.rsman.BetterMinecraftCore.Enchantments;

import me.rsman.BetterMinecraftCore.Managers.ItemTypeChecker;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Protection extends CustomEnchantClass {
    private static Protection instance;

    private static final Map<String, Long> attributeModifiers = new HashMap<String, Long>(){{
        put("defense", 5L);
        put("health", 15L);
    }};

    public Protection() {
        super(NamespacedKey.minecraft("bmc_protection"));
    }

    @Override
    public String getName() {
        return "bmc_Protection";
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return true;
    }

    public static Protection getEnchant(){
        if (instance == null) {
            instance = new Protection();
        }
        return instance;
    }

    @Override
    public boolean isApplicable(ItemStack item) {
        return ItemTypeChecker.isArmorOrHead(item);
    }

    @Override
    public int getMinimumLevel() {
        return 99;
    }

    @Override
    public boolean hasAttributesModifiers() {
        return false;
    }

    @Override
    public Map<String, Long> getAttributesModifiers() {
        return Protection.attributeModifiers;
    }
}
