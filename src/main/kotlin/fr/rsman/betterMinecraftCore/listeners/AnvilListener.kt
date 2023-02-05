package fr.rsman.betterMinecraftCore.listeners

import fr.rsman.betterMinecraftCore.enums.EMessages
import fr.rsman.betterMinecraftCore.enchantments.CustomEnchantClass
import fr.rsman.betterMinecraftCore.configs.containers.MessagesLangContainer
import fr.rsman.betterMinecraftCore.extensions.isRenamable
import fr.rsman.betterMinecraftCore.extensions.saveName
import fr.rsman.betterMinecraftCore.extensions.updateCustomLore
import fr.rsman.betterMinecraftCore.utils.getNbt
import fr.rsman.betterMinecraftCore.utils.setNbt
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import kotlin.math.pow

class AnvilListener : Listener {
    @EventHandler
    fun onAnvilFill(e: PrepareAnvilEvent) {
        val inventory = e.inventory
        val firstItem = inventory.getItem(0)
        val secondItem = inventory.getItem(1)
        val secondItemMeta = secondItem?.itemMeta
        var repairCost = 0
        if (firstItem?.saveName != null) {
            e.result = null
        }
        if(firstItem != null){
            val result = firstItem.clone()
            val resultMeta = result.itemMeta
            if(inventory.renameText != resultMeta?.displayName?.replace("((§.)*)".toRegex(), "") && !firstItem.isRenamable){
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
                    firstItem.saveName == secondItem?.saveName
                    || secondItem?.type == Material.ENCHANTED_BOOK
                )
                && secondItem != null
            ){
                e.result = null
                e.inventory.repairCost = 0
                return
            }
            if(resultMeta != null && secondItem != null){
                repairCost = (2).toDouble().pow(firstItem.getNbt("repair-cost") ?: 0).toInt()
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
            result.updateCustomLore()
            resultMeta?.lore = resultMeta?.lore?.plus(listOf("§c$repairCost lvl requis")) ?: listOf("§c$repairCost lvl requis")
            result.itemMeta = resultMeta
            e.result = result
            inventory.repairCost = repairCost
            inventory.maximumRepairCost = 9999
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.server.scheduler.runTaskLaterAsynchronously(fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance, Runnable {
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
                e.currentItem!!.setNbt("repair-cost", e.currentItem!!.getNbt<Int>("repair-cost")?.plus(1) ?: 1)
                e.currentItem?.updateCustomLore()
            }
        }
    }
}