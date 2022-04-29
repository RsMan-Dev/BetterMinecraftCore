package me.rsman.BetterMinecraftCore.configs.models

import me.rsman.BetterMinecraftCore.BetterMinecraftCore.Companion.instance
import me.rsman.BetterMinecraftCore.enums.EAttributes
import me.rsman.BetterMinecraftCore.enums.EEnchants
import me.rsman.BetterMinecraftCore.configs.models.BmcItem
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import me.rsman.BetterMinecraftCore.Managers.ItemManager
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.RecipeChoice
import me.rsman.BetterMinecraftCore.configs.models.BmcCraftSubContainer
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import me.rsman.BetterMinecraftCore.configs.models.BmcShapedCraft
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer
import org.bukkit.inventory.ShapelessRecipe
import me.rsman.BetterMinecraftCore.configs.models.BmcShapelessCraft
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer
import java.util.AbstractMap.SimpleEntry
import org.bukkit.Bukkit
import me.rsman.BetterMinecraftCore.configs.ConfigLoader
import me.rsman.BetterMinecraftCore.configs.containers.GlobalConfigContainer
import me.rsman.BetterMinecraftCore.configs.containers.EnchantLangContainer
import me.rsman.BetterMinecraftCore.configs.containers.AttributeLangContainer
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor
import java.io.FileInputStream
import org.yaml.snakeyaml.DumperOptions
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.FileOutputStream
import org.yaml.snakeyaml.representer.Representer
import org.yaml.snakeyaml.nodes.NodeTuple
import java.util.*

class BmcCraftSubContainer {
    var shaped: MutableMap<String?, BmcShapedCraft?>? = null
    var shapeless: MutableMap<String?, BmcShapelessCraft?>? = null
    fun cloneForConfig(): BmcCraftSubContainer {
        val csc = BmcCraftSubContainer()
        if (shaped != null) {
            val shapedClone = HashMap<String?, BmcShapedCraft?>()
            for ((key, value) in shaped!!) {
                shapedClone[key] = value!!.cloneForConfig()
            }
            csc.shaped = shapedClone
        }
        if (shapeless != null) {
            val shapelessClone = HashMap<String?, BmcShapelessCraft?>()
            for ((key, value) in shapeless!!) {
                shapelessClone[key] = value!!.cloneForConfig()
            }
            csc.shapeless = shapelessClone
        }
        return csc
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

        fun convertSchemeToRecipeChoice(scheme: String?, character: Char): Map.Entry<Char, RecipeChoice?>? {
            val out: Map.Entry<Char, RecipeChoice?>
            val splitted = scheme!!.trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()
            val item = splitted[0]
            val number = if (splitted.size == 1) 1 else splitted[1].toInt()
            if (item == "m.AIR" || item == "AIR" || item == "null") {
                out = SimpleEntry<Char, RecipeChoice?>(' ', null)
            } else if (item.startsWith("m.")) {
                if (Material.matchMaterial(item.substring(2)) == null) return null
                out = SimpleEntry<Char, RecipeChoice?>(character, ExactChoice(ItemStack(Objects.requireNonNull(Material.matchMaterial(item.substring(2)))!!, number)))
            } else if (item.startsWith("all.")) {
                var tag = Bukkit.getTag("blocks", NamespacedKey.minecraft(item.substring(4).toLowerCase()), Material::class.java)?:return null
                if (tag.values.toTypedArray().size == 0) {
                    tag = Bukkit.getTag("items", NamespacedKey.minecraft(item.substring(4).toLowerCase()), Material::class.java)?:return null
                }
                if (tag.values.toTypedArray().size == 0) {
                    tag = Bukkit.getTag("fluids", NamespacedKey.minecraft(item.substring(4).toLowerCase()), Material::class.java)?:return null
                }
                out = SimpleEntry<Char, RecipeChoice?>(character, MaterialChoice(tag))
            } else {
                if (BmcItemContainer.instance?.items?.containsKey(item) == true) {
                    val temp: ItemStack = BmcItemContainer.instance?.items?.get(item)?.itemStack ?: return null
                    temp.amount = number
                    out = SimpleEntry<Char, RecipeChoice?>(character, ExactChoice(temp))
                } else {
                    return null
                }
            }
            return out
        }
    }
}