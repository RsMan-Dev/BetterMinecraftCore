package me.rsman.BetterMinecraftCore.Enchantments;

import me.rsman.BetterMinecraftCore.Managers.ItemTypeChecker;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Telekinesis extends CustomEnchantClass {
    private static Telekinesis instance;

    private static final Map<String, Long> attributeModifiers = null;

    public Telekinesis() {
        super(NamespacedKey.minecraft("bmc_telekinesis"));
    }

    @Override
    public String getName() {
        return "bmc_Telekinesis";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {return null;}

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

    public static Telekinesis getEnchant(){
        if (instance == null) {
            instance = new Telekinesis();
        }
        return instance;
    }
    @Override
    public boolean isApplicable(ItemStack item) {
        return ItemTypeChecker.isToolOrWeapon(item);
    }
    @Override
    public int getMinimumLevel() {
        return 20;
    }

    @Override
    public boolean hasAttributesModifiers() {
        return false;
    }

    @Override
    public Map<String, Long> getAttributesModifiers() {
        return Telekinesis.attributeModifiers;
    }
}
