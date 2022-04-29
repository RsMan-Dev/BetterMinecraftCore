package me.rsman.BetterMinecraftCore.Listeners

import org.bukkit.entity.Player
import org.bukkit.Bukkit
import me.rsman.BetterMinecraftCore.Managers.PlayerManager
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.entity.Arrow
import me.rsman.BetterMinecraftCore.Managers.DamageManager
import org.bukkit.event.entity.EntityDamageEvent
import java.lang.NullPointerException
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.math.roundToInt

class DamageListener : Listener {
    @EventHandler
    fun onEntityDamaged(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        val entity = event.entity
        try {
            if (damager is Player) {
                event.damage = DamageManager.getDamageFromAll(damager)
                damager.sendMessage("infligé: " + event.damage)
            } else if (damager is Arrow) {
                val source = damager.shooter
                if (source is Player) {
                    event.damage = DamageManager.getDamageFromAll(source)
                    source.sendMessage("infligé: " + event.damage)
                }
            } else if (entity is Player) {
                val attributes = PlayerManager.getAttributes(event.entity.uniqueId.toString())
                val reduc = (attributes["defense"]?.toDouble() ?: 0.0) / ((attributes["defense"]?.toDouble() ?: 0.0) + 100)
                event.damage = event.damage * (1 - reduc)
                event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 0.0)
                event.entity.sendMessage("recu: " + event.damage + "  | reduction: " + (reduc * 100).roundToInt())
            }
        } catch (e: NullPointerException) {
            Bukkit.getLogger().warning(e.toString())
        }
    }
}