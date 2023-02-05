package fr.rsman.betterMinecraftCore.listeners

import org.bukkit.entity.Player
import org.bukkit.Bukkit
import fr.rsman.betterMinecraftCore.managers.PlayerManager
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.entity.Arrow
import fr.rsman.betterMinecraftCore.managers.DamageManager
import fr.rsman.betterMinecraftCore.configs.containers.GlobalConfigContainer
import org.bukkit.event.entity.EntityDamageEvent
import java.lang.NullPointerException
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.math.roundToInt

class DamageListener : Listener {
    @EventHandler
    fun onEntityDamagedByEntity(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        val entity = event.entity
        try {
            if (damager is Player) {
                event.damage = DamageManager.getDamageFromAll(damager)
                if(GlobalConfigContainer.instance?.isVerbose == true)
                    damager.sendMessage("infligé: " + event.damage)
            } else if (damager is Arrow) {
                val source = damager.shooter
                if (source is Player) {
                    event.damage = DamageManager.getDamageFromAll(source)
                    if(GlobalConfigContainer.instance?.isVerbose == true)
                        source.sendMessage("infligé: " + event.damage)
                }
            }
        } catch (e: NullPointerException) {
            Bukkit.getLogger().warning(e.toString())
        }
    }
    @EventHandler
    fun onPlayerDamaged(event: EntityDamageEvent) {
        val entity = event.entity
        try {
            if(entity is Player){
                val attributes = PlayerManager.getAttributes(event.entity.uniqueId.toString())
                val reduc = (attributes["defense"]?.toDouble() ?: 0.0) / ((attributes["defense"]?.toDouble() ?: 0.0) + 100)
                event.damage = event.damage * (1 - reduc)
                event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 0.0)
                if(GlobalConfigContainer.instance?.isVerbose == true)
                    event.entity.sendMessage("recu: " + event.damage + "  | reduction: " + (reduc * 100).roundToInt())
            }
        } catch (e: NullPointerException) {
            Bukkit.getLogger().warning(e.toString())
        }
    }
}