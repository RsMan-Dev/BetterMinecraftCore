package me.rsman.BetterMinecraftCore.enums

import java.util.ArrayList

enum class EAttributes(val key: String, val isPercent: Boolean = false) {
    DAMAGE("damage"),
    STRENGTH("strength"),
    INTELLIGENCE("intelligence"),
    CRIT_CHANCE("critChance", true),
    CRIT_DAMAGE("critDamage", true),
    DEFENSE("defense"),
    HEALTH("health"),
    ATTACK_SPEED("attackSpeed", true),
    MANA("mana"),
    SPEED("speed", true);

    companion object {
        val keys: List<String>
            get() = values().toList().map { it.key }
        val percentKeys: List<String>
            get() = values().toList().filter { it.isPercent } .map { it.key }

        fun fromKey(key: String) : EAttributes? = EAttributes.values().find { it.key == key }
    }
}