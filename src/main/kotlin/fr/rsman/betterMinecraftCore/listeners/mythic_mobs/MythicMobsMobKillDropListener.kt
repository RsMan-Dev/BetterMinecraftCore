package fr.rsman.betterMinecraftCore.listeners.mythic_mobs

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent
import fr.rsman.betterMinecraftCore.enchantments.Telekinesis
import fr.rsman.betterMinecraftCore.configs.containers.BmcItemContainer
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class MythicMobsMobKillDropListener: Listener {
    @EventHandler
    fun onEntityKill(event: MythicMobDeathEvent){
        BmcItemContainer.entityLootTable["mm." + event.mobType.internalName]?.forEach {
            val stack = it.second.itemStack
            stack.amount = if (it.first.minimum == it.first.maximum)
                it.first.minimum
            else
                Random.nextInt(it.first.minimum, it.first.maximum)
            event.drops = event.drops + stack
        }
        val killer = event.killer
        if (event.entity is Player) return
        val player: Player = when (killer) {
            is Arrow -> killer.shooter as? Player ?: return
            is Player -> killer
            else -> return
        }
        val inv = player.inventory
        if (inv.itemInMainHand.type == Material.AIR ||
                !inv.itemInMainHand.enchantments.containsKey(Telekinesis.enchant) || player.gameMode == GameMode.SPECTATOR || player.gameMode == GameMode.CREATIVE) return
        val drops: Collection<ItemStack> = event.drops
        if (drops.isEmpty()) return
        val itemsNotAdded: Map<Int, ItemStack> = inv.addItem(drops.iterator().next())
        event.drops.clear()
        if (itemsNotAdded.isEmpty()) return
        for ((_, value) in itemsNotAdded) {
            player.world.dropItemNaturally(event.entity.location, value)
        }
    }
}