package me.rsman.BetterMinecraftCore.Managers

import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.Listeners.DamageListener
import me.rsman.BetterMinecraftCore.Listeners.PlayerConnectListener
import me.rsman.BetterMinecraftCore.Listeners.EquipmentListener
import me.rsman.BetterMinecraftCore.Listeners.BlockListener
import me.rsman.BetterMinecraftCore.Listeners.CraftingListener
import me.rsman.BetterMinecraftCore.Listeners.EnchantListener
import me.rsman.BetterMinecraftCore.Listeners.AnvilListener
import me.rsman.BetterMinecraftCore.Listeners.Enchantments.AimingListener
import me.rsman.BetterMinecraftCore.Listeners.Enchantments.TelekinesisListener
import me.rsman.BetterMinecraftCore.Listeners.CraftCommandListener

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
                CraftCommandListener())
        for (listener in listeners) {
            BetterMinecraftCore.instance.server.pluginManager.registerEvents(listener, BetterMinecraftCore.instance)
        }
    }
}