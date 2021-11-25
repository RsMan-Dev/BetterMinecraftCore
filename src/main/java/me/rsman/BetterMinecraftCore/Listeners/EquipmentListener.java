package me.rsman.BetterMinecraftCore.Listeners;

import me.rsman.BetterMinecraftCore.Managers.ItemManager;
import me.rsman.BetterMinecraftCore.Managers.ItemTypeChecker;
import me.rsman.BetterMinecraftCore.Managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class EquipmentListener implements Listener {

    @EventHandler
    public void onChangeHeldItem(PlayerItemHeldEvent event){
        PlayerManager.alterPlayerAttributesWithEquippedStuff(event.getPlayer());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event)
    {
        if (event.getInventory().getType() == InventoryType.CRAFTING || event.getInventory().getType() == InventoryType.PLAYER)
        {
            if (event.getSlotType() == InventoryType.SlotType.ARMOR || event.getSlot() == 40 || event.isShiftClick())
            {
                Player player = (Player) event.getWhoClicked();
                PlayerManager.alterPlayerAttributesWithEquippedStuff(player);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            ItemStack item =event.getItem();
            if(item == null) return;
            if (ItemTypeChecker.isArmor(event.getItem()))
                PlayerManager.alterPlayerAttributesWithEquippedStuff(event.getPlayer());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        PlayerManager.alterPlayerAttributesWithEquippedStuff(event.getEntity());
    }
}
