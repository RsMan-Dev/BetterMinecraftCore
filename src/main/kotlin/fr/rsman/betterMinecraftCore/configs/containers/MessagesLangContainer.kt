package fr.rsman.betterMinecraftCore.configs.containers

import fr.rsman.betterMinecraftCore.configs.ConfigLoader
import fr.rsman.betterMinecraftCore.enums.EMessages

class MessagesLangContainer {
    var translations: MutableMap<String?, String?>? = null
        private set

    fun setTranslations(translations: MutableMap<String?, String?>) {
        for ((key) in translations) {
            if (!EMessages.enumKeys.contains(key)) {
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
        var instance: MessagesLangContainer? = null
            private set

        fun load() {
            instance = null
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.logger.info("§3Loading Messages lang...")
            val messageLangContainerInstance = ConfigLoader.loadConfig("lang/messages", MessagesLangContainer::class.java)
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.logger.info("§bLoaded Messages lang.")
            instance = messageLangContainerInstance
        }
    }
}