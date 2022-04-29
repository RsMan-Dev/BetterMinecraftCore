package me.rsman.BetterMinecraftCore.Listeners.Enchantments

import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.Enchantments.Aiming
import org.bukkit.entity.Player
import me.rsman.BetterMinecraftCore.Managers.EnchantManager
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class AimingListener : Listener {
    @EventHandler
    fun onShoot(event: EntityShootBowEvent) {
        val arrow = event.projectile
        if (event.entity !is Player) return
        val player = event.entity as Player
        val bow = player.inventory.itemInMainHand
        if (EnchantManager.hasEnchantment(bow, Aiming.enchant)) {
            val level = EnchantManager.getEnchantmentLevel(bow, Aiming.enchant)
            object : BukkitRunnable() {
                override fun run() {
                    if (arrow.isOnGround || arrow.isDead) cancel()
                    val nearest = arrow.getNearbyEntities((level!! * 2).toDouble(), (level * 2).toDouble(), (level * 2).toDouble())
                    for (target in nearest) {
                        if (player.hasLineOfSight(target) && target is LivingEntity && !target.isDead() && target !== player) {
                            arrow.velocity = target.getLocation().add(0.0, target.getHeight() / 2, 0.0).toVector().subtract(arrow.location.toVector()).normalize().multiply(2)
                        }
                    }
                }
            }.runTaskTimer(BetterMinecraftCore.instance, 0, 1)
        }
    }
}