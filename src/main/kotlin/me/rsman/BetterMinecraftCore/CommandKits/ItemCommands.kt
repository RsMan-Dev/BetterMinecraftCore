package me.rsman.BetterMinecraftCore.CommandKits

import me.rsman.BetterMinecraftCore.BetterMinecraftCore.Companion.instance
import co.aikar.commands.BaseCommand
import org.bukkit.entity.Player
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.*
import co.aikar.commands.annotation.Optional
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import me.rsman.BetterMinecraftCore.Managers.ItemManager
import me.rsman.BetterMinecraftCore.Managers.PlayerManager
import me.rsman.BetterMinecraftCore.Managers.EnchantManager
import me.rsman.BetterMinecraftCore.enums.EEnchants
import org.bukkit.inventory.meta.ItemMeta
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer
import me.rsman.BetterMinecraftCore.configs.models.BmcItem
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager
import me.rsman.BetterMinecraftCore.Managers.Command.Lang.MessageKeys
import java.util.*

@CommandAlias("bmc|betterminecraftcore")
@Subcommand("item|i")
class ItemCommands : BaseCommand() {
    private val commandManager = CommandManager.get()
    @Subcommand("setAttribute")
    @CommandCompletion("@attribute <value> @nothing")
    @CommandPermission("bmc.item.attribute.set")
    @Description("{@@bmc.command.description.item.attribute.set}")
    @Syntax("<attribute> <value>")
    fun onAddAttr(playerSender: Player, @Values("@attribute") attr: String?, value: Int) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        ItemManager.setItemAttr(item, attr ?: return , value.toLong())
        PlayerManager.alterPlayerAttributesWithEquippedStuff(playerSender)
        issuerSender.sendInfo(MessageKeys.ITEM_ATTRIBUTE_SET, "{attr}", attr, "{val}", value.toString() + "")
    }

    @Subcommand("setEnchantment")
    @CommandCompletion("@enchantment <level> @nothing")
    @CommandPermission("bmc.item.enchantment.set")
    @Description("{@@bmc.command.description.item.enchantment.set}")
    @Syntax("<enchantment> <level>")
    fun onAddEnch(playerSender: Player, @Values("@enchantment") ench: String?, level: Int) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        EnchantManager.addEnchantment(item, EEnchants.valueOf(ench!!).enchant, level)
        PlayerManager.alterPlayerAttributesWithEquippedStuff(playerSender)
        issuerSender.sendInfo(MessageKeys.ITEM_ENCHANTMENT_SET, "{ench}", ench, "{level}", level.toString() + "")
    }

    @Subcommand("removeAttribute")
    @CommandCompletion("@attribute @nothing")
    @CommandPermission("bmc.item.attribute.remove")
    @Description("{@@bmc.command.description.item.attribute.remove}")
    @Syntax("<attribute>")
    fun onRemoveAttr(playerSender: Player, @Values("@attribute") attr: String?) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        ItemManager.setItemAttr(item, attr ?: return , 0)
        PlayerManager.alterPlayerAttributesWithEquippedStuff(playerSender)
        issuerSender.sendInfo(MessageKeys.ITEM_ATTRIBUTE_REMOVE, "{attr}", attr)
    }

    @Subcommand("removeEnchantment")
    @CommandCompletion("@enchantment @nothing")
    @CommandPermission("bmc.item.enchantment.remove")
    @Description("{@@bmc.command.description.item.enchantment.remove}")
    @Syntax("<enchantment>")
    fun onRemoveEnch(playerSender: Player, @Values("@enchantment") ench: String?) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        EnchantManager.removeEnchantment(item, EEnchants.valueOf(ench!!).enchant)
        PlayerManager.alterPlayerAttributesWithEquippedStuff(playerSender)
        issuerSender.sendInfo(MessageKeys.ITEM_ENCHANTMENT_REMOVE, "{ench}", ench)
    }

    @Subcommand("setDisplayName")
    @CommandCompletion("<name/null> @nothing")
    @CommandPermission("bmc.item.name.set")
    @Description("{@@bmc.command.description.item.name.set}")
    @Syntax("<name/null>")
    fun onSetName(playerSender: Player, name: String) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        val meta = item.itemMeta
        if (name == "none") {
            meta!!.setDisplayName("")
        } else {
            meta!!.setDisplayName(name.replace("&", "§"))
        }
        item.itemMeta = meta
        issuerSender.sendInfo(MessageKeys.ITEM_NAME_SET, "{name}", name.replace("&", "§"))
    }

    @Subcommand("setMaterialData")
    @CommandCompletion("<id> @nothing")
    @CommandPermission("bmc.item.material_data.set")
    @Description("{@@bmc.command.description.item.material_data.set}")
    @Syntax("<id>")
    fun onSetMaterialData(playerSender: Player, id: Int) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        val meta = item.itemMeta
        meta!!.setCustomModelData(id)
        item.itemMeta = meta
        issuerSender.sendInfo(MessageKeys.ITEM_MATERIAL_DATA_SET, "{id}", id.toString() + "")
    }

    @Subcommand("setRev")
    @CommandCompletion("<rev> @nothing")
    @CommandPermission("bmc.item.rev.set")
    @Description("{@@bmc.command.description.item.rev.set}")
    @Syntax("<rev>")
    fun onSetRev(playerSender: Player, rev: Int) {
        var rev = rev
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        if (rev < 1) rev = 1
        ItemManager.setItemRev(item, rev)
        issuerSender.sendInfo(MessageKeys.ITEM_REV_SET, "{id}", rev.toString() + "")
    }

    @Subcommand("get")
    @CommandCompletion("@item [amount] @nothing")
    @CommandPermission("bmc.item.give.self")
    @Description("{@@bmc.command.description.item.give.self}")
    @Syntax("<item_key> [amount]")
    fun onGet(playerSender: Player, @Values("@item") itemName: String?, @Optional number: Int?) {
        var number = number ?: 1
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        if (BmcItemContainer.instance?.items?.containsKey(itemName) != true) {
            issuerSender.sendInfo(MessageKeys.INCORRECT_NAME, "{val}", itemName)
            return
        }
        val item = BmcItemContainer.instance?.items?.get(itemName)!!.itemStack
        val initNum = number
        do {
            item.amount = Math.min(item.maxStackSize, number)
            val notAdded: Map<Int, ItemStack> = playerSender.inventory.addItem(item.clone())
            if (!notAdded.isEmpty()) {
                issuerSender.sendError(MessageKeys.INVENTORY_FULL)
                break
            }
            number -= Math.min(item.maxStackSize, number)
        } while (number > 0)
        PlayerManager.alterPlayerAttributesWithEquippedStuff(playerSender)
        issuerSender.sendInfo(MessageKeys.ITEM_GIVEN, "{item}", itemName, "{number}", initNum.toString() + "", "{player}", playerSender.displayName)
    }

    @Subcommand("give")
    @CommandCompletion("@item @players [amount] @nothing")
    @CommandPermission("bmc.item.give.other")
    @Description("{@@bmc.command.description.item.give.other}")
    @Syntax("<item_key> <player> [amount]")
    fun onGive(@Optional playerSender: Player?, @Values("@item") itemName: String?, player: OnlinePlayer, @Optional number: Int?) {
        var number = number ?: 1
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        if (BmcItemContainer.instance?.items?.containsKey(itemName) != true) {
            issuerSender.sendInfo(MessageKeys.INCORRECT_NAME, "{val}", itemName)
            return
        }
        val item = BmcItemContainer.instance?.items?.get(itemName)!!.itemStack
        val initNum = number
        do {
            item.amount = Math.min(item.maxStackSize, number)
            val notAdded: Map<Int, ItemStack> = player.getPlayer().inventory.addItem(item.clone())
            if (!notAdded.isEmpty()) {
                issuerSender.sendError(MessageKeys.INVENTORY_FULL)
                break
            }
            number -= Math.min(item.maxStackSize, number)
        } while (number > 0)
        PlayerManager.alterPlayerAttributesWithEquippedStuff(player.getPlayer())
        issuerSender.sendInfo(MessageKeys.ITEM_GIVEN, "{item}", itemName, "{number}", initNum.toString() + "", "{player}", player.getPlayer().displayName)
    }

    @Subcommand("save")
    @CommandCompletion("<item_key> @nothing")
    @CommandPermission("bmc.item.save")
    @Description("{@@bmc.command.description.item.save}")
    @Syntax("[item_key]")
    fun onSave(playerSender: Player, @Optional itemName: String?) {
        var itemName = itemName
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        if (itemName == null) {
            itemName = if (ItemManager.getItemName(item) == "") {
                issuerSender.sendError(MessageKeys.ITEM_NEVER_SAVED)
                return
            } else {
                ItemManager.getItemName(item)
            }
        }
        if (itemName!!.matches("^[a-zA-Z]+(_[a-zA-Z]+)*$".toRegex())) {
            ItemManager.setItemName(item, itemName)
            ItemManager.getItemRev(item)
            BmcItemContainer.instance?.items?.set(itemName, BmcItem.parseItemStack(item))
            BmcItemContainer.save()
            issuerSender.sendInfo(MessageKeys.ITEM_SAVED, "{item}", itemName)
        } else {
            issuerSender.sendError(MessageKeys.INCORRECT_NAME, "{val}", itemName)
        }
    }

    @Subcommand("delete")
    @CommandCompletion("@item @nothing")
    @CommandPermission("bmc.item.delete")
    @Description("{@@bmc.command.description.item.delete}")
    @Syntax("<item_key>")
    fun onDelete(playerSender: Player?, @Values("@item") itemName: String?) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        BmcItemContainer.instance?.items?.remove(itemName)
        BmcItemContainer.save()
        issuerSender.sendInfo(MessageKeys.ITEM_DELETED, "{item}", itemName)
    }

    @Subcommand("setLore")
    @CommandCompletion("<line> <text/null> @nothing")
    @CommandPermission("bmc.item.set_lore")
    @Description("{@@bmc.command.description.item.set_lore}")
    @Syntax("<line> <text/null>")
    fun onLoreSet(playerSender: Player, line: Int, text: String?) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        ItemManager.setCustomLore(item, text ?: return , line)
        ItemManager.updateItemLore(item)
        issuerSender.sendInfo(MessageKeys.ITEM_LORE_SET, "{text}", text, "{line}", line.toString() + "")
    }

    @Subcommand("addLore")
    @CommandCompletion("<text/null> @nothing")
    @CommandPermission("bmc.item.add_lore")
    @Description("{@@bmc.command.description.item.add_lore}")
    @Syntax("<text/null>")
    fun onLoreAdd(playerSender: Player, text: String?) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        ItemManager.setCustomLore(item, text ?: return , null)
        ItemManager.updateItemLore(item)
        issuerSender.sendInfo(MessageKeys.ITEM_LORE_SET, "{text}", text, "{line}", "n")
    }

    @Subcommand("setUnbreakable")
    @CommandCompletion("<true/false> @nothing")
    @CommandPermission("bmc.item.set_unbreakable")
    @Description("{@@bmc.command.description.item.set_unbreakable}")
    @Syntax("<true/false>")
    fun onSetUnbreakable(playerSender: Player, state: Boolean) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        ItemManager.setUnbreakable(item, state)
        ItemManager.updateItemLore(item)
        issuerSender.sendInfo(MessageKeys.ITEM_UNBREAKABLE_SET, "{state}", state.toString() + "")
    }

    @Subcommand("setRenamable")
    @CommandCompletion("<true/false> @nothing")
    @CommandPermission("bmc.item.set_renamable")
    @Description("{@@bmc.command.description.item.set_renamable}")
    @Syntax("<true/false>")
    fun onSetRenamable(playerSender: Player, state: Boolean) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        ItemManager.setRenamable(item, state)
        issuerSender.sendInfo(MessageKeys.ITEM_RENAMABLE_SET, "{state}", state.toString() + "")
    }

    @Subcommand("log")
    @CommandCompletion("@nothing")
    @CommandPermission("bmc.item.log")
    @Description("{@@bmc.command.description.item.log}")
    fun onLog(playerSender: Player) {
        instance.logger.info(Objects.requireNonNull(playerSender.equipment)!!.itemInMainHand.toString() + "")
    }

    @Subcommand("addDropBlockSource")
    @CommandCompletion("@blockDropSource [min_count] [max_count] [chance(between_0_and_1)] @nothing")
    @CommandPermission("bmc.item.drop_source")
    @Description("{@@bmc.command.description.item.drop_source.add_block}")
    @Syntax("<block_pattern> [min_count] [max_count] [chance(between_0_and_1)]")
    fun onAddDropBlockSource(playerSender: Player, @Values("@blockDropSource") blockPattern: String, @Optional minCount: Int?, @Optional maxCount: Int?, @Optional chance: Double) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        ItemManager.addDrop(item, "$blockPattern $minCount-$maxCount $chance", "block")
        issuerSender.sendInfo(MessageKeys.ITEM_DROP_SOURCE_SET)
    }

    @Subcommand("removeDropBlockSource")
    @CommandCompletion("@blockDropSource @nothing")
    @CommandPermission("bmc.item.drop_source")
    @Description("{@@bmc.command.description.item.drop_source.remove_block}")
    @Syntax("<block_pattern>")
    fun onRemoveDropBlockSource(playerSender: Player, @Values("@blockDropSource") blockPattern: String) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        ItemManager.removeDrop(item, blockPattern, "block")
        issuerSender.sendInfo(MessageKeys.ITEM_DROP_SOURCE_REMOVED)
    }

    @Subcommand("removeAllDropBlockSource")
    @CommandCompletion("@nothing")
    @CommandPermission("bmc.item.drop_source")
    @Description("{@@bmc.command.description.item.drop_source.remove_all_block}")
    @Syntax("")
    fun onRemoveAllDropBlockSource(playerSender: Player) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        ItemManager.removeAllDrops(item, "block")
        issuerSender.sendInfo(MessageKeys.ITEM_DROP_SOURCES_REMOVED)
    }

    @Subcommand("getAllDropBlockSource")
    @CommandCompletion("@nothing")
    @CommandPermission("bmc.item.drop_source")
    @Description("{@@bmc.command.description.item.drop_source.get_all_block}")
    @Syntax("")
    fun onGetAllDropBlockSource(playerSender: Player) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        issuerSender.sendInfo(MessageKeys.ITEM_DROP_SOURCES_GET, "{sources}", ItemManager.getDrops(item, "block")?.joinToString(",") ?: "null")
    }

    @Subcommand("addDropEntitySource")
    @CommandCompletion("@entityDropSource [min_count] [max_count] [chance(between_0_and_1)] @nothing")
    @CommandPermission("bmc.item.drop_source")
    @Description("{@@bmc.command.description.item.drop_source.add_entity}")
    @Syntax("<block_pattern> [min_count] [max_count] [chance(between_0_and_1)]")
    fun onAddDropEntitySource(playerSender: Player, @Values("@entityDropSource") blockPattern: String, @Optional minCount: Int?, @Optional maxCount: Int?, @Optional chance: Double) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        ItemManager.addDrop(item, "$blockPattern $minCount-$maxCount $chance", "entity")
        issuerSender.sendInfo(MessageKeys.ITEM_DROP_SOURCE_SET)
    }

    @Subcommand("removeDropEntitySource")
    @CommandCompletion("@entityDropSource @nothing")
    @CommandPermission("bmc.item.drop_source")
    @Description("{@@bmc.command.description.item.drop_source.remove_entity}")
    @Syntax("<block_pattern>")
    fun onRemoveDropEntitySource(playerSender: Player, @Values("@entityDropSource") blockPattern: String) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        ItemManager.removeDrop(item, blockPattern, "entity")
        issuerSender.sendInfo(MessageKeys.ITEM_DROP_SOURCE_REMOVED)
    }

    @Subcommand("removeAllDropEntitySource")
    @CommandCompletion("@nothing")
    @CommandPermission("bmc.item.drop_source")
    @Description("{@@bmc.command.description.item.drop_source.remove_all_entity}")
    @Syntax("")
    fun onRemoveAllDropEntitySource(playerSender: Player) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        ItemManager.removeAllDrops(item, "entity")
        issuerSender.sendInfo(MessageKeys.ITEM_DROP_SOURCES_REMOVED)
    }

    @Subcommand("getAllDropEntitySource")
    @CommandCompletion("@nothing")
    @CommandPermission("bmc.item.drop_source")
    @Description("{@@bmc.command.description.item.drop_source.get_all_entity}")
    @Syntax("")
    fun onGetAllEntityBlockSource(playerSender: Player) {
        val issuerSender: CommandIssuer = commandManager?.getCommandIssuer(playerSender) ?: return
        val item = playerSender.equipment?.itemInMainHand
        if (item?.type == Material.AIR || item?.itemMeta == null) {
            issuerSender.sendError(MessageKeys.NEED_HOLD_ITEM)
            return
        }
        issuerSender.sendInfo(MessageKeys.ITEM_DROP_SOURCES_GET, "{sources}", ItemManager.getDrops(item, "entity")?.joinToString(",") ?: "null")
    }
}