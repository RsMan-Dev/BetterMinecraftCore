package me.rsman.BetterMinecraftCore.Listeners

import EMessages
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.Enchantments.CustomEnchantClass
import me.rsman.BetterMinecraftCore.Managers.ItemManager
import me.rsman.BetterMinecraftCore.configs.containers.MessagesLangContainer
import me.rsman.BetterMinecraftCore.utils.NBT
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType
import kotlin.math.pow

class AnvilListener : Listener {
    @EventHandler
    fun onAnvilFill(e: PrepareAnvilEvent) {
        val inventory = e.inventory;
        val firstItem = inventory.getItem(0)
        val secondItem = inventory.getItem(1)
        val secondItemMeta = secondItem?.itemMeta;
        var repairCost = 0
        if (ItemManager.getItemName(firstItem) != "") {
            e.result = null
        }
        if(firstItem != null){
            val result = firstItem.clone()
            val resultMeta = result.itemMeta
            if(inventory.renameText != resultMeta?.displayName?.replace("((§.)*)".toRegex(), "") && !ItemManager.isRenamable(firstItem)){
                val res = ItemStack(Material.BARRIER, 1)
                val meta = res.itemMeta
                meta?.setDisplayName(
                    MessagesLangContainer.instance?.translations?.get(EMessages.RENAME_NOT_ALLOWED.key)
                    ?: "This item is not renamable"
                )
                res.itemMeta = meta
                e.result = res
                inventory.repairCost = 0
                return
            } else if (inventory.renameText != resultMeta?.displayName?.replace("((§.)*)".toRegex(), "")) {
                resultMeta?.setDisplayName(
                    inventory.renameText?.replace("&", "§")
                    ?: resultMeta.displayName
                )
                repairCost += 1
            }
            if(firstItem.type != secondItem?.type
                && !(
                    ItemManager.getItemName(firstItem) == ItemManager.getItemName(secondItem)
                    || secondItem?.type == Material.ENCHANTED_BOOK
                )
                && secondItem != null
            ){
                e.result = null
                e.inventory.repairCost = 0
                return
            }
            if(resultMeta != null && secondItem != null){
                repairCost = (2).toDouble().pow(NBT[firstItem, "repair-cost", PersistentDataType.INTEGER] ?: 0).toInt()
                for(enchEntry in secondItem.enchantments){
                    if(firstItem.enchantments.containsKey(enchEntry.key)){
                        if(enchEntry.value > firstItem.enchantments[enchEntry.key]!!){
                            repairCost += firstItem.enchantments[enchEntry.key]?.minus(enchEntry.value) ?: 0
                            resultMeta.removeEnchant(enchEntry.key)
                            resultMeta.addEnchant(enchEntry.key, enchEntry.value, true)
                        } else if(enchEntry.value == firstItem.enchantments[enchEntry.key]!!){
                            if(enchEntry.value < enchEntry.key.maxLevel){
                                repairCost += 1
                                resultMeta.removeEnchant(enchEntry.key)
                                resultMeta.addEnchant(enchEntry.key, enchEntry.value +1, false)
                            }
                        }
                    } else if (enchEntry.key is CustomEnchantClass && (enchEntry.key as CustomEnchantClass).isApplicable(result) || enchEntry.key.itemTarget.includes(result)){
                        repairCost += enchEntry.value
                        resultMeta.addEnchant(enchEntry.key, enchEntry.value, true)
                    }
                }
                if(resultMeta is Damageable && secondItemMeta is Damageable){
                    resultMeta.damage = resultMeta.damage - (secondItem.type.maxDurability - secondItemMeta.damage)
                }
            }
            result.itemMeta = resultMeta
            ItemManager.updateItemLore(result)
            resultMeta?.lore = resultMeta?.lore?.plus(listOf("§c$repairCost lvl requis")) ?: listOf("§c$repairCost lvl requis")
            result.itemMeta = resultMeta
            e.result = result
            inventory.repairCost = repairCost
            inventory.maximumRepairCost = 9999
            BetterMinecraftCore.instance.server.scheduler.runTaskLaterAsynchronously(BetterMinecraftCore.instance, Runnable {
                inventory.repairCost = repairCost
                inventory.maximumRepairCost = 9999
            }, 0)
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
                ItemManager.updateItemLore(e.currentItem!!)
            }
        }
    }
}