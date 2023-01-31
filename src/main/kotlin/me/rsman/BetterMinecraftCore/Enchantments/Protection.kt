package me.rsman.BetterMinecraftCore.Enchantments

import java.util.HashMap
import org.bukkit.enchantments.Enchantment
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import me.rsman.BetterMinecraftCore.Managers.ItemTypeChecker
import me.rsman.BetterMinecraftCore.enums.EAttributes
import org.bukkit.enchantments.EnchantmentTarget

class Protection : CustomEnchantClass(NamespacedKey.minecraft("bmc_protection")) {

    override fun getName(): String {
        return "bmc_Protection"
    }

    override fun getMaxLevel(): Int {
        return 5
    }

    override fun getStartLevel(): Int {
        return 1
    }

    override fun getItemTarget(): EnchantmentTarget {
        return EnchantmentTarget.ARMOR
    }

    override fun isTreasure(): Boolean {
        return false
    }

    override fun isCursed(): Boolean {
        return false
    }

    override fun conflictsWith(enchantment: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(itemStack: ItemStack): Boolean {
        return true
    }

    override fun isApplicable(item: ItemStack): Boolean {
        return ItemTypeChecker.isArmorOrHead(item)
    }

    override val minimumLevel: Int
        get() = 99

    override fun hasAttributesModifiers(): Boolean {
        return attributeModifiers.isNotEmpty()
    }

    override val attributeModifiers: Map<EAttributes, Long>
        get() = _attributeModifiers

    companion object {
        private lateinit var _instance: Protection
        private var _attributeModifiers: Map<EAttributes, Long> = mapOf(
            EAttributes.DEFENSE to 5L,
            EAttributes.HEALTH to 15L
        )
        val enchant: Protection
            get() = if(this::_instance.isInitialized) _instance else {
                _instance = Protection(); _instance
            }
    }
}