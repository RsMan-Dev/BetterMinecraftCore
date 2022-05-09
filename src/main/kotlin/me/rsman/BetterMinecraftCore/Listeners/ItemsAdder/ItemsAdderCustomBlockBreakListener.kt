package me.rsman.BetterMinecraftCore.Listeners.ItemsAdder

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent
import me.rsman.BetterMinecraftCore.Managers.ItemManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.random.Random

class ItemsAdderCustomBlockBreakListener: Listener {
    @EventHandler
    fun onCustomBlockBreak(event: CustomBlockBreakEvent) {
        val player = event.player
        ItemManager.blockLootTable["ia." + event.namespacedID.split(":")[1]]?.forEach {
            val stack = it.second.itemStack
            stack.amount = if (it.first.minimum == it.first.maximum)
                it.first.minimum
            else
                Random.nextInt(it.first.minimum, it.first.maximum)
            player.world.dropItemNaturally(event.block.location, stack)
        }
    }
}