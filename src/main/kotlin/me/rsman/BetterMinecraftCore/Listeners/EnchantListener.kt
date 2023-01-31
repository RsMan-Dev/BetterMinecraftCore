package me.rsman.BetterMinecraftCore.Listeners

import java.util.HashMap
import org.bukkit.enchantments.Enchantment
import me.rsman.BetterMinecraftCore.enums.EEnchants
import me.rsman.BetterMinecraftCore.Managers.ItemManager
import me.rsman.BetterMinecraftCore.Enchantments.CustomEnchantClass
import me.rsman.BetterMinecraftCore.extensions.updateCustomLore
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.ArrayList

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
            val enchantKey = Math.round(Math.random() * (applicableEnchants.size - 1)).toInt()
            val deltaLv = e.expLevelCost - applicableEnchants[enchantKey].minimumLevel
            val deltaMax = 30 - applicableEnchants[enchantKey].minimumLevel
            val level = Math.max(1,
                    Math.min(applicableEnchants[enchantKey].maxLevel,
                            Math.round(deltaLv.toFloat() / deltaMax.toFloat() * applicableEnchants[enchantKey].maxLevel)
                    )
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