package me.rsman.BetterMinecraftCore.Listeners

import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.Managers.ItemManager
import me.rsman.BetterMinecraftCore.extensions.updateCustomLore
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.meta.Damageable

class GrindstoneListener : Listener {
    @EventHandler
    fun onGrindstonePickResult(e: InventoryClickEvent){
        if(e.inventory is GrindstoneInventory && e.slotType != InventoryType.SlotType.RESULT){
            BetterMinecraftCore.instance.server.scheduler.runTaskLater(BetterMinecraftCore.instance, Runnable {
                if(e.inventory.getItem(3) != null) e.inventory.getItem(3)?.updateCustomLore()
            }, 5)
        }
        if(e.inventory is GrindstoneInventory && e.slotType == InventoryType.SlotType.RESULT){
            var result = e.inventory.getItem(1)
            var noDamageEdit = result == null
            if(result == null) result = e.inventory.getItem(2)
            if(result == null) {e.isCancelled = true; return}
            val resultMeta = result.itemMeta
            if(resultMeta is Damageable && noDamageEdit){
                val secondItem = e.inventory.getItem(2)
                val secondItemMeta = secondItem?.itemMeta
                if(secondItemMeta != null && secondItemMeta is Damageable){
                    resultMeta.damage = resultMeta.damage - (secondItem.type.maxDurability - secondItemMeta.damage)
                }
            }
            if(resultMeta != null){
                for (enchEntry in resultMeta.enchants) {
                    resultMeta.removeEnchant(enchEntry.key)
                }
            }
            result.itemMeta = resultMeta
            result.updateCustomLore()
            e.currentItem = result
        }
    }
}