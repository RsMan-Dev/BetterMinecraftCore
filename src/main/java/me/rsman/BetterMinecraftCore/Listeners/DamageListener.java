package me.rsman.BetterMinecraftCore.Listeners;

import me.rsman.BetterMinecraftCore.Managers.DamageManager;
import me.rsman.BetterMinecraftCore.Managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Map;

public class DamageListener implements Listener {
    @EventHandler
    public void onEntityDamaged(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        try {
            if (damager instanceof Player) {
                event.setDamage(DamageManager.getDamageFromAll((Player)damager));
                damager.sendMessage("infligé: " + event.getDamage());

            } else if(damager instanceof Arrow) {
                Arrow arrow = (Arrow) damager;
                ProjectileSource source = arrow.getShooter();
                if(source instanceof Player){
                    event.setDamage(DamageManager.getDamageFromAll((Player)source));
                    ((Player)source).sendMessage("infligé: " + event.getDamage());
                }
            } else if(entity instanceof Player) {
                Map<String, Long> attributes = PlayerManager.getAttributes(event.getEntity().getUniqueId().toString());

                double reduc = ((double)attributes.get("defense") / ((double)attributes.get("defense") + 100));
                event.setDamage(event.getDamage() * (1-reduc));
                event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 0);
                event.getEntity().sendMessage("recu: " + event.getDamage() + "  | reduction: " + Math.round(reduc*100));

            }
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning(e.toString());
        }
    }

}
