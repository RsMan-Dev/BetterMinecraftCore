package me.rsman.BetterMinecraftCore.configs.models

import me.rsman.BetterMinecraftCore.BetterMinecraftCore.Companion.instance
import java.util.HashMap
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer

class BmcShapedCraft {
    var shape: List<String>? = null
    var result: String? = null
    var name: String? = null
    var key: String? = null
    fun registerCraft() {
        val shapeArr = arrayOf("", "", "")
        val ingredients: MutableMap<Char, RecipeChoice?> = HashMap()
        assert(result != null)
        val resultItemFromScheme: Map.Entry<Char, RecipeChoice?>? = BmcCraftSubContainer.convertSchemeToRecipeChoice(result, 'X')
        if (resultItemFromScheme?.value == null) {
            instance.logger.warning("§cskipping shaped recipe §3$name $key§c, result is invalid or not registered")
            return
        }
        val rec: ShapedRecipe = when(resultItemFromScheme.value){
            is MaterialChoice -> ShapedRecipe(NamespacedKey.minecraft("bmc_shaped_" + name!!.lowercase() + "_" + key), (resultItemFromScheme.value as MaterialChoice?)!!.itemStack)
            is ExactChoice -> ShapedRecipe(NamespacedKey.minecraft("bmc_shaped_" + name!!.lowercase() + "_" + key), (resultItemFromScheme.value as ExactChoice?)!!.itemStack)
            else -> ShapedRecipe(NamespacedKey.minecraft("bmc_shaped_" + name!!.lowercase() + "_" + key), (resultItemFromScheme.value as ExactChoice?)!!.itemStack)
        }
        assert(shape != null)
        for (i in shape!!.toTypedArray().indices) {
            val itemSchemes = shape!![i].split("\\|".toRegex()).toTypedArray()
            for (itemScheme in itemSchemes) {
                val itemFromScheme: Map.Entry<Char, RecipeChoice?>? = BmcCraftSubContainer.convertSchemeToRecipeChoice(itemScheme, (97 + i).toChar())
                if (itemFromScheme == null) {
                    instance.logger.warning("§cskipping shaped recipe §3$name $key&c, some ingredients are invalid or not registered")
                    return
                }
                shapeArr[i] = shapeArr[i] + itemFromScheme.key
                if (itemFromScheme.value != null) ingredients[itemFromScheme.key] = itemFromScheme.value
            }
        }
        rec.shape(shapeArr[0], shapeArr[1], shapeArr[2])
        for ((key1, value) in ingredients) {
            rec.setIngredient(key1, value!!)
        }
        instance.server.addRecipe(rec)
    }

    fun cloneForConfig(): BmcShapedCraft {
        val sc = BmcShapedCraft()
        sc.key = null
        sc.name = null
        sc.shape = shape
        sc.result = result
        return sc
    }

    fun registerSelfInConfig() {
        var bmcCraftSubContainerMap: MutableMap<String?, BmcCraftSubContainer?>? = BmcCraftContainer.instance?.recipes
        if (bmcCraftSubContainerMap == null) {
            bmcCraftSubContainerMap = HashMap()
        }
        var bmcCraftSubContainer = bmcCraftSubContainerMap[name]
        if (bmcCraftSubContainer == null) {
            bmcCraftSubContainer = BmcCraftSubContainer()
        }
        var bmcCraftMap = bmcCraftSubContainer.shaped
        if (bmcCraftMap == null) {
            bmcCraftMap = HashMap()
        }
        var bmcCraft = bmcCraftMap[key]
        if (bmcCraft == null) {
            bmcCraft = BmcShapedCraft()
        }
        bmcCraft.key = key
        bmcCraft.name = name
        bmcCraft.shape = shape
        bmcCraft.result = result
        bmcCraftMap[key] = bmcCraft
        bmcCraftSubContainer.shaped = bmcCraftMap
        bmcCraftSubContainerMap[name] = bmcCraftSubContainer
        BmcCraftContainer.instance?.recipes = bmcCraftSubContainerMap
        BmcCraftContainer.save()
        BmcCraftContainer.registerCrafts()
    }
}