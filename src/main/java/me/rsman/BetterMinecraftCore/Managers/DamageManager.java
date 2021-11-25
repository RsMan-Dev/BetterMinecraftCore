package me.rsman.BetterMinecraftCore.Managers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import org.bukkit.entity.Player;

import java.util.Map;

public final class DamageManager {
    public static double getDamageFromAll(Player player){
        Map<String, Long> attributes = PlayerManager.getAttributes(player.getUniqueId().toString());
        double damage = Math.max((double)attributes.get("damage"),1);

        //apply strength
        damage *= (1 + (double)attributes.get("strength") / 100);

        //apply crit chance / damage
        if(Math.random()*100 < (double) attributes.get("critChance")){
            damage *= (1 + (double)attributes.get("critDamage") / 100);
        }

        return damage;
    }
}
