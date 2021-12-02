package me.rsman.BetterMinecraftCore.CommandKits;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.j256.ormlite.stmt.query.In;
import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager;
import me.rsman.BetterMinecraftCore.Managers.Command.Lang.MessageKeys;
import me.rsman.BetterMinecraftCore.Managers.EnchantManager;
import me.rsman.BetterMinecraftCore.Managers.ItemManager;
import me.rsman.BetterMinecraftCore.Managers.PlayerManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@CommandAlias("bmc|betterminecraftcore")
@Subcommand("item|i")
public class ItemCommands extends BaseCommand {

    private final CommandManager commandManager = CommandManager.get();

    @Subcommand("setAttribute")
    @CommandCompletion("@attribute <value> @nothing")
    @CommandPermission("hc.item.attribute.set")
    @Description("{@@bmc.command.description.item.attribute.set}")
    @Syntax("<attribute> <value>")
    public void onAddAttr(Player playerSender, @Values("@attribute") String attr, Integer value){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemStack item = Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand();
        if(item.getType() == Material.AIR || item.getItemMeta() == null){
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM);return;
        }
        ItemManager.setItemAttr(item, attr, value);
        PlayerManager.alterPlayerAttributesWithEquippedStuff(playerSender);
        issuerSender.sendInfo(MessageKeys.ITEM_ATTRIBUTE_SET, "{attr}", attr, "{val}", value+"");
    }
    @Subcommand("setEnchantment")
    @CommandCompletion("@enchantment <level> @nothing")
    @CommandPermission("hc.item.enchantment.set")
    @Description("{@@bmc.command.description.item.enchantment.set}")
    @Syntax("<enchantment> <level>")
    public void onAddEnch(Player playerSender, @Values("@enchantment") String ench, Integer level){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemStack item = Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand();
        if(item.getType() == Material.AIR || item.getItemMeta() == null){
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM);return;
        }
        EnchantManager.addEnchantment(item, EnchantManager.enchants.get(ench), level);
        PlayerManager.alterPlayerAttributesWithEquippedStuff(playerSender);
        issuerSender.sendInfo(MessageKeys.ITEM_ENCHANTMENT_SET, "{ench}", ench, "{level}", level+"");
    }
    @Subcommand("removeAttribute")
    @CommandCompletion("@attribute @nothing")
    @CommandPermission("hc.item.attribute.remove")
    @Description("{@@bmc.command.description.item.attribute.remove}")
    @Syntax("<attribute>")
    public void onRemoveAttr(Player playerSender, @Values("@attribute") String attr){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemStack item = Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand();
        if(item.getType() == Material.AIR || item.getItemMeta() == null){
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM);return;
        }
        ItemManager.setItemAttr(item, attr, 0);
        PlayerManager.alterPlayerAttributesWithEquippedStuff(playerSender);
        issuerSender.sendInfo(MessageKeys.ITEM_ATTRIBUTE_REMOVE, "{attr}", attr);
    }
    @Subcommand("removeEnchantment")
    @CommandCompletion("@enchantment @nothing")
    @CommandPermission("hc.item.enchantment.remove")
    @Description("{@@bmc.command.description.item.enchantment.remove}")
    @Syntax("<enchantment>")
    public void onRemoveEnch(Player playerSender, @Values("@enchantment") String ench){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemStack item = Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand();
        if(item.getType() == Material.AIR || item.getItemMeta() == null){
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM);return;
        }
        EnchantManager.removeEnchantment(item, EnchantManager.enchants.get(ench));
        PlayerManager.alterPlayerAttributesWithEquippedStuff(playerSender);
        issuerSender.sendInfo(MessageKeys.ITEM_ENCHANTMENT_REMOVE, "{ench}", ench);
    }
    @Subcommand("setDisplayName")
    @CommandCompletion("<name/null> @nothing")
    @CommandPermission("hc.item.name.set")
    @Description("{@@bmc.command.description.item.name.set}")
    @Syntax("<name/null>")
    public void onSetName(Player playerSender, String name){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemStack item = Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand();
        if(item.getType() == Material.AIR || item.getItemMeta() == null){
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM);return;
        }
        ItemMeta meta = item.getItemMeta();
        if(name.equals("none")){
            meta.setDisplayName("");
        } else {
            meta.setDisplayName(name.replace("&", "§"));
        }
        item.setItemMeta(meta);
        issuerSender.sendInfo(MessageKeys.ITEM_NAME_SET, "{name}", name.replace("&", "§"));
    }
    @Subcommand("setMaterialData")
    @CommandCompletion("<id> @nothing")
    @CommandPermission("hc.item.material_data.set")
    @Description("{@@bmc.command.description.item.material_data.set}")
    @Syntax("<id>")
    public void onSetMaterialData(Player playerSender, Integer id){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemStack item = Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand();
        if(item.getType() == Material.AIR || item.getItemMeta() == null){
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM);return;
        }
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(id);
        item.setItemMeta(meta);
        issuerSender.sendInfo(MessageKeys.ITEM_MATERIAL_DATA_SET, "{id}", id+"");
    }
    @Subcommand("setRev")
    @CommandCompletion("<rev> @nothing")
    @CommandPermission("hc.item.rev.set")
    @Description("{@@bmc.command.description.item.rev.set}")
    @Syntax("<rev>")
    public void onSetRev(Player playerSender, Integer rev){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemStack item = Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand();
        if(item.getType() == Material.AIR || item.getItemMeta() == null){
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM);return;
        }
        if(rev < 1) rev = 1;
        ItemManager.setItemRev(item,rev);
        issuerSender.sendInfo(MessageKeys.ITEM_REV_SET, "{id}", rev+"");
    }
    @Subcommand("get")
    @CommandCompletion("@item [amount] @nothing")
    @CommandPermission("hc.item.give.self")
    @Description("{@@bmc.command.description.item.give.self}")
    @Syntax("<item_key> [amount]")
    public void onGet(Player playerSender, @Values("@item") String itemName, @Optional Integer number){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        if(!ItemManager.registeredItems.containsKey(itemName)){
            issuerSender.sendInfo(MessageKeys.INCORRECT_NAME, "{val}", itemName);
            return;
        }
        ItemStack item = ItemManager.registeredItems.get(itemName).clone();
        if(number == null){ number = 1; }
        int initNum = number;
        do{
            item.setAmount(Math.min(item.getMaxStackSize(), number));
            Map<Integer,ItemStack> notAdded = playerSender.getInventory().addItem(item.clone());
            if(!notAdded.isEmpty()) {
                issuerSender.sendError(MessageKeys.INVENTORY_FULL);
                break;
            }
            number -= Math.min(item.getMaxStackSize(), number);
        } while(number>0);
        PlayerManager.alterPlayerAttributesWithEquippedStuff(playerSender);
        issuerSender.sendInfo(MessageKeys.ITEM_GIVEN, "{item}", itemName, "{number}", initNum+"", "{player}", playerSender.getDisplayName());
    }
    @Subcommand("give")
    @CommandCompletion("@item @players [amount] @nothing")
    @CommandPermission("hc.item.give.other")
    @Description("{@@bmc.command.description.item.give.other}")
    @Syntax("<item_key> <player> [amount]")
    public void onGive(@Optional Player playerSender, @Values("@item") String itemName, OnlinePlayer player, @Optional Integer number){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        if(!ItemManager.registeredItems.containsKey(itemName)){
            issuerSender.sendInfo(MessageKeys.INCORRECT_NAME, "{val}", itemName);
            return;
        }
        ItemStack item = ItemManager.registeredItems.get(itemName).clone();
        if(number == null){ number = 1; }
        int initNum = number;
        do{
            item.setAmount(Math.min(item.getMaxStackSize(), number));
            Map<Integer,ItemStack> notAdded = player.getPlayer().getInventory().addItem(item.clone());
            if(!notAdded.isEmpty()) {
                issuerSender.sendError(MessageKeys.INVENTORY_FULL);
                break;
            }
            number -= Math.min(item.getMaxStackSize(), number);
        } while(number>0);
        PlayerManager.alterPlayerAttributesWithEquippedStuff(player.getPlayer());
        issuerSender.sendInfo(MessageKeys.ITEM_GIVEN, "{item}", itemName, "{number}", initNum+"", "{player}", player.getPlayer().getDisplayName());
    }
    @Subcommand("save")
    @CommandCompletion("<item_key> @nothing")
    @CommandPermission("hc.item.save")
    @Description("{@@bmc.command.description.item.save}")
    @Syntax("[item_key]")
    public void onSave(Player playerSender, @Optional String itemName){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemStack item = Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand();
        if(item.getType() == Material.AIR || item.getItemMeta() == null){
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM);return;
        }
        if(itemName == null){
            if(ItemManager.getItemName(item).equals("")){
                issuerSender.sendError(MessageKeys.ITEM_NEVER_SAVED);
                return;
            }else{
                itemName = ItemManager.getItemName(item);
            }
        }
        if(itemName.matches("^[a-zA-Z]+(_[a-zA-Z]+)*$")){
            ItemManager.setItemName(item, itemName);
            ItemManager.getItemRev(item);
            ItemManager.setItemInConfig(item, itemName);
            ItemManager.registeredItems.clear();
            ItemManager.registerAllItemsWithConfig();
            issuerSender.sendInfo(MessageKeys.ITEM_SAVED, "{item}", itemName);
        } else {
            issuerSender.sendError(MessageKeys.INCORRECT_NAME, "{val}", itemName);
        }
    }
    @Subcommand("delete")
    @CommandCompletion("@item @nothing")
    @CommandPermission("hc.item.delete")
    @Description("{@@bmc.command.description.item.delete}")
    @Syntax("<item_key>")
    public void onDelete(Player playerSender, @Values("@item") String itemName){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemManager.deleteItemFromConfig(itemName);
        ItemManager.registeredItems.clear();
        ItemManager.registerAllItemsWithConfig();
        issuerSender.sendInfo(MessageKeys.ITEM_DELETED, "{item}", itemName);
    }
    @Subcommand("setLore")
    @CommandCompletion("<line> <text/null> @nothing")
    @CommandPermission("hc.item.set_lore")
    @Description("{@@bmc.command.description.item.set_lore}")
    @Syntax("<line> <text/null>")
    public void onLoreSet(Player playerSender, Integer line, String text){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemStack item = Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand();
        if(item.getType() == Material.AIR || item.getItemMeta() == null){
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM);return;
        }
        ItemManager.setCustomLore(item, text, line);
        ItemManager.updateItemLore(item);
        issuerSender.sendInfo(MessageKeys.ITEM_LORE_SET, "{text}", text, "{line}", line+"");
    }
    @Subcommand("addLore")
    @CommandCompletion("<text/null> @nothing")
    @CommandPermission("hc.item.add_lore")
    @Description("{@@bmc.command.description.item.add_lore}")
    @Syntax("<text/null>")
    public void onLoreAdd(Player playerSender, String text){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemStack item = Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand();
        if(item.getType() == Material.AIR || item.getItemMeta() == null){
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM);return;
        }
        ItemManager.setCustomLore(item, text, null);
        ItemManager.updateItemLore(item);
        issuerSender.sendInfo(MessageKeys.ITEM_LORE_SET, "{text}", text, "{line}", "n");
    }
    @Subcommand("setUnbreakable")
    @CommandCompletion("<true/false> @nothing")
    @CommandPermission("hc.item.set_unbreakable")
    @Description("{@@bmc.command.description.item.set_unbreakable}")
    @Syntax("<true/false>")
    public void onSetUnbreakable(Player playerSender, Boolean state){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemStack item = Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand();
        if(item.getType() == Material.AIR || item.getItemMeta() == null){
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM);return;
        }
        ItemManager.setUnbreakable(item,state);
        ItemManager.updateItemLore(item);
        issuerSender.sendInfo(MessageKeys.ITEM_UNBREAKABLE_SET, "{state}", state+"");
    }
    @Subcommand("setRenamable")
    @CommandCompletion("<true/false> @nothing")
    @CommandPermission("hc.item.set_renamable")
    @Description("{@@bmc.command.description.item.set_renamable}")
    @Syntax("<true/false>")
    public void onSetRenamable(Player playerSender, Boolean state){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        ItemStack item = Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand();
        if(item.getType() == Material.AIR || item.getItemMeta() == null){
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM);return;
        }
        ItemManager.setRenamable(item,state);
        issuerSender.sendInfo(MessageKeys.ITEM_RENAMABLE_SET, "{state}", state+"");
    }
    @Subcommand("log")
    @CommandCompletion("@nothing")
    @CommandPermission("hc.item.log")
    @Description("{@@bmc.command.description.item.log}")
    public void onLog(Player playerSender){
        BetterMinecraftCore.getInstance().getLogger().info(Objects.requireNonNull(playerSender.getEquipment()).getItemInMainHand()+"");
    }
}