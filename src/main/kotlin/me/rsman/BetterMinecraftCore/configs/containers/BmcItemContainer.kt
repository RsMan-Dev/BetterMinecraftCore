package me.rsman.BetterMinecraftCore.configs.containers

import me.rsman.BetterMinecraftCore.BetterMinecraftCore.Companion.instance
import java.util.HashMap
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
import me.rsman.BetterMinecraftCore.interfaces.ItemDropPattern
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor
import java.io.FileInputStream
import org.yaml.snakeyaml.DumperOptions
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.FileOutputStream
import org.yaml.snakeyaml.representer.Representer
import org.yaml.snakeyaml.nodes.NodeTuple
import java.util.ArrayList

class BmcItemContainer {
    var items: HashMap<String, BmcItem?>? = null
    fun cloneForConfig(): BmcItemContainer {
        val ic = BmcItemContainer()
        val itemsClone = HashMap<String, BmcItem?>()
        for ((key, value) in items!!) {
            itemsClone[key] = value!!.cloneForConfig()
        }
        ic.items = itemsClone
        return ic
    }

    companion object {
        @JvmStatic
        var instance: BmcItemContainer? = null
            private set

        fun load() {
            instance = null
            BetterMinecraftCore.instance.logger.info("§3Loading BMC items...")
            val bmcItemContainerInstance = ConfigLoader.loadConfig("items/all", BmcItemContainer::class.java)
            if (bmcItemContainerInstance == null) {
                BetterMinecraftCore.instance.logger.severe("§4Items cannot be loaded")
            } else {
                val keys: MutableList<String> = ArrayList()
                for ((key, item) in bmcItemContainerInstance.items!!) {
                    item!!.setName(key)
                    bmcItemContainerInstance.items!![key] = item
                    keys.add(key)
                    item.getDropsFromBlock()?.forEach { d ->
                        val pat = ItemDropPattern.parsePattern(d)
                        if(pat != null){
                            if(ItemManager.blockLootTable[pat.patternId] == null){
                                ItemManager.blockLootTable[pat.patternId] = mutableListOf(Pair(pat, item))
                            } else {
                                ItemManager.blockLootTable[pat.patternId]?.plusAssign(Pair(pat, item))
                            }
                        }
                    }
                    item.getDropsFromEntity()?.forEach { d ->
                        val pat = ItemDropPattern.parsePattern(d)
                        if(pat != null){
                            if(ItemManager.entityLootTable[pat.patternId] == null){
                                ItemManager.entityLootTable[pat.patternId] = mutableListOf(Pair(pat, item))
                            } else {
                                ItemManager.entityLootTable[pat.patternId]?.plusAssign(Pair(pat, item))
                            }
                        }
                    }
                }
                BetterMinecraftCore.instance.logger.info("§bLoaded §6" + keys.size + " §bitems.")
                if (GlobalConfigContainer.instance?.isVerbose == true) {
                    BetterMinecraftCore.instance.logger.info("§bLoaded items: §6$keys")
                }
            }
            instance = bmcItemContainerInstance
        }

        fun save() {
            BetterMinecraftCore.instance.logger.info("§3Saving BMC items...")
            val bmcItemsContainerClone = instance!!.cloneForConfig()
            ConfigLoader.saveConfig("items/all", bmcItemsContainerClone)
            BetterMinecraftCore.instance.logger.info("§bSaved BMC items.")
        }
    }
}