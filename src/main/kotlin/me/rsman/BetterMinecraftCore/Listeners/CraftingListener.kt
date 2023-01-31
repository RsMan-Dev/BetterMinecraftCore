package me.rsman.BetterMinecraftCore.Listeners

import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe
import java.lang.Runnable
import org.bukkit.Material
import org.bukkit.GameMode
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.CraftingInventory
import me.rsman.BetterMinecraftCore.Managers.CraftManager
import me.rsman.BetterMinecraftCore.Managers.ItemManager
import me.rsman.BetterMinecraftCore.extensions.updateCustomLore
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.meta.Damageable
import java.util.*
import kotlin.math.floor

class CraftingListener : Listener {
    @EventHandler
    fun onCraftChange(e: PrepareItemCraftEvent) {
        val inv = e.inventory
        val matrix = e.inventory.matrix
        if(e.inventory.result != null) e.inventory.result!!.updateCustomLore()
        val ingredientMatrix: Array<ItemStack?>?
        if (e.recipe is ShapedRecipe) {
            ingredientMatrix = CraftManager.convertIngredientMapToMatrix(inv.matrix, (Objects.requireNonNull(inv.recipe) as ShapedRecipe).ingredientMap)
        } else if (e.recipe is ShapelessRecipe) {
            ingredientMatrix = CraftManager.convertIngredientListToMatrix(inv.matrix, (Objects.requireNonNull(inv.recipe) as ShapelessRecipe).ingredientList)
            if (ingredientMatrix == null) {
                e.inventory.result = null
                return
            }
        } else {
            return
        }
        var i = -1
        for (item in ingredientMatrix) {
            i++
            if (matrix[i] == null || item == null) continue
            if (matrix[i]!!.amount < item.amount) {
                e.inventory.result = null
            }
            if (matrix[i]!!.itemMeta != null && item.itemMeta != null) {
                val ingredientMeta = item.itemMeta!!.clone()
                val matrixMeta = Objects.requireNonNull(matrix[i]!!.itemMeta)!!.clone()
                if (ingredientMeta is Damageable) {
                    ingredientMeta.damage = 0
                    (matrixMeta as Damageable).damage = 0
                }
                if (ingredientMeta != matrixMeta) {
                    e.inventory.result = null
                }
            } else if (matrix[i]!!.itemMeta !== item.itemMeta) {
                e.inventory.result = null
            }
        }
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.inventory.holder is Player && (e.inventory.holder as Player?)!!.gameMode == GameMode.CREATIVE && (e.inventory.holder as Player?)!!.gameMode == GameMode.SPECTATOR) return
        if (e.inventory is CraftingInventory && e.clickedInventory != null) {
            val inv = e.inventory as CraftingInventory
            val isCraftInventory = (e.clickedInventory!!.javaClass.name.endsWith("CraftInventoryCrafting")
                    || e.clickedInventory!!.javaClass.name.endsWith("CraftInventoryPlayer"))
            if (isCraftInventory || e.isShiftClick) {
                if (isCraftInventory && e.slot == 0) {
                    return
                }
                BetterMinecraftCore.instance.server.scheduler.runTaskLater(BetterMinecraftCore.instance,
                        Runnable { inv.matrix = inv.matrix }, 1)
            }
        }
    }

    @EventHandler
    fun onCraft(e: CraftItemEvent) {
        if (e.whoClicked !is Player) return
        val p = e.whoClicked as Player
        val pinv = p.inventory
        val inv = e.inventory
        if (e.currentItem != null && e.currentItem != inv.result) {
            return
        }
        val newMatrix = inv.matrix
        val ingredientMatrix: Array<ItemStack?>?
        val result = Objects.requireNonNull(inv.result)!!.clone()
        if (e.recipe is ShapedRecipe) {
            ingredientMatrix = CraftManager.convertIngredientMapToMatrix(inv.matrix, (Objects.requireNonNull(inv.recipe) as ShapedRecipe).ingredientMap)
        } else if (e.recipe is ShapelessRecipe) {
            ingredientMatrix = CraftManager.convertIngredientListToMatrix(inv.matrix, (Objects.requireNonNull(inv.recipe) as ShapelessRecipe).ingredientList)
            if (ingredientMatrix == null) {
                e.isCancelled = true
                return
            }
        } else {
            return
        }
        e.isCancelled = true
        var maxItemsPossible = result.maxStackSize / result.amount
        var i = 0
        for (item in ingredientMatrix) {
            if (newMatrix[i] != null && item != null) {
                maxItemsPossible = maxItemsPossible.toDouble().coerceAtMost(floor(newMatrix[i]!!.amount.toDouble() / item.amount)).toInt()
            }
            i++
        }
        if (!e.isShiftClick) {
            if (maxItemsPossible >= 1) maxItemsPossible = 1
            if (p.itemOnCursor.type != Material.AIR) {
                if (p.itemOnCursor.hasItemMeta() && result.hasItemMeta()) {
                    if (p.itemOnCursor.itemMeta != result.itemMeta) return
                }
                if (p.itemOnCursor.amount + maxItemsPossible > p.itemOnCursor.maxStackSize) return
                p.itemOnCursor.amount = p.itemOnCursor.amount + maxItemsPossible * result.amount
            } else {
                p.setItemOnCursor(result)
            }
        } else {
            result.amount = maxItemsPossible * result.amount
            val notadded: Map<Int, ItemStack> = pinv.addItem(result)
            if (notadded.isNotEmpty()) {
                maxItemsPossible -= notadded[0]!!.amount
            }
        }
        i = 0
        for (item in ingredientMatrix) {
            if (newMatrix[i] != null) {
                val newAmount = newMatrix[i]!!.amount - item!!.amount * maxItemsPossible
                if (newAmount <= 0) {
                    newMatrix[i] = null
                } else {
                    newMatrix[i]!!.amount = newAmount
                }
            }
            i++
        }
        inv.matrix = newMatrix
    }
}