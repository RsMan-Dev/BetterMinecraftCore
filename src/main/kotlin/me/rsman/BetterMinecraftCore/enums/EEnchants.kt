package me.rsman.BetterMinecraftCore.enums

import java.util.HashMap
import org.bukkit.enchantments.Enchantment
import me.rsman.BetterMinecraftCore.Enchantments.Protection
import me.rsman.BetterMinecraftCore.Enchantments.Telekinesis
import me.rsman.BetterMinecraftCore.Enchantments.Aiming
import org.bukkit.NamespacedKey
import java.util.Arrays
import java.util.ArrayList

enum class EEnchants {
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
    PROTECTION("bmc_protection", Protection.enchant, arrayOf(BLAST_PROTECTION, OLD_PROTECTION, PROJECTILE_PROTECTION, FIRE_PROTECTION)),
    TELEKINESIS("bmc_telekinesis", Telekinesis.enchant),
    AIMING("bmc_aiming", Aiming.enchant);

    val key: String
    val enchant: Enchantment
    val replaces: Array<EEnchants>

    constructor(key: String, enchant: Enchantment, replaces: Array<EEnchants>) {
        this.key = key
        this.enchant = enchant
        this.replaces = replaces
    }

    constructor(key: String, enchant: Enchantment) {
        this.key = key
        this.enchant = enchant
        replaces = arrayOf()
    }

    val minecraftKey: NamespacedKey
        get() = NamespacedKey.minecraft(key)

    companion object {
        val enumKeys: List<String>
            get() {
                val keys: MutableList<String> = ArrayList()
                for (ee in values()) {
                    keys.add(ee.name)
                }
                return keys
            }
        val nonReplacedEnumKeys: List<String>
            get() {
                val keys: MutableList<String> = ArrayList()
                val replaced: MutableList<EEnchants> = ArrayList()
                for (ee in values()) {
                    keys.add(ee.name)
                    replaced.addAll(Arrays.asList(*ee.replaces))
                }
                for (ee in replaced) {
                    keys.remove(ee.name)
                }
                return keys
            }
        private var replacesMap: HashMap<String, Enchantment?>? = null
        val replacesMapFromNamespaces: HashMap<String, Enchantment>
            get() {
                val map = HashMap<String, Enchantment>()
                for (ee in values()) {
                    for (ee2 in ee.replaces) {
                        map[NamespacedKey.minecraft(ee2.key).toString()] = ee.enchant
                    }
                }
                return map
            }

        fun getEnumKeyFromKey(key: String): String? {
            for (ee in values()) {
                if (ee.key == key) return ee.name
            }
            return null
        }
    }
}