package me.rsman.BetterMinecraftCore.Managers

import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.Listeners.*

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
            BetterMinecraftCore.instance.server.pluginManager.registerEvents(listener, BetterMinecraftCore.instance)
        }
    }
}