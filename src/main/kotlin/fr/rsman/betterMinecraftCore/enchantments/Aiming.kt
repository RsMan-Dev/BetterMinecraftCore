package fr.rsman.betterMinecraftCore.enchantments

import fr.rsman.betterMinecraftCore.configs.containers.EnchantLangContainer
import org.bukkit.enchantments.Enchantment
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import fr.rsman.betterMinecraftCore.managers.ItemTypeChecker
import fr.rsman.betterMinecraftCore.enums.EAttributes
import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.Component
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot

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

    override fun displayName(p0: Int): Component {
        return Component.text(EnchantLangContainer.instance?.translations?.get(name) ?: "")
    }

    override fun isTradeable(): Boolean {
        return true
    }

    override fun isDiscoverable(): Boolean {
        return true;
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.VERY_RARE
    }

    override fun getDamageIncrease(p0: Int, p1: EntityCategory): Float {
        return 0.0F
    }

    override fun getActiveSlots(): MutableSet<EquipmentSlot> {
        return EquipmentSlot.values().toMutableSet()
    }

    override fun isApplicable(item: ItemStack): Boolean {
        return ItemTypeChecker.isBow(item)
    }

    override val minimumLevel: Int
        get() = 10

    override fun hasAttributesModifiers(): Boolean {
        return attributeModifiers.isNotEmpty()
    }

    override fun translationKey(): String {
        return name;
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