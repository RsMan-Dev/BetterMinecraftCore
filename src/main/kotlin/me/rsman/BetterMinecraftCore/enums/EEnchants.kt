package me.rsman.BetterMinecraftCore.enums

import java.util.HashMap
import org.bukkit.enchantments.Enchantment
import me.rsman.BetterMinecraftCore.Enchantments.Protection
import me.rsman.BetterMinecraftCore.Enchantments.Telekinesis
import me.rsman.BetterMinecraftCore.Enchantments.Aiming
import org.bukkit.NamespacedKey
import java.util.Arrays
import java.util.ArrayList

enum class EEnchants(val key: String, val enchant: Enchantment, val replaces: List<EEnchants>? = null) {
    AQUA_INFINITY("aqua_affinity", Enchantment.WATER_WORKER),
    BANE_OF_ARTHROPODS("bane_of_arthropods", Enchantment.DAMAGE_ARTHROPODS),
    BLAST_PROTECTION("blast_protection", Enchantment.PROTECTION_EXPLOSIONS),
    CHANNELING("channeling", Enchantment.CHANNELING),
    BINDING_CURSE("binding_curse", Enchantment.BINDING_CURSE),
    VANISHING_CURSE("vanishing_curse", Enchantment.VANISHING_CURSE),
    DEPTH_STRIDER("depth_strider", Enchantment.DEPTH_STRIDER),
    EFFICIENCY("efficiency", Enchantment.DIG_SPEED),
    FEATHER_FALLING("efficiency", Enchantment.DIG_SPEED),
    FIRE_ASPECT("fire_aspect", Enchantment.FIRE_ASPECT),
    FIRE_PROTECTION("fire_protection", Enchantment.PROTECTION_FIRE),
    FLAME("flame", Enchantment.ARROW_FIRE),
    FORTUNE("fortune", Enchantment.LOOT_BONUS_BLOCKS),
    FROST_WALKER("frost_walker", Enchantment.FROST_WALKER),
    IMPALING("impaling", Enchantment.IMPALING),
    INFINITY("infinity", Enchantment.ARROW_INFINITE),
    KNOCKBACK("knockback", Enchantment.KNOCKBACK),
    LOYALTY("loyalty", Enchantment.LOYALTY),
    LUCK_OF_THE_SEA("luck_of_the_sea", Enchantment.LUCK),
    LURE("lure", Enchantment.LURE),
    MENDING("mending", Enchantment.MENDING),
    MULTISHOT("multishot", Enchantment.MULTISHOT),
    PIERCING("piercing", Enchantment.PIERCING),
    POWER("power", Enchantment.ARROW_DAMAGE),
    PROJECTILE_PROTECTION("projectile_protection", Enchantment.PROTECTION_PROJECTILE),
    OLD_PROTECTION("protection", Enchantment.PROTECTION_ENVIRONMENTAL),
    PUNCH("punch", Enchantment.ARROW_KNOCKBACK),
    QUICK_CHARGE("quick_charge", Enchantment.QUICK_CHARGE),
    RESPIRATION("respiration", Enchantment.OXYGEN),
    RIPTIDE("riptide", Enchantment.RIPTIDE),
    SHARPNESS("sharpness", Enchantment.DAMAGE_ALL),
    SILK_TOUCH("silk_touch", Enchantment.SILK_TOUCH),
    SMITE("smite", Enchantment.DAMAGE_UNDEAD),
    SOUL_SPEED("soul_speed", Enchantment.SOUL_SPEED),
    SWEEPING("sweeping", Enchantment.SWEEPING_EDGE),
    UNBREAKING("unbreaking", Enchantment.DURABILITY),
    THORNS("thorns", Enchantment.THORNS),
    LOOTING("looting", Enchantment.LOOT_BONUS_MOBS),
    PROTECTION("bmc_protection", Protection.enchant, listOf(BLAST_PROTECTION, OLD_PROTECTION, PROJECTILE_PROTECTION, FIRE_PROTECTION)),
    TELEKINESIS("bmc_telekinesis", Telekinesis.enchant),
    AIMING("bmc_aiming", Aiming.enchant);

    val minecraftKey: NamespacedKey
        get() = NamespacedKey.minecraft(key)

    companion object {
        val keys: List<String>
            get() = values().toList().map { it.key }

        val nonReplacedKeys: List<String>
           get() = keys.toMutableList().apply { removeAll(values().mapNotNull { it.replaces }.flatten().map { it.key }) }

        private var replacesMap: HashMap<String, Enchantment?>? = null
        val replacesMapFromNamespaces: HashMap<String, Enchantment>
            get() = HashMap<String, Enchantment>().apply {
                values().forEach { it.replaces?.forEach { it2 -> this[NamespacedKey.minecraft(it2.key).toString()] = it.enchant } }
            }

        fun fromKey(key: String): EEnchants? = values().find { it.key == key }
    }
}