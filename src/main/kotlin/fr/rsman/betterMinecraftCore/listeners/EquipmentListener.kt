package fr.rsman.betterMinecraftCore.listeners

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import fr.rsman.betterMinecraftCore.managers.PlayerManager
import fr.rsman.betterMinecraftCore.managers.ItemTypeChecker
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action

class EquipmentListener : Listener {
    @EventHandler
    fun onChangeHeldItem(event: PlayerItemHeldEvent) {
        PlayerManager.alterPlayerAttributesWithEquippedStuff(event.player)
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if (event.inventory.type == InventoryType.CRAFTING || event.inventory.type == InventoryType.PLAYER) {
            if (event.slotType == InventoryType.SlotType.ARMOR || event.slot == 40 || event.isShiftClick) {
                val player = event.whoClicked as Player
                PlayerManager.alterPlayerAttributesWithEquippedStuff(player)
            }
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            event.item ?: return
            if (ItemTypeChecker.isArmor(event.item)) PlayerManager.alterPlayerAttributesWithEquippedStuff(event.player)
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        PlayerManager.alterPlayerAttributesWithEquippedStuff(event.entity)
    }
}