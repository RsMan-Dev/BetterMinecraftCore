package me.rsman.BetterMinecraftCore.Managers.Command;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import me.rsman.BetterMinecraftCore.Managers.CraftManager;
import me.rsman.BetterMinecraftCore.Managers.EnchantManager;
import me.rsman.BetterMinecraftCore.Managers.ItemManager;
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer;
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer;
import me.rsman.BetterMinecraftCore.enums.EAttributes;
import me.rsman.BetterMinecraftCore.enums.EEnchants;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class CommandCompletionsManager {

    public static void init(){
        registerCommandCompletions();
    }

    private static void registerCommandCompletions() {

        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = CommandManager.get().getCommandCompletions();
        commandCompletions.registerAsyncCompletion("item", c -> {
            CommandSender sender = c.getSender();
            if (sender instanceof Player) {
                Set<String> itemList = BmcItemContainer.getInstance().getItems().keySet();
                return new ArrayList<>(itemList);
            }
            return null;
        });
        commandCompletions.registerAsyncCompletion("attribute", c -> {
            CommandSender sender = c.getSender();
            if (sender instanceof Player) {
                return EAttributes.getAllKeys();
            }
            return null;
        });
        commandCompletions.registerAsyncCompletion("enchantment", c -> {
            CommandSender sender = c.getSender();
            if (sender instanceof Player) {
                return EEnchants.getNonReplacedEnumKeys();
            }
            return null;
        });
        commandCompletions.registerAsyncCompletion("shapedCrafts", c -> {
            CommandSender sender = c.getSender();
            if (sender instanceof Player) {
                return BmcCraftContainer.getShapedKeys();
            }
            return null;
        });
        commandCompletions.registerAsyncCompletion("shapelessCrafts", c -> {
            CommandSender sender = c.getSender();
            if (sender instanceof Player) {
                return BmcCraftContainer.getShapelessKeys();
            }
            return null;
        });
    }
}
