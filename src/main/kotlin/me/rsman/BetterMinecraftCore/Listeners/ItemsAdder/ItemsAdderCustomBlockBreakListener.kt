package me.rsman.BetterMinecraftCore.Listeners.ItemsAdder

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent
import dev.lone.itemsadder.api.ItemsAdder
import me.rsman.BetterMinecraftCore.Enchantments.Telekinesis
import me.rsman.BetterMinecraftCore.Managers.ItemManager
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Container
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class ItemsAdderCustomBlockBreakListener: Listener {
    @EventHandler
    fun onCustomBlockBreak(event: CustomBlockBreakEvent) {
        val player = event.player
        val inv = player.inventory
        val block = event.block
        val addedDrops = mutableListOf<ItemStack>()
        ItemManager.blockLootTable["ia." + event.namespacedID.split(":")[1]]?.forEach {
            val stack = it.second.itemStack
            stack.amount = if (it.first.minimum == it.first.maximum)
                it.first.minimum
            else
                Random.nextInt(it.first.minimum, it.first.maximum)
            addedDrops.add(stack)
        }
        //if(addedDrops.isEmpty()) return
        if (
            inv.itemInMainHand.type == Material.AIR ||
            !inv.itemInMainHand.enchantments.containsKey(Telekinesis.enchant) ||
            player.gameMode == GameMode.SPECTATOR ||
            player.gameMode == GameMode.CREATIVE ||
            block.state is Container
        ) {
            addedDrops.forEach { block.world.dropItemNaturally(block.location, it) }
            return
        }
        val drops = addedDrops
        if (drops.isEmpty()) return
        val itemsNotAdded: Map<Int, ItemStack> = inv.addItem(drops.iterator().next())
        if (itemsNotAdded.isEmpty()) return
        for ((_, value) in itemsNotAdded) {
            player.world.dropItemNaturally(block.location, value)
        }
    }
}