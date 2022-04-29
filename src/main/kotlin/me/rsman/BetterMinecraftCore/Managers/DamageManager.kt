package me.rsman.BetterMinecraftCore.Managers

import org.bukkit.entity.Player

object DamageManager {
    fun getDamageFromAll(player: Player): Double {
        val attributes = PlayerManager.getAttributes(player.uniqueId.toString())
        var damage = (attributes["damage"]?.toDouble() ?: 1.0).coerceAtLeast(1.0)

        //apply strength
        damage *= 1 + (attributes["strength"]?.toDouble() ?: 0.0) / 100

        //apply crit chance / damage
        if (Math.random() * 100 < (attributes["critChance"]?.toDouble() ?: 0.0)) {
            damage *= 1 + (attributes["critDamage"]?.toDouble() ?: 0.0) / 100
        }
        return damage
    }
}