@file:Suppress("BooleanMethodIsAlwaysInverted", "BooleanMethodIsAlwaysInverted")

package fr.rsman.betterMinecraftCore.enchantments

import fr.rsman.betterMinecraftCore.enums.EAttributes
import org.bukkit.enchantments.Enchantment
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

abstract class CustomEnchantClass(key: NamespacedKey?) : Enchantment(key!!) {
    abstract val attributeModifiers: Map<EAttributes, Long>

    @Suppress("BooleanMethodIsAlwaysInverted")
    abstract fun isApplicable(item: ItemStack): Boolean

    abstract val minimumLevel: Int

    abstract fun hasAttributesModifiers(): Boolean
}