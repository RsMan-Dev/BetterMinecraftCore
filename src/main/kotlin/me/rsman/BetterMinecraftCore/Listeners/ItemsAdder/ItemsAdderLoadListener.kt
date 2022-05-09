package me.rsman.BetterMinecraftCore.Listeners.ItemsAdder

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ItemsAdderLoadListener : Listener {
    @EventHandler
    fun onLoaded(event: ItemsAdderLoadDataEvent){
    }
}