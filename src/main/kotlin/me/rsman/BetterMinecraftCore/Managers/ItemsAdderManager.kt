package me.rsman.BetterMinecraftCore.Managers

import dev.lone.itemsadder.api.ItemsAdder
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.Listeners.ItemsAdder.ItemsAdderCustomBlockBreakListener
import me.rsman.BetterMinecraftCore.Listeners.ItemsAdder.ItemsAdderLoadListener
import org.bukkit.event.Listener

object ItemsAdderManager {
    val isItemsAdderInstalled
        get() = BetterMinecraftCore.instance.server.pluginManager.getPlugin("ItemsAdder") != null


    fun getListeners() : List<Listener> = if(isItemsAdderInstalled) listOf(
        ItemsAdderLoadListener(),
        ItemsAdderCustomBlockBreakListener()
    ) else listOf()
}