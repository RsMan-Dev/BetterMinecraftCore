package fr.rsman.betterMinecraftCore.configs.containers

import fr.rsman.betterMinecraftCore.configs.ConfigLoader

data class AttributeLangContainer(
    var translations: Map<String?, String?>? = null
) {
    companion object {
        @JvmStatic
        var instance: AttributeLangContainer? = null
            private set

        fun load() {
            instance = null
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.logger.info("§3Loading Attributes lang...")
            val enchantLangContainerInstance = ConfigLoader.loadConfig("lang/attributes", AttributeLangContainer::class.java)
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.logger.info("§bLoaded Attributes lang.")
            instance = enchantLangContainerInstance
        }
    }
}