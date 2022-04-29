package me.rsman.BetterMinecraftCore.models

import com.j256.ormlite.table.DatabaseTable
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.DataType
import java.util.HashMap
import java.lang.NoSuchFieldException
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import java.lang.IllegalAccessException

@DatabaseTable(tableName = "player_base_attrs")
class PlayerBaseAttr {
    @DatabaseField(dataType = DataType.STRING, id = true, width = 36)
    private var uuid: String? = null

    @DatabaseField
    var health: Long = 100
        private set

    @DatabaseField
    var damage: Long = 1
        private set

    @DatabaseField
    var defense: Long = 0
        private set

    @DatabaseField
    var strength: Long = 0
        private set

    @DatabaseField
    var speed: Long = 0
        private set

    @DatabaseField
    var critChance: Long = 0
        private set

    @DatabaseField
    var critDamage: Long = 0
        private set

    @DatabaseField
    var attackSpeed: Long = 0
        private set

    @DatabaseField
    var intelligence: Long = 0
        private set

    @DatabaseField
    var mana: Long = 100
        private set

    constructor()
    constructor(uuid: String?) {
        this.uuid = uuid
    }

    val map: HashMap<String?, Long?>
        get() = object : HashMap<String?, Long?>() {
            init {
                put("health", health)
                put("damage", damage)
                put("defense", defense)
                put("strength", strength)
                put("speed", speed)
                put("critChance", critChance)
                put("critDamage", critDamage)
                put("attackSpeed", attackSpeed)
                put("intelligence", intelligence)
                put("mana", mana)
            }
        }

    fun setByName(attrName: String, value: Long): PlayerBaseAttr {
        return try {
            val field = this.javaClass.getDeclaredField(attrName)
            field.setLong(this, value)
            this
        } catch (e: NoSuchFieldException) {
            BetterMinecraftCore.instance.logger.info("trying to set $attrName attribute, but does not exists")
            this
        } catch (e: IllegalAccessException) {
            BetterMinecraftCore.instance.logger.info("trying to set $attrName attribute, but does not exists")
            this
        }
    }

    fun setUuid(uuid: String?): PlayerBaseAttr {
        this.uuid = uuid
        return this
    }

    fun setHealth(health: Long): PlayerBaseAttr {
        this.health = health
        return this
    }

    fun setDamage(damage: Long): PlayerBaseAttr {
        this.damage = damage
        return this
    }

    fun setDefense(defense: Long): PlayerBaseAttr {
        this.defense = defense
        return this
    }

    fun setStrength(strength: Long): PlayerBaseAttr {
        this.strength = strength
        return this
    }

    fun setSpeed(speed: Long): PlayerBaseAttr {
        this.speed = speed
        return this
    }

    fun setCritChance(critChance: Long): PlayerBaseAttr {
        this.critChance = critChance
        return this
    }

    fun setCritDamage(critDamage: Long): PlayerBaseAttr {
        this.critDamage = critDamage
        return this
    }

    fun setAttackSpeed(attackSpeed: Long): PlayerBaseAttr {
        this.attackSpeed = attackSpeed
        return this
    }

    fun setIntelligence(intelligence: Long): PlayerBaseAttr {
        this.intelligence = intelligence
        return this
    }

    fun setMana(mana: Long): PlayerBaseAttr {
        this.mana = mana
        return this
    }
}