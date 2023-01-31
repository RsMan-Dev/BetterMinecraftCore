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
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor
import java.io.FileInputStream
import org.yaml.snakeyaml.DumperOptions
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.FileOutputStream
import org.yaml.snakeyaml.representer.Representer
import org.yaml.snakeyaml.nodes.NodeTuple

class EnchantLangContainer {
    var translations: Map<String?, String?>? = null
        private set

    fun setTranslations(translations: MutableMap<String?, String?>) {
        for ((key) in translations) {
            if (!EEnchants.keys.contains(key)) {
                translations.remove(key)
            }
        }
        this.translations = translations
    }

    fun getTranslation(key: String?): String? {
        return translations!![key]
    }

    companion object {
        @JvmStatic
        var instance: EnchantLangContainer? = null
            private set

        fun load() {
            instance = null
            BetterMinecraftCore.instance.logger.info("§3Loading Enchants lang...")
            val enchantLangContainerInstance = ConfigLoader.loadConfig("lang/enchants", EnchantLangContainer::class.java)
            BetterMinecraftCore.instance.logger.info("§bLoaded Enchants lang.")
            instance = enchantLangContainerInstance
        }
    }
}