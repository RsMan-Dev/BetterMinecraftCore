package fr.rsman.betterMinecraftCore.listeners

import fr.rsman.betterMinecraftCore.configs.containers.BmcCraftContainer.Companion.load
import fr.rsman.betterMinecraftCore.configs.containers.BmcCraftContainer.Companion.registerCrafts
import fr.rsman.betterMinecraftCore.commandKits.CraftCommands
import org.bukkit.persistence.PersistentDataType
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.InventoryCloseEvent
import fr.rsman.betterMinecraftCore.configs.models.BmcShapedCraft
import fr.rsman.betterMinecraftCore.configs.models.BmcShapelessCraft
import fr.rsman.betterMinecraftCore.extensions.saveName
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

class CraftCommandListener : Listener {
    @EventHandler
    fun onCraftInventoryClose(event: InventoryCloseEvent) {
        val container = event.player.persistentDataContainer
        if (!container.has(CraftCommands.NAMESPACE_KEY, PersistentDataType.STRING)) return
        val data = container.get(CraftCommands.NAMESPACE_KEY, PersistentDataType.STRING)
        event.player.persistentDataContainer.remove(CraftCommands.NAMESPACE_KEY)
        val splittedData = data?.split("\\|".toRegex())?.toTypedArray() ?: throw Exception("error occured")
        if (splittedData.size != 4) return
        val nameKeyPairArr = splittedData[0].split("\\.".toRegex()).toTypedArray()
        val name = nameKeyPairArr[0]
        val key = nameKeyPairArr[1]
        val result = splittedData[1]
        val resultCount = splittedData[2].toInt()
        val isShaped = splittedData[3] == "shaped"
        val inv = event.inventory
        if (inv.type != InventoryType.DISPENSER) return
        var i = 0
        val schemes = Array(3) { arrayOfNulls<String>(3) }
        for (item in inv.storageContents) {
            val itemName: String = if (item == null) {
                "m.AIR"
            } else {
                if (item.saveName == null) {
                    "m." + item.type
                } else {
                    item.saveName!!
                }
            }
            schemes[i / 3][i % 3] = itemName + if (item != null) " " + item.amount else ""
            i++
        }
        if (isShaped) {
            val schemesFormatted = arrayOf("", "", "")
            i = 0
            while (i < 3) {
                schemesFormatted[i] = schemes[i][0].toString() + " | " + schemes[i][1] + " | " + schemes[i][2]
                i++
            }
            val bmcCraft = BmcShapedCraft(listOf(*schemesFormatted), "$result $resultCount", name, key)
            bmcCraft.registerSelfInConfig()
        } else {
            val schemesFormatted: MutableList<String> = ArrayList()
            i = 0
            while (i < 9) {
                if (schemes[i / 3][i % 3] != null && schemes[i / 3][i % 3] != "m.AIR")
                    schemesFormatted.add(schemes[i / 3][i % 3]!!)
                i++
            }
            val bmcCraft = BmcShapelessCraft()
            bmcCraft.ingredients = schemesFormatted
            bmcCraft.result = "$result $resultCount"
            bmcCraft.name = name
            bmcCraft.key = key
            bmcCraft.registerSelfInConfig()
        }
        load()
        registerCrafts()
    }
}