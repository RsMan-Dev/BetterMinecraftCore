package me.rsman.BetterMinecraftCore.Listeners

import me.rsman.BetterMinecraftCore.Managers.ItemManager
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class AnvilListener : Listener {
    @EventHandler
    fun onAnvilFill(e: PrepareAnvilEvent) {
        val firstItem = e.inventory.getItem(0)
        val secondItem = e.inventory.getItem(1)
        val result = e.inventory.getItem(3)
        if (ItemManager.getItemName(firstItem) != "") {
            e.result = null
        }
    }
}