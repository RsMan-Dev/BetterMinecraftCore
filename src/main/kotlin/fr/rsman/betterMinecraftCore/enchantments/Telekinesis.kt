package fr.rsman.betterMinecraftCore.enchantments

import org.bukkit.enchantments.Enchantment
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import fr.rsman.betterMinecraftCore.managers.ItemTypeChecker
import fr.rsman.betterMinecraftCore.enums.EAttributes
import org.bukkit.enchantments.EnchantmentTarget

class Telekinesis : CustomEnchantClass(NamespacedKey.minecraft("bmc_telekinesis")) {
    override fun getName(): String {
        return "bmc_Telekinesis"
    }

    override fun getMaxLevel(): Int {
        return 1
    }

    override fun getStartLevel(): Int {
        return 1
    }

    override fun getItemTarget(): EnchantmentTarget {
        return EnchantmentTarget.TOOL
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
        return ItemTypeChecker.isToolOrWeapon(item)
    }

    override val minimumLevel: Int
        get() = 20

    override fun hasAttributesModifiers(): Boolean {
        return false
    }

    override val attributeModifiers: Map<EAttributes, Long>
        get() = _attributeModifiers

    companion object {
        private lateinit var _instance: Telekinesis
        private val _attributeModifiers: Map<EAttributes, Long> = mapOf()
        val enchant: Telekinesis
            get() = if(this::_instance.isInitialized) _instance else {
                _instance = Telekinesis(); _instance
            }
    }
}