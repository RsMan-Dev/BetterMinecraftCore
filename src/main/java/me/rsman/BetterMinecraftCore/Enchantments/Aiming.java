package me.rsman.BetterMinecraftCore.Enchantments;

import me.rsman.BetterMinecraftCore.Managers.ItemTypeChecker;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Aiming extends CustomEnchantClass {
    private static Aiming instance;

    private static final Map<String, Long> attributeModifiers = null;

    public Aiming() {
        super(NamespacedKey.minecraft("bmc_aiming"));
    }

    @Override
    public String getName() {
        return "bmc_aiming";
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
        return EnchantmentTarget.BOW;
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

    public static Aiming getEnchant(){
        if (instance == null) {
            instance = new Aiming();
        }
        return instance;
    }

    @Override
    public boolean isApplicable(ItemStack item) {
        return ItemTypeChecker.isBow(item);
    }

    @Override
    public int getMinimumLevel() {
        return 10;
    }

    @Override
    public boolean hasAttributesModifiers() {
        return false;
    }

    @Override
    public Map<String, Long> getAttributesModifiers() {
        return Aiming.attributeModifiers;
    }
}
