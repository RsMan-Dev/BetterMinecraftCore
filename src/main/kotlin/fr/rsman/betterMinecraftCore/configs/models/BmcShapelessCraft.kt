package fr.rsman.betterMinecraftCore.configs.models

import fr.rsman.betterMinecraftCore.BetterMinecraftCore.Companion.instance
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import fr.rsman.betterMinecraftCore.configs.containers.BmcCraftContainer
import org.bukkit.inventory.ShapelessRecipe

data class BmcShapelessCraft(
    var ingredients: List<String>? = null,
    var result: String? = null,
    var name: String? = null,
    var key: String? = null
) {
    fun registerCraft() {
        val resultItemFromScheme: Pair<Char, RecipeChoice?>? = BmcCraftSubContainer.convertSchemeToRecipeChoice(result, 'X')
        resultItemFromScheme?.second ?: return instance.logger.warning("§cSkipping shapeless recipe §3$name $key§c, result is invalid or not registered")

        val rec: ShapelessRecipe = when (resultItemFromScheme.second){
            is MaterialChoice -> ShapelessRecipe(NamespacedKey.minecraft("bmc_shapeless_" + name!!.lowercase() + "_" + key), (resultItemFromScheme.second as MaterialChoice?)!!.itemStack)
            else -> ShapelessRecipe(NamespacedKey.minecraft("bmc_shapeless_" + name!!.lowercase() + "_" + key), (resultItemFromScheme.second as ExactChoice?)!!.itemStack)
        }

        for (ing in ingredients ?: return instance.logger.warning("§cSkipping shapeless recipe §3$name $key§c, ingredients not defined")) {
            val itemFromSchemeRc: RecipeChoice = BmcCraftSubContainer.convertSchemeToRecipeChoice(ing, 'X')?.second
                ?: return instance.logger.warning("§cskipping shapeless recipe §3$name $key§c, one ingredient is invalid or not registered")
            rec.addIngredient(itemFromSchemeRc)
        }
        instance.server.addRecipe(rec)
    }

    fun cloneForConfig(): BmcShapelessCraft = this.copy(key = null, name = null)

    fun registerSelfInConfig() {
        BmcCraftContainer.instance?.recipes!![name] = BmcCraftContainer.instance?.recipes!![name] ?: BmcCraftSubContainer()
        BmcCraftContainer.instance?.recipes!![name]!!.shapeless[key] = BmcCraftContainer.instance?.recipes!![name]!!.shapeless[key]?.copy(
            key = key,
            name = name,
            ingredients = ingredients,
            result = result
        ) ?: BmcShapelessCraft(
            key = key,
            name = name,
            ingredients = ingredients,
            result = result
        )
        BmcCraftContainer.save()
        BmcCraftContainer.registerCrafts()
    }
}