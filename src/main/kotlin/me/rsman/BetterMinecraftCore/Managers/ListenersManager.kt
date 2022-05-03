package me.rsman.BetterMinecraftCore.Managers

import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.Listeners.*
import me.rsman.BetterMinecraftCore.Listeners.Enchantments.AimingListener
import me.rsman.BetterMinecraftCore.Listeners.Enchantments.TelekinesisListener

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
                AimingListener(),
                TelekinesisListener(),  // Commands listener
                CraftCommandListener(),
                GrindstoneListener()
        )
        for (listener in listeners) {
            BetterMinecraftCore.instance.server.pluginManager.registerEvents(listener, BetterMinecraftCore.instance)
        }
    }
}