package fr.rsman.betterMinecraftCore.configs.containers

import fr.rsman.betterMinecraftCore.configs.models.BmcItem
import fr.rsman.betterMinecraftCore.configs.ConfigLoader
import fr.rsman.betterMinecraftCore.interfaces.ItemDropPattern

data class BmcItemContainer(
    var items: MutableMap<String, BmcItem?>? = null
) {

    fun cloneForConfig(): BmcItemContainer {
        return this.copy(items = items?.mapValues { it.value?.cloneForConfig() }?.toMutableMap())
    }

    companion object {
        val blockLootTable = mutableMapOf<String, MutableList<Pair<ItemDropPattern, BmcItem>>>()
        val entityLootTable = mutableMapOf<String, MutableList<Pair<ItemDropPattern, BmcItem>>>()

        @JvmStatic
        var instance: BmcItemContainer? = null
            private set

        fun load() {
            instance = null
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§3Loading BMC items...")
            val bmcItemContainerInstance = ConfigLoader.loadConfig("items/all", BmcItemContainer::class.java)
            if (bmcItemContainerInstance == null) {
                fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.severe("§4Items cannot be loaded")
            } else {
                val keys: MutableList<String> = mutableListOf()
                for ((key, item) in bmcItemContainerInstance.items!!) {
                    item?.name = key
                    keys.add(key)
                    if(item?.dropsFromBlock != null ) for(dropPattern in item.dropsFromBlock!!){
                        val pat = ItemDropPattern.parsePattern(dropPattern) ?: continue
                        blockLootTable[pat.patternId] = blockLootTable[pat.patternId]
                            ?.plus(Pair(pat, item))?.toMutableList()
                            ?: mutableListOf(Pair(pat, item))
                    }
                    if(item?.dropsFromEntity != null ) for(dropPattern in item.dropsFromEntity!!){
                        val pat = ItemDropPattern.parsePattern(dropPattern) ?: continue
                        entityLootTable[pat.patternId] = entityLootTable[pat.patternId]
                            ?.plus(Pair(pat, item))?.toMutableList()
                            ?: mutableListOf(Pair(pat, item))
                    }
                }
                fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§bLoaded §6" + keys.size + " §bitems.")
                if (GlobalConfigContainer.instance?.isVerbose == true) {
                    fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§bLoaded items: §6$keys")
                }
            }
            instance = bmcItemContainerInstance
        }

        fun save() {
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§3Saving BMC items...")
            val bmcItemsContainerClone = instance?.cloneForConfig()
                ?: return fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.severe("§bBMC Items Instance not set.")
            ConfigLoader.saveConfig("items/all", bmcItemsContainerClone)
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§bSaved BMC items.")
        }
    }
}