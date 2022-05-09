package me.rsman.BetterMinecraftCore.Listeners.ItemsAdder

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class CustomBlockBreak: Listener {
    @EventHandler
    fun onCustomBlockBreak(event: CustomBlockBreakEvent){
        event.block.location
    }
}