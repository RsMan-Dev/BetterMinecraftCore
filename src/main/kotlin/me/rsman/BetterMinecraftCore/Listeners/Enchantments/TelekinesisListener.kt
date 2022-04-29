package me.rsman.BetterMinecraftCore.Listeners.Enchantments

import me.rsman.BetterMinecraftCore.Enchantments.Telekinesis
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.GameMode
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.entity.Arrow
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.block.Container
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class TelekinesisListener : Listener {
    @EventHandler
    fun onBreakBlock(event: BlockBreakEvent) {
        val inv = event.player.inventory
        val player = event.player
        val block = event.block
        if (inv.itemInMainHand.type == Material.AIR ||
                !inv.itemInMainHand.enchantments.containsKey(Telekinesis.enchant) || player.gameMode == GameMode.SPECTATOR || player.gameMode == GameMode.CREATIVE ||
                block.state is Container) return
        val drops = block.getDrops(inv.itemInMainHand)
        event.isDropItems = false
        if (drops.isEmpty()) return
        val itemsNotAdded: Map<Int, ItemStack> = inv.addItem(drops.iterator().next())
        if (itemsNotAdded.isEmpty()) return
        for ((_, value) in itemsNotAdded) {
            player.world.dropItemNaturally(block.location, value)
        }
    }

    @EventHandler
    fun onEntityKill(event: EntityDeathEvent) {
        val entity: Entity = event.entity
        val damageEvent: EntityDamageEvent = event.entity.lastDamageCause as? EntityDamageByEntityEvent ?: return
        val damager = (damageEvent as EntityDamageByEntityEvent).damager
        if (event.entity is Player) return
        val player: Player = when (damager) {
            is Arrow -> damager.shooter as? Player ?: return
            is Player -> damager
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
            player.world.dropItemNaturally(entity.location, value)
        }
    }
}