package me.rsman.BetterMinecraftCore.configs.containers

import java.util.HashMap
import me.rsman.BetterMinecraftCore.configs.models.BmcCraftSubContainer
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.configs.ConfigLoader
import java.util.ArrayList

class BmcCraftContainer {
    var recipes: MutableMap<String?, BmcCraftSubContainer?>? = null
    fun cloneForConfig(): BmcCraftContainer {
        val cc = BmcCraftContainer()
        val craftsClone = HashMap<String?, BmcCraftSubContainer?>()
        for ((key, value) in recipes!!) {
            craftsClone[key] = value!!.cloneForConfig()
        }
        cc.recipes = craftsClone
        return cc
    }

    companion object {
        var instance: BmcCraftContainer? = null
            private set
        private var keys: MutableList<String>? = null
        private var shapedKeys: MutableList<String>? = null
        private var shapelessKeys: MutableList<String>? = null
        fun getKeys(): List<String>? {
            return keys
        }

        @JvmStatic
        fun getShapedKeys(): List<String>? {
            return shapedKeys
        }

        @JvmStatic
        fun getShapelessKeys(): List<String>? {
            return shapelessKeys
        }

        @JvmStatic
        fun load() {
            instance = null
            BetterMinecraftCore.instance.logger.info("§3Loading BMC crafts...")
            val bmcCraftContainerInstance = ConfigLoader.loadConfig("crafts/all", BmcCraftContainer::class.java)
            if (bmcCraftContainerInstance == null) {
                BetterMinecraftCore.instance.logger.severe("§4Crafts cannot be loaded")
            } else {
                keys = ArrayList()
                shapedKeys = ArrayList()
                shapelessKeys = ArrayList()
                for ((key, value) in bmcCraftContainerInstance.recipes!!) {
                    if (value?.shaped != null) {
                        for ((key1, shapedCraftObj) in value.shaped!!) {
                            shapedCraftObj?.name = key
                            shapedCraftObj?.key = key1
                            bmcCraftContainerInstance.recipes!![key]?.shaped?.set(key1, shapedCraftObj)
                            keys?.add(shapedCraftObj?.name + "." + shapedCraftObj?.key)
                            shapedKeys?.add(shapedCraftObj?.name + "." + shapedCraftObj?.key)
                        }
                    }
                    if (value?.shapeless != null) {
                        for ((key1, shapelessCraftObj) in value.shapeless!!) {
                            shapelessCraftObj?.name = key
                            shapelessCraftObj?.key = key1
                            bmcCraftContainerInstance.recipes!![key]?.shapeless?.set(key1, shapelessCraftObj)
                            keys?.add(shapelessCraftObj?.name + "." + shapelessCraftObj?.key)
                            shapelessKeys?.add(shapelessCraftObj?.name + "." + shapelessCraftObj?.key)
                        }
                    }
                }
                BetterMinecraftCore.instance.logger.info("§bLoaded §6" + keys?.size + " §bcrafts.")
                if (GlobalConfigContainer.instance?.isVerbose == true) {
                    BetterMinecraftCore.instance.logger.info("§bLoaded crafts: §6$keys")
                }
            }
            instance = bmcCraftContainerInstance
        }

        fun save() {
            BetterMinecraftCore.instance.logger.info("§3Saving BMC crafts...")
            val bmcCraftContainerClone = instance!!.cloneForConfig()
            ConfigLoader.saveConfig("crafts/all", bmcCraftContainerClone)
            BetterMinecraftCore.instance.logger.info("§bSaved BMC crafts.")
        }

        @JvmStatic
        fun registerCrafts() {
            BetterMinecraftCore.instance.server.resetRecipes()
            for ((_, value) in instance!!.recipes!!) {
                if (value?.shaped != null) {
                    for ((_, value1) in value.shaped!!) {
                        value1!!.registerCraft()
                    }
                }
                if (value?.shapeless != null) {
                    for ((_, value1) in value.shapeless!!) {
                        value1!!.registerCraft()
                    }
                }
            }
        }
    }
}