package me.rsman.BetterMinecraftCore.configs.models

import me.rsman.BetterMinecraftCore.BetterMinecraftCore.Companion.instance
import java.util.HashMap
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer
import org.bukkit.inventory.ShapelessRecipe

class BmcShapelessCraft {
    var ingredients: List<String>? = null
    var result: String? = null
    var name: String? = null
    var key: String? = null
    fun registerCraft() {
        assert(result != null)
        val resultItemFromScheme: Map.Entry<Char, RecipeChoice?>? = BmcCraftSubContainer.convertSchemeToRecipeChoice(result, 'X')
        if (resultItemFromScheme?.value == null) {
            instance.logger.warning("§cskipping shapeless recipe §3$name $key§c, result is invalid or not registered")
            return
        }
        val rec: ShapelessRecipe = when (resultItemFromScheme.value){
            is ExactChoice -> ShapelessRecipe(NamespacedKey.minecraft("bmc_shapeless_" + name!!.lowercase() + "_" + key), (resultItemFromScheme.value as ExactChoice?)!!.itemStack)
            is MaterialChoice -> ShapelessRecipe(NamespacedKey.minecraft("bmc_shapeless_" + name!!.lowercase() + "_" + key), (resultItemFromScheme.value as MaterialChoice?)!!.itemStack)
            else -> ShapelessRecipe(NamespacedKey.minecraft("bmc_shapeless_" + name!!.lowercase() + "_" + key), (resultItemFromScheme.value as ExactChoice?)!!.itemStack)
        }
        assert(ingredients != null)
        for (i in ingredients!!.toTypedArray().indices) {
            val itemFromScheme: Map.Entry<Char, RecipeChoice?>? = BmcCraftSubContainer.convertSchemeToRecipeChoice(ingredients!![i], 'X')
            if (itemFromScheme?.value == null) {
                instance.logger.warning("§cskipping shapeless recipe §3$name $key§c, one ingredient is invalid or not registered")
                return
            }
            rec.addIngredient(itemFromScheme.value!!)
        }
        instance.server.addRecipe(rec)
    }

    fun cloneForConfig(): BmcShapelessCraft {
        val sc = BmcShapelessCraft()
        sc.key = null
        sc.name = null
        sc.ingredients = ingredients
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
        var bmcCraftMap = bmcCraftSubContainer.shapeless
        if (bmcCraftMap == null) {
            bmcCraftMap = HashMap()
        }
        var bmcCraft = bmcCraftMap[key]
        if (bmcCraft == null) {
            bmcCraft = BmcShapelessCraft()
        }
        bmcCraft.key = key
        bmcCraft.name = name
        bmcCraft.ingredients = ingredients
        bmcCraft.result = result
        bmcCraftMap[key] = bmcCraft
        bmcCraftSubContainer.shapeless = bmcCraftMap
        bmcCraftSubContainerMap[name] = bmcCraftSubContainer
        BmcCraftContainer.instance?.recipes = bmcCraftSubContainerMap
        BmcCraftContainer.save()
        BmcCraftContainer.registerCrafts()
    }
}