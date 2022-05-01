package me.rsman.BetterMinecraftCore.Listeners

import me.rsman.BetterMinecraftCore.Enchantments.CustomEnchantClass
import me.rsman.BetterMinecraftCore.Managers.ItemManager
import me.rsman.BetterMinecraftCore.configs.containers.MessagesLangContainer
import me.rsman.BetterMinecraftCore.utils.NBT
import org.bukkit.Material
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import kotlin.math.pow

class AnvilListener : Listener {
    @EventHandler
    fun onAnvilFill(e: PrepareAnvilEvent) {
        e.inventory.maximumRepairCost = 9999
        val firstItem = e.inventory.getItem(0)
        val secondItem = e.inventory.getItem(1)
        var repairCost: Int
        if (ItemManager.getItemName(firstItem) != "") {
            e.result = null
        }
        if(firstItem != null){
            repairCost = (2).toDouble().pow(NBT[firstItem, "repair-cost", PersistentDataType.INTEGER] ?: 0).toInt()
            if(e.inventory.renameText != "" && ItemManager.isRenamable(firstItem)){
                val res = ItemStack(Material.BARRIER, 1)
                val meta = res.itemMeta
                meta?.setDisplayName(
                    MessagesLangContainer.instance?.translations?.get(EMessages.RENAME_NOT_ALLOWED.key)
                    ?: "This item is not renamable"
                )
                res.itemMeta = meta
                e.result = res
                e.inventory.repairCost = 0
                return
            }
            if(firstItem.type != secondItem?.type
                && !(
                    ItemManager.getItemName(firstItem) == ItemManager.getItemName(secondItem)
                    || secondItem?.type == Material.ENCHANTED_BOOK
                )
            ){
                e.result = null
                e.inventory.repairCost = 0
                return
            }
            val result = firstItem.clone()
            val resultMeta = result.itemMeta
            if(resultMeta != null && secondItem != null){
                for(enchEntry in secondItem.enchantments){
                    if(firstItem.enchantments.containsKey(enchEntry.key)){
                        if(enchEntry.value > firstItem.enchantments[enchEntry.key]!!){
                            resultMeta.removeEnchant(enchEntry.key)
                            resultMeta.addEnchant(enchEntry.key, enchEntry.value, true)
                        } else if(enchEntry.value == firstItem.enchantments[enchEntry.key]!!){
                            if(enchEntry.value < enchEntry.key.maxLevel){
                                resultMeta.removeEnchant(enchEntry.key)
                                resultMeta.addEnchant(enchEntry.key, enchEntry.value +1, false)
                            }
                        }
                    } else if (enchEntry.key is CustomEnchantClass && (enchEntry.key as CustomEnchantClass).isApplicable(result) || enchEntry.key.itemTarget.includes(result)){
                        resultMeta.addEnchant(enchEntry.key, enchEntry.value, true)
                    }
                }
            }
            e.inventory.repairCost = repairCost
            result.itemMeta = resultMeta
            ItemManager.updateItemLore(result)
            e.result = result
        }
    }

    @EventHandler
    fun onAnvilPickResult(e: InventoryClickEvent){
        if(e.inventory is AnvilInventory && e.slotType == InventoryType.SlotType.RESULT && e.currentItem != null){
            if(e.currentItem?.type == Material.BARRIER) {
                e.isCancelled = true
                return
            } else {
                NBT[e.currentItem!!, "repair-cost", PersistentDataType.INTEGER] =
                        NBT[e.currentItem!!, "repair-cost", PersistentDataType.INTEGER]?.plus(1) ?: 1
            }
        }
    }
}