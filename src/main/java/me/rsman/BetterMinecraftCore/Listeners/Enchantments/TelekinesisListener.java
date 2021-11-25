package me.rsman.BetterMinecraftCore.Listeners.Enchantments;

import me.rsman.BetterMinecraftCore.Enchantments.Telekinesis;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collection;
import java.util.Map;

public class TelekinesisListener implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event){
        PlayerInventory inv = event.getPlayer().getInventory();
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(
                inv.getItemInMainHand().getType() == Material.AIR ||
                        !inv.getItemInMainHand().getEnchantments().containsKey(Telekinesis.getEnchant()) ||
                        player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE ||
                        block.getState() instanceof Container
        ) return;

        Collection<ItemStack> drops = block.getDrops(inv.getItemInMainHand());
        event.setDropItems(false);
        if(drops.isEmpty()) return;
        Map<Integer, ItemStack> itemsNotAdded = inv.addItem(drops.iterator().next());
        if(itemsNotAdded.isEmpty()) return;
        for (  Map.Entry<Integer, ItemStack> itemNotAdded: itemsNotAdded.entrySet() ){
            player.getWorld().dropItemNaturally(block.getLocation(), itemNotAdded.getValue());
        }
    }


    @EventHandler
    public void onEntityKill(EntityDeathEvent event){
        Entity entity = event.getEntity();
        Event damageEvent =  event.getEntity().getLastDamageCause();
        if(!(damageEvent instanceof EntityDamageByEntityEvent)) return;
        Entity damager = ((EntityDamageByEntityEvent)damageEvent).getDamager();
        if(event.getEntity() instanceof Player) return;
        Player player;
        if(damager instanceof Arrow){
            Arrow arrow = (Arrow) damager;
            ProjectileSource source = arrow.getShooter();
            if(!(source instanceof Player))return;
            player = (Player)source;
        } else if(damager instanceof Player){
            player = (Player)damager;
        } else {
            return;
        }
        PlayerInventory inv = player.getInventory();
        if(
            inv.getItemInMainHand().getType() == Material.AIR ||
            !inv.getItemInMainHand().getEnchantments().containsKey(Telekinesis.getEnchant()) ||
            player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE
        ) return;

        Collection<ItemStack> drops = event.getDrops();
        if(drops.isEmpty()) return;
        Map<Integer, ItemStack> itemsNotAdded = inv.addItem(drops.iterator().next());
        event.getDrops().clear();
        if(itemsNotAdded.isEmpty()) return;
        for (Map.Entry<Integer, ItemStack> itemNotAdded: itemsNotAdded.entrySet() ){
            player.getWorld().dropItemNaturally(entity.getLocation(), itemNotAdded.getValue());
        }
    }
}
