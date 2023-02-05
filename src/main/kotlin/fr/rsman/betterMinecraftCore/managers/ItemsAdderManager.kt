package fr.rsman.betterMinecraftCore.managers

import fr.rsman.betterMinecraftCore.listeners.items_adder.ItemsAdderCustomBlockBreakListener
import fr.rsman.betterMinecraftCore.listeners.items_adder.ItemsAdderLoadListener
import org.bukkit.event.Listener

object ItemsAdderManager {

    @Suppress("BooleanMethodIsAlwaysInverted")
    val isItemsAdderInstalled
        get() = fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.server.pluginManager.getPlugin("ItemsAdder") != null


    fun getListeners() : List<Listener> = if(isItemsAdderInstalled) listOf(
        ItemsAdderLoadListener(),
        ItemsAdderCustomBlockBreakListener()
    ) else listOf()
}