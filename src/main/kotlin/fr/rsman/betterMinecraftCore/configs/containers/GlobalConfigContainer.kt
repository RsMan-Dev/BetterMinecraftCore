package fr.rsman.betterMinecraftCore.configs.containers

import fr.rsman.betterMinecraftCore.configs.ConfigLoader

@Suppress("PropertyName")
data class GlobalConfigContainer(
    var isVerbose: Boolean = false,
    var db_host: String? = null,
    var db_port: String? = null,
    var db_database: String? = null,
    var db_user: String? = null,
    var db_password: String? = null,
    var attribute_display_format: String? = null,
    var attribute_modifier_display_format: String? = null,
    var enchant_display_format: String? = null,
    var enchant_separator_display_format: String? = null,
    var action_bar_display_format: String? = null
) {


    companion object {
        @JvmStatic
        var instance: GlobalConfigContainer? = null
            private set

        fun load() {
            instance = null
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.logger.info("§3Loading BMC config...")
            val globalConfigContainerInstance = ConfigLoader.loadConfig("global", GlobalConfigContainer::class.java)
            if (globalConfigContainerInstance == null) {
                fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.logger.severe("§4Config cannot be loaded")
            } else {
                fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.logger.info("§bLoaded global config.")
                fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.logger.info("§bverbose mode §6" + if (globalConfigContainerInstance.isVerbose) "on" else "off")
            }
            instance = globalConfigContainerInstance
        }
    }
}