package fr.rsman.betterMinecraftCore.listeners

import fr.rsman.betterMinecraftCore.enchantments.Telekinesis
import fr.rsman.betterMinecraftCore.configs.containers.BmcItemContainer
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class EntityKillListener: Listener {
    @EventHandler
    fun onEntityKill(event: EntityDeathEvent) {
        val addedDrops = mutableListOf<ItemStack>()
        BmcItemContainer.entityLootTable["m." + event.entityType.name]?.forEach {
            val stack = it.second.itemStack
            stack.amount = if (it.first.minimum == it.first.maximum)
                it.first.minimum
            else
                Random.nextInt(it.first.minimum, it.first.maximum)
            addedDrops.add(stack)
        }
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
        if (
            inv.itemInMainHand.type == Material.AIR ||
            !inv.itemInMainHand.enchantments.containsKey(Telekinesis.enchant) ||
            player.gameMode == GameMode.SPECTATOR ||
            player.gameMode == GameMode.CREATIVE
        ) {
            addedDrops.forEach { event.entity.world.dropItemNaturally(event.entity.location, it) }
            return
        }
        val drops: Collection<ItemStack> = event.drops + addedDrops
        player.giveExp(event.droppedExp)
        player.sendExperienceChange(player.exp, player.level)
        event.droppedExp = 0
        if (drops.isEmpty()) return
        val itemsNotAdded: Map<Int, ItemStack> = inv.addItem(drops.iterator().next())
        event.drops.clear()
        if (itemsNotAdded.isEmpty()) return
        for ((_, value) in itemsNotAdded) {
            player.world.dropItemNaturally(entity.location, value)
        }
    }
}