package fr.rsman.betterMinecraftCore.managers

import fr.rsman.betterMinecraftCore.listeners.*

object ListenersManager {
    fun registerAllEvents() {
        val listeners = arrayOf(
            DamageListener(),
            PlayerConnectListener(),
            EquipmentListener(),
            BlockListener(),
            CraftingListener(),
            EnchantListener(),
            AnvilListener(),  //enchantments
            ArrowShotListener(),
            CraftCommandListener(),
            GrindstoneListener(),
            EntityKillListener()
        ) + ItemsAdderManager.getListeners() +
            MythicMobsManager.getListeners()
        for (listener in listeners) {
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.server.pluginManager.registerEvents(listener, fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance)
        }
    }
}