package me.rsman.BetterMinecraftCore.Managers

import java.util.HashMap
import org.bukkit.enchantments.Enchantment
import me.rsman.BetterMinecraftCore.enums.EEnchants
import me.rsman.BetterMinecraftCore.extensions.updateCustomLore
import org.bukkit.NamespacedKey
import java.util.Arrays
import org.bukkit.inventory.ItemStack
import java.util.stream.Collectors
import java.lang.Exception

object EnchantManager {
    fun registerEnchantment(enchantment: Enchantment?) {
        try {
            val f = Enchantment::class.java.getDeclaredField("acceptingNew")
            f.isAccessible = true
            f[null] = true
            Enchantment.registerEnchantment(enchantment!!)
        } catch (ignored: Exception) {
        }
    }

    fun registerAllEnchantments() {
        for (enchantment in EEnchants.values()) {
            if (!Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(enchantment.enchant)) registerEnchantment(enchantment.enchant)
        }
    }

    fun unRegisterAllEnchantments() {
        try {
            val keyField = Enchantment::class.java.getDeclaredField("byKey")
            keyField.isAccessible = true
            val byKey = keyField[null] as HashMap<*, *>
            val nameField = Enchantment::class.java.getDeclaredField("byName")
            nameField.isAccessible = true
            val byName = nameField[null] as HashMap<*, *>
            for (enchantment in EEnchants.values()) {
                if (enchantment.key.startsWith(NamespacedKey.minecraft("bmc_").toString())) {
                    byKey.remove(enchantment.enchant?.key)
                    byName.remove(enchantment.enchant?.name)
                }
            }
        } catch (ignored: Exception) {
        }
    }

    fun hasEnchantment(item: ItemStack, enchantment: Enchantment?): Boolean {
        return item.enchantments.containsKey(enchantment)
    }

    fun addEnchantment(item: ItemStack, enchantment: Enchantment?, level: Int) {
        item.addUnsafeEnchantment(enchantment!!, level)
        item.updateCustomLore()
    }

    fun removeEnchantment(item: ItemStack, enchantment: Enchantment?) {
        item.removeEnchantment(enchantment!!)
        item.updateCustomLore()
    }

    fun getEnchantmentLevel(item: ItemStack, enchantment: Enchantment?): Int? {
        return item.enchantments[enchantment]
    }

    fun isCustom(enchantment: Enchantment): Boolean {
        return enchantment.key.toString().startsWith(NamespacedKey.minecraft("bmc_").toString())
    }
}