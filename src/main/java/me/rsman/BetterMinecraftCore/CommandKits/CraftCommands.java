package me.rsman.BetterMinecraftCore.CommandKits;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager;
import me.rsman.BetterMinecraftCore.Managers.Command.Lang.MessageKeys;

import me.rsman.BetterMinecraftCore.Managers.CraftManager;
import me.rsman.BetterMinecraftCore.configs.models.BmcCraftSubContainer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@CommandAlias("bmc|betterminecraftcore")
@Subcommand("craft|c")
public class CraftCommands extends BaseCommand {

    public static NamespacedKey NAMESPACE_KEY = NamespacedKey.minecraft("bmc_modify_craft");

    // all config wiped out, using config for database data is useless.
    private final CommandManager commandManager = CommandManager.get();

    @Subcommand("setShaped")
    @CommandCompletion("@shapedCrafts <result> <resultCount> [force] @nothing")
    @CommandPermission("hc.craft.set_shaped")
    @Description("{@@bmc.command.description.craft.set_shaped}")
    @Syntax("<name.key> <result> <resultCount> [force]")
    public void onSetShaped(Player playerSender, String nameKeyPair, @Optional String result, @Optional Integer resultCount, @Optional String force) {
        CraftManager.openCraftingEditorInventory(commandManager, playerSender, NAMESPACE_KEY, result, nameKeyPair, resultCount, "shaped", force);
    }

    @Subcommand("setShapeless")
    @CommandCompletion("@shapelessCrafts <result> <resultCount> [force] @nothing")
    @CommandPermission("hc.craft.set_shapeless")
    @Description("{@@bmc.command.description.craft.set_shapeless}")
    @Syntax("<name.key> <result> <resultCount> [force]")
    public void onSetShapeless(Player playerSender, String nameKeyPair, @Optional String result, @Optional Integer resultCount, @Optional String force) {
        CraftManager.openCraftingEditorInventory(commandManager, playerSender, NAMESPACE_KEY, result, nameKeyPair, resultCount, "shapeless", force);
    }
}