package me.rsman.BetterMinecraftCore.enums

import java.util.ArrayList

enum class EAttributes {
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

    val key: String
    val isPercent: Boolean

    constructor(key: String, percent: Boolean) {
        this.key = key
        isPercent = percent
    }

    constructor(key: String) {
        this.key = key
        isPercent = false
    }

    companion object {
        val allKeys: List<String>
            get() {
                val keys: MutableList<String> = ArrayList()
                for (ea in values()) {
                    keys.add(ea.key)
                }
                return keys
            }
        val allPercentKeys: List<String>
            get() {
                val keys: MutableList<String> = ArrayList()
                for (ea in values()) {
                    if (ea.isPercent) keys.add(ea.key)
                }
                return keys
            }
    }
}