package me.rsman.BetterMinecraftCore.configs.containers

import me.rsman.BetterMinecraftCore.enums.EAttributes
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.configs.ConfigLoader

class AttributeLangContainer {
    var translations: Map<String?, String?>? = null
        private set

    fun setTranslations(translations: MutableMap<String?, String?>) {
        for ((key) in translations) {
            if (!EAttributes.keys.contains(key)) {
                BetterMinecraftCore.instance.logger.info(key)
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
        var instance: AttributeLangContainer? = null
            private set

        fun load() {
            instance = null
            BetterMinecraftCore.instance.logger.info("§3Loading Attributes lang...")
            val enchantLangContainerInstance = ConfigLoader.loadConfig("lang/attributes", AttributeLangContainer::class.java)
            BetterMinecraftCore.instance.logger.info("§bLoaded Attributes lang.")
            instance = enchantLangContainerInstance
        }
    }
}