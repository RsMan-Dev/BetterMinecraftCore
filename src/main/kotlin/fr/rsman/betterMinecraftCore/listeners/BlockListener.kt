package fr.rsman.betterMinecraftCore.listeners

import fr.rsman.betterMinecraftCore.enchantments.Telekinesis
import fr.rsman.betterMinecraftCore.configs.containers.BmcItemContainer
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Container
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class BlockListener : Listener{
    @EventHandler
    fun onBreakBlock(event: BlockBreakEvent){
        val player = event.player
        val inv = player.inventory
        val block = event.block
        val addedDrops = mutableListOf<ItemStack>()
        BmcItemContainer.blockLootTable["m." + event.block.type.name]?.forEach {
            val stack = it.second.itemStack
            stack.amount = if (it.first.minimum == it.first.maximum)
                it.first.minimum
            else
                Random.nextInt(it.first.minimum, it.first.maximum)
            addedDrops.add(stack)
        }
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
        val drops = block.getDrops(inv.itemInMainHand) + addedDrops
        player.giveExp(event.expToDrop)
        player.sendExperienceChange(player.exp, player.level)
        event.expToDrop = 0
        event.isDropItems = false
        if (drops.isEmpty()) return
        val itemsNotAdded: Map<Int, ItemStack> = inv.addItem(drops.iterator().next())
        if (itemsNotAdded.isEmpty()) return
        for ((_, value) in itemsNotAdded) {
            player.world.dropItemNaturally(block.location, value)
        }
    }
}