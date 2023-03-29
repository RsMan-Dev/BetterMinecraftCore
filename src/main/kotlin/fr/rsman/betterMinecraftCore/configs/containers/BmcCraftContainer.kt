package fr.rsman.betterMinecraftCore.configs.containers

import fr.rsman.betterMinecraftCore.BetterMinecraftCore
import fr.rsman.betterMinecraftCore.configs.models.BmcCraftSubContainer
import fr.rsman.betterMinecraftCore.configs.ConfigLoader
import org.bukkit.Bukkit
import org.bukkit.inventory.ShapedRecipe

data class BmcCraftContainer(
    var recipes: MutableMap<String?, BmcCraftSubContainer> = mutableMapOf(),
) {

    fun cloneForConfig(): BmcCraftContainer {
        return this.copy(
            recipes = recipes.mapValues { it.value.cloneForConfig() }.toMutableMap()
        )
    }

    companion object {
        var instance: BmcCraftContainer? = null
            private set
        private var shapedKeys = mutableListOf<String>()
        @JvmStatic fun getShapedKeys(): List<String> = shapedKeys

        private var shapelessKeys = mutableListOf<String>()
        @JvmStatic fun getShapelessKeys(): List<String> = shapelessKeys
        @JvmStatic fun getKeys(): List<String> = shapedKeys + shapelessKeys


        @JvmStatic
        fun load() {
            instance = null
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§3Loading BMC crafts...")
            val bmcCraftContainerInstance =
                ConfigLoader.loadConfig("crafts/all", BmcCraftContainer::class.java)
                    ?: return fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.severe("§4Crafts cannot be loaded")

            shapedKeys.clear()
            shapelessKeys.clear()

            for ((name, value) in bmcCraftContainerInstance.recipes) {
                for ((key, shapedCraftObj) in value.shaped) {
                    bmcCraftContainerInstance.recipes[name]?.shaped?.set(key, shapedCraftObj?.copy(name = name, key = key))
                    shapedKeys.add("$name.$key")
                }

                for ((key, shapelessCraftObj) in value.shapeless) {
                    bmcCraftContainerInstance.recipes[name]?.shapeless?.set(key, shapelessCraftObj?.copy(name = name, key = key))
                    shapelessKeys.add("$name.$key")
                }
            }
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§bLoaded §6" + getKeys().size + " §bcrafts.")
            if (GlobalConfigContainer.instance?.isVerbose == true) {
                fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§bLoaded crafts: §6${getKeys()}")
            }
            instance = bmcCraftContainerInstance
        }

        fun save() {
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§3Saving BMC crafts...")
            val bmcCraftContainerClone = instance?.cloneForConfig()
                ?: return fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.severe("§bBMC Crafts Instance not set.")
            ConfigLoader.saveConfig("crafts/all", bmcCraftContainerClone)
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§bSaved BMC crafts.")
        }

        @JvmStatic
        fun registerCrafts() {
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.server.resetRecipes()
            if (GlobalConfigContainer.instance?.isVerbose == true) {
                fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info { instance.toString() }
            }
            for ((_, value) in instance!!.recipes) {
                for ((_, value1) in value.shaped) {
                    value1!!.registerCraft()
                }
                for ((_, value1) in value.shapeless) {
                    value1!!.registerCraft()
                }
            }
            Bukkit.recipeIterator().forEach {
                if (it is ShapedRecipe && it.key.toString() == "minecraft:diamond_helmet") {
                    BetterMinecraftCore.logger.info(it.shape.toList().toString())
                    BetterMinecraftCore.logger.info(it.choiceMap.toString())
                }
                if (it is ShapedRecipe && it.key.toString().startsWith("minecraft:bmc_")) {
                    BetterMinecraftCore.logger.info(it.shape.toList().toString())
                    BetterMinecraftCore.logger.info(it.choiceMap.toString())
                }
            }
        }
    }
}