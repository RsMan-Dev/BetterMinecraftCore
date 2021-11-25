package me.rsman.BetterMinecraftCore.CommandKits;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager;
import me.rsman.BetterMinecraftCore.Managers.Command.Lang.MessageKeys;

import com.j256.ormlite.logger.Log;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Recipe;
import org.bukkit.persistence.PersistentDataType;

@CommandAlias("bmc|betterminecraftcore")
@Subcommand("craft|c")
public class CraftCommands extends BaseCommand {

    public static NamespacedKey NAMESPACE_KEY = NamespacedKey.minecraft("bmc_modify_craft");

    // all config wiped out, using config for database data is useless.
    private final CommandManager commandManager = CommandManager.get();

    @Subcommand("setShaped")
    @CommandCompletion("@shapedCrafts <result> @nothing")
    @CommandPermission("hc.craft.set_shaped")
    @Description("{@@bmc.command.description.craft.set_shaped}")
    @Syntax("<name.key> <result>")
    public void onSetShaped(Player playerSender, String nameKeyPair, String result) {
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        issuerSender.sendInfo(MessageKeys.COMING_SOON_CONF_POSSIBLE);
        String[] nameKeyPairDettached = nameKeyPair.split("\\.");
        if (nameKeyPairDettached.length != 2) {
            issuerSender.sendInfo(MessageKeys.INVALID_NAME_KEY_PAIR);
            return;
        }
        Recipe r = Bukkit.getRecipe(NamespacedKey
                .minecraft("bmc_shaped_" + nameKeyPairDettached[0].toLowerCase() + "_" + nameKeyPairDettached[1]));
        Inventory cInv = Bukkit.createInventory(playerSender, InventoryType.DISPENSER, "Craft " + nameKeyPair);

        playerSender.openInventory(cInv);

        String value = new StringBuilder().append(nameKeyPair).append("|").append(result).toString();

        playerSender.getPersistentDataContainer().set(NAMESPACE_KEY,
                PersistentDataType.STRING, value);

        BetterMinecraftCore.getInstance().getLogger().info(r == null ? "null" : r.toString());
    }

    @Subcommand("setShapeless")
    @CommandCompletion("@shapelessCrafts @nothing")
    @CommandPermission("hc.craft.set_shapeless")
    @Description("{@@bmc.command.description.craft.set_shapeless}")
    @Syntax("<name.key>")
    public void onSetShapeless(Player playerSender, String name, String key) {
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        issuerSender.sendInfo(MessageKeys.COMING_SOON_CONF_POSSIBLE);
    }
}