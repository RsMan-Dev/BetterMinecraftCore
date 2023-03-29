package fr.rsman.betterMinecraftCore.configs.models

import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import fr.rsman.betterMinecraftCore.configs.containers.BmcItemContainer
import org.bukkit.Bukkit
import java.util.*

data class BmcCraftSubContainer(
    var shaped: MutableMap<String?, BmcShapedCraft?> = mutableMapOf(),
    var shapeless: MutableMap<String?, BmcShapelessCraft?> = mutableMapOf(),
) {
    fun cloneForConfig(): BmcCraftSubContainer {
        return this.copy(
            shaped = shaped.mapValues { it.value?.cloneForConfig() }.toMutableMap(),
            shapeless = shapeless.mapValues { it.value?.cloneForConfig() }.toMutableMap()
        )
    }

    companion object {
        @JvmStatic
        fun convertItemSchemeToItemStack(item: String): ItemStack? {
            return if (item == "m.AIR" || item == "AIR" || item == "null") {
                null
            } else if (item.startsWith("m.")) {
                if (Material.matchMaterial(item.substring(2)) == null) null else ItemStack(Objects.requireNonNull(Material.matchMaterial(item.substring(2)))!!)
            } else {
                if (BmcItemContainer.instance?.items?.containsKey(item) == true) {
                    BmcItemContainer.instance!!.items?.get(item)?.itemStack
                } else {
                    null
                }
            }
        }

        fun convertSchemeToRecipeChoice(scheme: String?, character: Char): Pair<Char, RecipeChoice?>? {
            val out: Pair<Char, RecipeChoice?>
            val splitted = scheme?.trim { it <= ' ' }?.split(" ".toRegex())?.toTypedArray() ?: return null
            val item = splitted[0]
            val number = if (splitted.size == 1) 1 else splitted[1].toInt()
            if (item == "m.AIR" || item == "AIR" || item == "null") {
                out = Pair(character, null)
            } else if (item.startsWith("m.")) {
                if (Material.matchMaterial(item.substring(2)) == null) return null
                out = Pair(character, ExactChoice(ItemStack(Objects.requireNonNull(Material.matchMaterial(item.substring(2)))!!, number)))
            } else if (item.startsWith("all.")) {
                var tag = Bukkit.getTag("blocks", NamespacedKey.minecraft(item.substring(4).lowercase()), Material::class.java)?:return null
                if (tag.values.toTypedArray().isEmpty()) {
                    tag = Bukkit.getTag("items", NamespacedKey.minecraft(item.substring(4).lowercase()), Material::class.java)?:return null
                }
                if (tag.values.toTypedArray().isEmpty()) {
                    tag = Bukkit.getTag("fluids", NamespacedKey.minecraft(item.substring(4).lowercase()), Material::class.java)?:return null
                }
                out = Pair(character, MaterialChoice(tag))
            } else {
                if (BmcItemContainer.instance?.items?.containsKey(item) == true) {
                    val temp: ItemStack = BmcItemContainer.instance?.items?.get(item)?.itemStack ?: return null
                    temp.amount = number
                    out = Pair(character, ExactChoice(temp))
                } else {
                    return null
                }
            }
            return out
        }
    }
}