package me.rsman.BetterMinecraftCore.Managers.Command;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import me.rsman.BetterMinecraftCore.Managers.CraftManager;
import me.rsman.BetterMinecraftCore.Managers.EnchantManager;
import me.rsman.BetterMinecraftCore.Managers.ItemManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public final class CommandCompletionsManager {

    public static void init(){
        registerCommandCompletions();
    }

    private static void registerCommandCompletions() {

        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = CommandManager.get().getCommandCompletions();
        commandCompletions.registerAsyncCompletion("item", c -> {
            CommandSender sender = c.getSender();
            if (sender instanceof Player) {
                Set<String> itemList = ItemManager.registeredItems.keySet();
                return new ArrayList<>(itemList);
            }
            return null;
        });
        commandCompletions.registerAsyncCompletion("attribute", c -> {
            CommandSender sender = c.getSender();
            if (sender instanceof Player) {
                return new ArrayList<>(Arrays.asList(ItemManager.allowedAttrs));
            }
            return null;
        });
        commandCompletions.registerAsyncCompletion("enchantment", c -> {
            CommandSender sender = c.getSender();
            if (sender instanceof Player) {
                return new ArrayList<>(EnchantManager.enchants.keySet());
            }
            return null;
        });
        commandCompletions.registerAsyncCompletion("shapedCrafts", c -> {
            CommandSender sender = c.getSender();
            if (sender instanceof Player) {
                List<String> crafts = new ArrayList<>();
                CraftManager.registeredCrafts.forEach((craftId, subCrafts) -> {
                    if(subCrafts.containsKey("shaped"))
                    subCrafts.get("shaped").forEach(craftKey -> {
                        crafts.add(craftId + "." + craftKey);
                    });
                });
                return crafts;
            }
            return null;
        });
        commandCompletions.registerAsyncCompletion("shapelessCrafts", c -> {
            CommandSender sender = c.getSender();
            if (sender instanceof Player) {
                List<String> crafts = new ArrayList<>();
                CraftManager.registeredCrafts.forEach((craftId, subCrafts) -> {
                    if(subCrafts.containsKey("shapeless"))
                    subCrafts.get("shapeless").forEach(craftKey -> {
                        crafts.add(craftId + "." + craftKey);
                    });
                });
                return crafts;
            }
            return null;
        });
    }
}
