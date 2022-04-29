package me.rsman.BetterMinecraftCore.Managers

import me.rsman.BetterMinecraftCore.configs.models.BmcCraftSubContainer.Companion.convertItemSchemeToItemStack
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import co.aikar.commands.CommandIssuer
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager
import me.rsman.BetterMinecraftCore.Managers.Command.Lang.MessageKeys
import java.util.*

object CraftManager {
    fun convertIngredientMapToMatrix(model: Array<ItemStack?>, ingredients: MutableMap<Char?, ItemStack?>): Array<ItemStack?> {
        val returned = arrayOfNulls<ItemStack>(model.size)
        ingredients.values.removeAll(setOf<Any?>(null))
        for ((i, item) in model.withIndex()) {
            if (item == null) {
                returned[i] = null
            } else {
                val optionnal: Optional<MutableMap.MutableEntry<Char?, ItemStack?>> = ingredients.entries.stream().findFirst()
                if (!optionnal.isPresent) {
                    returned[i] = null
                } else {
                    val (key, value) = optionnal.get()
                    ingredients.remove(key)
                    returned[i] = value
                }
            }
        }
        return returned
    }

    fun convertIngredientListToMatrix(model: Array<ItemStack?>, ingredients: MutableList<ItemStack>): Array<ItemStack?>? {
        val returned: MutableList<ItemStack?> = mutableListOf()
        for (item in model) {
            if (item == null) {
                returned.add(null)
            } else {
                var index = -1
                for (j in ingredients.toTypedArray().indices) {
                    if (item.itemMeta != null && ingredients[j].itemMeta != null) {
                        if (item.itemMeta == ingredients[j].itemMeta) {
                            if (item.amount >= ingredients[j].amount) {
                                if (index != -1) {
                                    if (ingredients[j].amount > ingredients[index].amount) {
                                        index = j
                                    }
                                } else {
                                    index = j
                                }
                            }
                        }
                    }
                }
                if (index == -1) return null
                returned.add(ingredients.removeAt(index))
            }
        }
        return returned.toTypedArray()
    }

    fun openCraftingEditorInventory(commandManager: CommandManager, playerSender: Player, NAMESPACE_KEY: NamespacedKey?, result: String?, nameKeyPair: String, resultCount: Int?, type: String, force: String?) {
        var result = result
        var resultCount = resultCount
        val issuerSender: CommandIssuer = commandManager.getCommandIssuer(playerSender)
        val nameKeyPairDettached = nameKeyPair.split("\\.".toRegex()).toTypedArray()
        if (nameKeyPairDettached.size != 2) {
            issuerSender.sendInfo(MessageKeys.INVALID_NAME_KEY_PAIR)
            return
        }
        val r = Bukkit.getRecipe(NamespacedKey.minecraft("bmc_" + type + "_" + nameKeyPairDettached[0].lowercase(Locale.getDefault()) + "_" + nameKeyPairDettached[1]))
        if (result != null) {
            val querriedItem = convertItemSchemeToItemStack(result)
            if (querriedItem == null) {
                issuerSender.sendInfo(MessageKeys.INVALID_RESULT)
                return
            }
            var needToForce = false
            if (r != null) {
                if (querriedItem == r.result) {
                    if (querriedItem.hasItemMeta() && r.result.hasItemMeta()) {
                        val querriedItemMeta = Objects.requireNonNull(querriedItem.itemMeta)!!.clone()
                        val resultMeta = Objects.requireNonNull(r.result.itemMeta)!!.clone()
                        if (querriedItemMeta != resultMeta) needToForce = true
                    }
                } else {
                    needToForce = true
                }
            }
            if (needToForce) {
                if (force == null || force != "force") {
                    issuerSender.sendInfo(MessageKeys.NEED_TO_FORCE)
                    return
                }
            }
        } else if (r == null) {
            issuerSender.sendInfo(MessageKeys.NEED_RESULT)
            return
        } else {
            val resultItem = r.result
            result = if (ItemManager.getItemName(resultItem) == "") "m." + resultItem.type.name.uppercase(Locale.ENGLISH) else ItemManager.getItemName(resultItem)
            resultCount = resultItem.amount
        }
        val cInv = Bukkit.createInventory(playerSender, InventoryType.DISPENSER, "Craft $nameKeyPair")
        if (r != null) {
            val itemList: List<ItemStack> = if (type == "shaped") {
                ArrayList((r as ShapedRecipe).ingredientMap.values)
            } else {
                (r as ShapelessRecipe).ingredientList
            }
            for ((i, item) in itemList.withIndex()) {
                cInv.setItem(i, item.clone())
            }
        }
        playerSender.openInventory(cInv)
        if (resultCount == null) resultCount = 1
        playerSender.persistentDataContainer.set(NAMESPACE_KEY!!, PersistentDataType.STRING, "$nameKeyPair|$result|$resultCount|$type")
    }
}