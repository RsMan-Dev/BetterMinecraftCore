package me.rsman.BetterMinecraftCore.Managers

import java.util.HashMap
import me.rsman.BetterMinecraftCore.models.PlayerBaseAttr
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.enums.EAttributes
import org.bukkit.entity.Player
import java.sql.SQLException
import org.bukkit.inventory.ItemStack
import java.lang.Runnable
import org.bukkit.Material
import org.bukkit.attribute.Attribute

object PlayerManager {
    var playersAttributes: MutableMap<String?, MutableMap<String, Long>> = HashMap()
    fun getBaseAttributes(uuid: String?): Map<String, Long>? {
        return getBaseAttributes(uuid, false)
    }

    fun getBaseAttributes(uuid: String?, force: Boolean?): Map<String, Long>? {
        if (playersAttributes.containsKey(uuid) && !force!!) {
            return playersAttributes[uuid]
        }
        try {
            var pba = DBManager.playerBaseAttrDao!!.queryForId(uuid)
            if (pba == null) {
                DBManager.playerBaseAttrDao!!.create(PlayerBaseAttr(uuid))
                pba = DBManager.playerBaseAttrDao!!.queryForId(uuid)
            }
            val playerStats: MutableMap<String, Long> = HashMap()
            for ((key, value) in pba!!.map) {
                playerStats[key.toString() + "_base"] = value ?: 0L
                playerStats[key.toString() + "_equip"] = 0L
                playerStats[key.toString() + "_skill"] = 0L
                playerStats[key.toString() + "_talisman"] = 0L
            }
            playersAttributes[uuid] = playerStats
            return playerStats
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
        return null
    }

    fun setBaseAttribute(uuid: String?, attr: String, value: Long) {
        try {
            val pba = DBManager.playerBaseAttrDao!!.queryForId(uuid)
            pba!!.setByName(attr, value)
            DBManager.playerBaseAttrDao!!.update(pba)
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
        getBaseAttributes(uuid, true)
    }

    fun setEquippedAttributes(uuid: String?, attr: Map<String, Long>) {
        val attributes = playersAttributes[uuid]!!
        for ((key, value) in attr) {
            attributes[key + "_equip"] = value
        }
        playersAttributes[uuid] = attributes
    }

    fun setSkillAttributes(uuid: String?, attr: Map<String, Long>) {
        val attributes = playersAttributes[uuid]!!
        for ((key, value) in attr) {
            attributes[key + "_skill"] = value
        }
        playersAttributes[uuid] = attributes
    }

    fun setTalismansAttributes(uuid: String?, attr: Map<String, Long>) {
        val attributes = playersAttributes[uuid]!!
        for ((key, value) in attr) {
            attributes[key + "_talisman"] = value
        }
        playersAttributes[uuid] = attributes
    }

    fun getAttributes(uuid: String?): Map<String, Long> {
        val returnAttributes: MutableMap<String, Long> = HashMap()
        var attributes: Map<String, Long>? = playersAttributes[uuid]
        if (attributes == null) {
            getBaseAttributes(uuid, true)
        }
        attributes = playersAttributes[uuid]
        for (attr in EAttributes.allKeys) {
            returnAttributes[attr] = attributes!![attr + "_base"]!! + attributes[attr + "_equip"]!! + attributes[attr + "_skill"]!! + attributes[attr + "_talisman"]!!
        }
        return returnAttributes
    }

    fun alterPlayerAttributesWithEquippedStuff(player: Player) {
        BetterMinecraftCore.instance.server.scheduler.runTaskLaterAsynchronously(BetterMinecraftCore.instance, Runnable {
            val equipment = player.equipment!!
            var mainHand: ItemStack? = equipment.itemInMainHand
            if (ItemTypeChecker.isArmorOrHead(mainHand)) mainHand = null
            val offHand = equipment.itemInOffHand
            if (ItemTypeChecker.isArmorOrHead(offHand)) mainHand = null
            val items = arrayOf(
                    mainHand,
                    offHand,
                    equipment.helmet,
                    equipment.chestplate,
                    equipment.leggings,
                    equipment.boots)
            val finalAttributes: MutableMap<String, Long> = HashMap()
            for (attr in EAttributes.allKeys) {
                finalAttributes[attr] = 0L
            }
            for (item in items) {
                if (item == null || item.type == Material.AIR || item.type == Material.WRITABLE_BOOK) continue
                ItemManager.updateItem(item)
                for (attr in EAttributes.allKeys) {
                    val attrValue = ItemManager.getFinalItemAttr(item, attr)
                    finalAttributes[attr] = finalAttributes[attr]!! + attrValue
                }
            }
            setEquippedAttributes(player.uniqueId.toString(), finalAttributes)
        }, 1)
    }

    fun updatePlayerAttributes(player: Player) {
        val uuid = player.uniqueId.toString()
        val totalAttributes = getAttributes(uuid)
        var attributes = playersAttributes[uuid]!!
        var mana = attributes["currentMana"]
        if (mana == null) {
            attributes["currentMana"] = 0L
            playersAttributes[uuid] = attributes
            attributes = playersAttributes[uuid]!!
            mana = attributes["currentMana"]
        }
        mana = Math.round(Math.min(Math.round(mana!! + totalAttributes["mana"]!! * 0.02), totalAttributes["mana"]!!).toFloat()).toLong()
        attributes["currentMana"] = mana
        playersAttributes[uuid] = attributes
        val playerAttackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)
        if (playerAttackSpeed != null) {
            playerAttackSpeed.baseValue = 4 * (1 + (totalAttributes["attackSpeed"]?.toDouble() ?: 2.0) / 100)
        }
        val playerSpeed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
        if (playerSpeed != null) {
            playerSpeed.baseValue = 0.1 * (1 + (totalAttributes["speed"]?.toDouble() ?: 0.0) / 100)
        }
        val playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        playerMaxHealth?.baseValue = totalAttributes["health"]?.toDouble() ?: 20.0
        player.health = (totalAttributes["health"]?.toDouble() ?: 20.0).coerceAtMost(player.health + (totalAttributes["strength"]?.toDouble() ?: 0.0) / 200 + 1)
        player.healthScale = 20.0
        ActionBarManager.updateActionBar(player, totalAttributes, mana)
    }
}