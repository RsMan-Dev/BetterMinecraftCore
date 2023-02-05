package fr.rsman.betterMinecraftCore.enchantments

import org.bukkit.enchantments.Enchantment
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import fr.rsman.betterMinecraftCore.managers.ItemTypeChecker
import fr.rsman.betterMinecraftCore.enums.EAttributes
import org.bukkit.enchantments.EnchantmentTarget

class Aiming : CustomEnchantClass(NamespacedKey.minecraft("bmc_aiming")) {

    override fun getName(): String {
        return "bmc_aiming"
    }

    override fun getMaxLevel(): Int {
        return 5
    }

    override fun getStartLevel(): Int {
        return 1
    }

    override fun getItemTarget(): EnchantmentTarget {
        return EnchantmentTarget.BOW
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
        return ItemTypeChecker.isBow(item)
    }

    override val minimumLevel: Int
        get() = 10

    override fun hasAttributesModifiers(): Boolean {
        return attributeModifiers.isNotEmpty()
    }

    override val attributeModifiers: Map<EAttributes, Long>
        get() = _attributeModifiers

    companion object {
        private lateinit var _instance: Aiming
        private var _attributeModifiers: Map<EAttributes, Long> = mapOf()
        val enchant: Aiming
            get() = if(this::_instance.isInitialized) _instance else {
                _instance = Aiming(); _instance
            }
    }
}