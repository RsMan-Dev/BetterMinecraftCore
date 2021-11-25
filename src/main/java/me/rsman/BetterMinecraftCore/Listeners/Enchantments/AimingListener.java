package me.rsman.BetterMinecraftCore.Listeners.Enchantments;

import me.rsman.BetterMinecraftCore.Enchantments.Aiming;
import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Managers.EnchantManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AimingListener implements Listener {

    @EventHandler
    public void onShoot(EntityShootBowEvent event){
        Entity arrow = event.getProjectile();
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ItemStack bow = player.getInventory().getItemInMainHand();

        if(EnchantManager.hasEnchantment(bow, Aiming.getEnchant())){
            Integer level = EnchantManager.getEnchantmentLevel(bow, Aiming.getEnchant());
            new BukkitRunnable(){
                public void run(){
                    if(arrow.isOnGround() || arrow.isDead())cancel();
                    List<Entity> nearest = arrow.getNearbyEntities(level*2,level*2,level*2);
                    for (Entity target : nearest){
                        if(player.hasLineOfSight(target) && target instanceof LivingEntity && !target.isDead() && target != player){
                            arrow.setVelocity(target.getLocation().add(0,target.getHeight()/2,0).toVector().subtract(arrow.getLocation().toVector()).normalize().multiply(2));
                        }
                    }
                }
            }.runTaskTimer(BetterMinecraftCore.getInstance(),0,1);
        }
    }
}
