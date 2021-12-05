package me.rsman.BetterMinecraftCore.Managers;

import me.rsman.BetterMinecraftCore.configs.containers.GlobalConfigContainer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Map;

public final class ActionBarManager {
    private static String actionBarDisplayFormat;

    private static String getActionBarDisplayFormat(){
        if(actionBarDisplayFormat != null) return actionBarDisplayFormat;
        actionBarDisplayFormat = GlobalConfigContainer.getInstance().getAction_bar_display_format();
        return actionBarDisplayFormat;
    }

    public static void updateActionBar(Player player, Map<String, Long> attributes, Long mana){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
            TextComponent.fromLegacyText(
                getActionBarDisplayFormat().replace("&", "§")
                    .replace("{health}", Math.round(player.getHealth()) + "")
                    .replace("{health_max}", attributes.get("health") + "")
                    .replace("{defense}", attributes.get("defense") + "")
                    .replace("{mana}", mana + "")
                    .replace("{mana_max}", attributes.get("mana") + "")
            )
        );
    }
}
