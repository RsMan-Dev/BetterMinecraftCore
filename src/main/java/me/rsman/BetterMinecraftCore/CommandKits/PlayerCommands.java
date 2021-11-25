package me.rsman.BetterMinecraftCore.CommandKits;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager;
import me.rsman.BetterMinecraftCore.Managers.Command.Lang.MessageKeys;
import me.rsman.BetterMinecraftCore.Managers.PlayerManager;
import org.bukkit.entity.Player;

@CommandAlias("bmc|betterminecraftcore")
@Subcommand("player|p")
public class PlayerCommands extends BaseCommand {

    //all config wiped out, using config for database data is useless.
    private final CommandManager commandManager = CommandManager.get();

    @Subcommand("getAttribute")
    @CommandCompletion("@attribute @players")
    @CommandPermission("hc.player.attribute.get")
    @Description("{@@bmc.command.description.attribute.get}")
    @Syntax("<attr> <player>")
    public void onGetAttr(Player playerSender, @Values("@attribute") String attr, @Optional @Values("@players") OnlinePlayer player){
        Player playerChecked = player != null ? player.getPlayer() : playerSender;
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        issuerSender.sendInfo(MessageKeys.PLAYER_ATTRIBUTE, "{player}", playerChecked.getDisplayName(), "{attr}", attr, "{val}", PlayerManager.getAttributes(playerChecked.getUniqueId().toString()).get(attr)+"");
    }
    @Subcommand("setBaseAttribute")
    @CommandCompletion("@attribute <value> @players")
    @CommandPermission("hc.player.attribute.set")
    @Description("{@@bmc.command.description.attribute.set}")
    @Syntax("<attr> <value> <player>")
    public void onSetBaseAttr(Player playerSender, @Values("@attribute") String attr, Long value, @Optional @Values("@players") OnlinePlayer player){
        Player playerChecked = player != null ? player.getPlayer() : playerSender;
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        PlayerManager.setBaseAttribute(playerChecked.getUniqueId().toString(), attr, value);
        issuerSender.sendInfo(MessageKeys.PLAYER_BASE_ATTRIBUTE_SET, "{player}", playerChecked.getDisplayName(), "{attr}", attr, "{val}", value+"");
    }
}