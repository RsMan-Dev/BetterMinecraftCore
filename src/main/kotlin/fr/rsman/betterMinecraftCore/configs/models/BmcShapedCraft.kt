package fr.rsman.betterMinecraftCore.configs.models

import fr.rsman.betterMinecraftCore.BetterMinecraftCore.Companion.instance
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import fr.rsman.betterMinecraftCore.configs.containers.BmcCraftContainer

data class BmcShapedCraft(
    var shape: List<String>? = null,
    var result: String? = null,
    var name: String? = null,
    var key: String? = null,
) {
    fun registerCraft() {
        val shapeArr = arrayOf("", "", "")
        val ingredients = mutableMapOf<Char, RecipeChoice>()
        val resultItemFromScheme = BmcCraftSubContainer.convertSchemeToRecipeChoice(result, 'X')
        resultItemFromScheme?.second ?: return instance.logger.warning("§cSkipping shaped recipe §3$name $key§c, result is invalid or not registered")

        val rec: ShapedRecipe = when(resultItemFromScheme.second){
            is MaterialChoice -> ShapedRecipe(NamespacedKey.minecraft("bmc_shaped_" + name!!.lowercase() + "_" + key), (resultItemFromScheme.second as MaterialChoice?)!!.itemStack)
            else -> ShapedRecipe(NamespacedKey.minecraft("bmc_shaped_" + name!!.lowercase() + "_" + key), (resultItemFromScheme.second as ExactChoice?)!!.itemStack)
        }
        for (i in shape?.indices ?: return instance.logger.warning("§cSkipping shaped recipe §3$name $key§c, shape not defined")) {
            for (itemScheme in shape!![i].split("|")) {
                val itemFromScheme = BmcCraftSubContainer.convertSchemeToRecipeChoice(itemScheme, (97 + i).toChar())
                    ?: return instance.logger.warning("§cskipping shaped recipe §3$name $key&c, some ingredients are invalid or not registered")
                shapeArr[i] = shapeArr[i] + itemFromScheme.first
                ingredients[itemFromScheme.first] = itemFromScheme.second ?: continue
            }
        }
        rec.shape(shapeArr[0], shapeArr[1], shapeArr[2])
        for ((key1, value) in ingredients) rec.setIngredient(key1, value)
        instance.server.addRecipe(rec)
    }

    fun cloneForConfig() = this.copy(key = null, name= null)

    fun registerSelfInConfig() {
        BmcCraftContainer.instance?.recipes!![name] = BmcCraftContainer.instance?.recipes!![name] ?: BmcCraftSubContainer()
        BmcCraftContainer.instance?.recipes!![name]!!.shaped[key] = BmcCraftContainer.instance?.recipes!![name]!!.shaped[key]?.copy(
            key = key,
            name = name,
            shape = shape,
            result = result
        ) ?: BmcShapedCraft(
            key = key,
            name = name,
            shape = shape,
            result = result
        )
        BmcCraftContainer.save()
        BmcCraftContainer.registerCrafts()
    }
}