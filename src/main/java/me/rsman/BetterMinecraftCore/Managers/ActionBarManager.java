package me.rsman.BetterMinecraftCore.Managers;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.Map;

public final class ActionBarManager {
    public static void updateActionBar(Player player, Map<String, Long> attributes, Long mana){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
            TextComponent.fromLegacyText(
                "§8[§c"+Math.round(player.getHealth())+"§7/§c"+attributes.get("health")+"§8]§c❤ §7| "+
                "§8[§a"+attributes.get("defense")+"§8]§a✤ §7| "+
                "§8[§9"+mana+"§7/§9"+attributes.get("mana")+"§8]§9✯"
            )
        );
    }
}
