package me.rsman.BetterMinecraftCore.CommandKits;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager;
import me.rsman.BetterMinecraftCore.Managers.Command.Lang.MessageKeys;

import com.j256.ormlite.logger.Log;

import me.rsman.BetterMinecraftCore.Managers.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
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
    public void onSetShaped(Player playerSender, String nameKeyPair, String result, Integer resultCount, @Optional String force) {
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        issuerSender.sendInfo(MessageKeys.COMING_SOON_CONF_POSSIBLE);
        String[] nameKeyPairDettached = nameKeyPair.split("\\.");
        if (nameKeyPairDettached.length != 2) { issuerSender.sendInfo(MessageKeys.INVALID_NAME_KEY_PAIR);return;}

        Recipe r = Bukkit.getRecipe(NamespacedKey.minecraft("bmc_shaped_" + nameKeyPairDettached[0].toLowerCase() + "_" + nameKeyPairDettached[1]));
        ItemStack querriedItem = ItemManager.convertItemSchemeToItemStack(result);
        if(querriedItem == null) return;
        boolean needToForce = false;
        if(r != null){
            if(querriedItem.equals(r.getResult())){
                if(querriedItem.hasItemMeta() && r.getResult().hasItemMeta()){
                    ItemMeta querriedItemMeta = Objects.requireNonNull(querriedItem.getItemMeta()).clone();
                    ItemMeta resultMeta = Objects.requireNonNull(r.getResult().getItemMeta()).clone();
                    if(!Objects.equals(querriedItemMeta, resultMeta)) needToForce = true;
                }
            } else {
                needToForce = true;
            }
        }
        if(needToForce){
            if(force == null || !force.equals("force")){
                playerSender.sendMessage("need to force");
                return;
            }
        }

        Inventory cInv = Bukkit.createInventory(playerSender, InventoryType.DISPENSER, "Craft " + nameKeyPair);
        if(r != null){
            Map<Character, ItemStack> itemMap = ((ShapedRecipe)r).getIngredientMap();

            int i = 0;
            for (ItemStack item: itemMap.values()) {
                if(item != null) cInv.setItem(i, item.clone());
                i++;
            }
        }
        playerSender.openInventory(cInv);

        playerSender.getPersistentDataContainer().set(NAMESPACE_KEY, PersistentDataType.STRING, nameKeyPair + "|" + result + "|" + resultCount + "|shaped");
    }

    @Subcommand("setShapeless")
    @CommandCompletion("@shapelessCrafts <result> <resultCount> [force] @nothing")
    @CommandPermission("hc.craft.set_shapeless")
    @Description("{@@bmc.command.description.craft.set_shapeless}")
    @Syntax("<name.key> <result> <resultCount> [force]")
    public void onSetShapeless(Player playerSender, String nameKeyPair, String result, Integer resultCount, @Optional String force) {
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        String[] nameKeyPairDettached = nameKeyPair.split("\\.");
        if (nameKeyPairDettached.length != 2) { issuerSender.sendInfo(MessageKeys.INVALID_NAME_KEY_PAIR);return;}

        Recipe r = Bukkit.getRecipe(NamespacedKey.minecraft("bmc_shapeless_" + nameKeyPairDettached[0].toLowerCase() + "_" + nameKeyPairDettached[1]));
        ItemStack querriedItem = ItemManager.convertItemSchemeToItemStack(result);
        if(querriedItem == null) return;
        boolean needToForce = false;
        if(r != null){
            if(querriedItem.equals(r.getResult())){
                if(querriedItem.hasItemMeta() && r.getResult().hasItemMeta()){
                    ItemMeta querriedItemMeta = Objects.requireNonNull(querriedItem.getItemMeta()).clone();
                    ItemMeta resultMeta = Objects.requireNonNull(r.getResult().getItemMeta()).clone();
                    if(!Objects.equals(querriedItemMeta, resultMeta)) needToForce = true;
                }
            } else {
                needToForce = true;
            }
        }
        if(needToForce){
            if(force == null || !force.equals("force")){
                playerSender.sendMessage("need to force");
                return;
            }
        }

        Inventory cInv = Bukkit.createInventory(playerSender, InventoryType.DISPENSER, "Craft " + nameKeyPair);
        if(r != null){
            List<ItemStack> itemList = ((ShapelessRecipe)r).getIngredientList();
            int i = 0;
            for (ItemStack item: itemList) {
                if(item != null) cInv.setItem(i, item.clone());
                i++;
            }
        }
        playerSender.openInventory(cInv);

        playerSender.getPersistentDataContainer().set(NAMESPACE_KEY, PersistentDataType.STRING, nameKeyPair + "|" + result + "|" + resultCount + "|shapeless");

    }
}