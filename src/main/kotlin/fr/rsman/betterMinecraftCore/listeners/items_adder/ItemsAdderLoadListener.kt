@file:Suppress("EmptyMethod")

package fr.rsman.betterMinecraftCore.listeners.items_adder

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ItemsAdderLoadListener : Listener {
    @Suppress("EmptyMethod")
    @EventHandler
    fun onLoaded(event: ItemsAdderLoadDataEvent){
    }
}