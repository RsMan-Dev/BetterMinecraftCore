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

class GlobalConfigContainer {
    var isVerbose = false
    var db_host: String? = null
    var db_port: String? = null
    var db_database: String? = null
    var db_user: String? = null
    var db_password: String? = null
    var attribute_display_format: String? = null
    var attribute_modifier_display_format: String? = null
    var enchant_display_format: String? = null
    var enchant_separator_display_format: String? = null
    var action_bar_display_format: String? = null

    companion object {
        @JvmStatic
        var instance: GlobalConfigContainer? = null
            private set

        fun load() {
            instance = null
            BetterMinecraftCore.instance.logger.info("§3Loading BMC config...")
            val globalConfigContainerInstance = ConfigLoader.loadConfig("global", GlobalConfigContainer::class.java)
            if (globalConfigContainerInstance == null) {
                BetterMinecraftCore.instance.logger.severe("§4Config cannot be loaded")
            } else {
                BetterMinecraftCore.instance.logger.info("§bLoaded global config.")
                BetterMinecraftCore.instance.logger.info("§bverbose mode §6" + if (globalConfigContainerInstance.isVerbose) "on" else "off")
            }
            instance = globalConfigContainerInstance
        }
    }
}