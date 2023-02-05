package fr.rsman.betterMinecraftCore.listeners

import org.bukkit.enchantments.Enchantment
import fr.rsman.betterMinecraftCore.enums.EEnchants
import fr.rsman.betterMinecraftCore.enchantments.CustomEnchantClass
import fr.rsman.betterMinecraftCore.extensions.updateCustomLore
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.ArrayList
import kotlin.math.roundToInt

class EnchantListener : Listener {
    @EventHandler
    fun addCustomEnchantFromTable(e: EnchantItemEvent) {
        val enchantsToReplace = mutableMapOf<Enchantment, Map<Enchantment, Int>>()
        for ((key, value) in e.enchantsToAdd) {
            if (EEnchants.replacesMapFromNamespaces.containsKey(key.key.toString())) {
                enchantsToReplace[key] = mapOf(EEnchants.replacesMapFromNamespaces[key.key.toString()]!! to value)
            }
        }
        for ((key, value) in enchantsToReplace) {
            e.enchantsToAdd.remove(key)
            val (key1, value1) = value.entries.iterator().next()
            e.enchantsToAdd[key1] = value1
        }
        val applicableEnchants: MutableList<CustomEnchantClass> = ArrayList()
        for (ench in EEnchants.values()) {
            if (ench.enchant is CustomEnchantClass) {
                if (ench.enchant.isApplicable(e.item) && ench.enchant.minimumLevel < e.expLevelCost) {
                    applicableEnchants.add(ench.enchant)
                }
            }
        }
        while (Math.random() > 0.5 && applicableEnchants.size > 0) {
            val enchantKey = (Math.random() * (applicableEnchants.size - 1)).roundToInt()
            val deltaLv = e.expLevelCost - applicableEnchants[enchantKey].minimumLevel
            val deltaMax = 30 - applicableEnchants[enchantKey].minimumLevel
            val level = 1.coerceAtLeast(
                applicableEnchants[enchantKey].maxLevel.coerceAtMost((deltaLv.toFloat() / deltaMax.toFloat() * applicableEnchants[enchantKey].maxLevel).roundToInt())
            )
            e.enchantsToAdd[applicableEnchants[enchantKey]] = level
            applicableEnchants.removeAt(enchantKey)
        }
        for ((key, value) in e.enchantsToAdd) {
            e.item.addUnsafeEnchantment(key!!, value!!)
        }
        e.item.updateCustomLore()
    }
}