package fr.rsman.betterMinecraftCore.configs.containers

import fr.rsman.betterMinecraftCore.configs.ConfigLoader

data class EnchantLangContainer(
    var translations: Map<String?, String?>? = null
) {

    companion object {
        @JvmStatic
        var instance: EnchantLangContainer? = null
            private set

        fun load() {
            instance = null
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§3Loading Enchants lang...")
            val enchantLangContainerInstance = ConfigLoader.loadConfig("lang/enchants", EnchantLangContainer::class.java)
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§bLoaded Enchants lang.")
            instance = enchantLangContainerInstance
        }
    }
}