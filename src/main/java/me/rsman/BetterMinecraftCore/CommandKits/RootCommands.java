package me.rsman.BetterMinecraftCore.CommandKits;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Managers.*;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager;
import me.rsman.BetterMinecraftCore.Managers.Command.Lang.MessageKeys;
import me.rsman.BetterMinecraftCore.configs.ConfigLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Locale;

@CommandAlias("bmc|betterminecraftcore")
public class RootCommands extends BaseCommand {
    private final CommandManager commandManager = CommandManager.get();

    @HelpCommand
    @CommandCompletion("")
    @Syntax("")
    public void doHelp(Player playerSender, CommandHelp help) {
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        issuerSender.sendInfo(MessageKeys.BMC_HELP);
        help.showHelp();
    }

    @Subcommand("reload")
    @CommandPermission("bmc.admin")
    @Description("{@@bmc.command.description.reload}")
    public void onReload(Player playerSender) {
        CraftManager.initCrafts();
        ConfigLoader.init();
        try {
            commandManager.getLocales().loadYamlLanguageFile("lang/commandMessages.yml", Locale.ENGLISH);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        BetterMinecraftCore.getInstance().getServer().getOnlinePlayers().forEach((player) -> {
            PlayerManager.getBaseAttributes(player.getUniqueId().toString(), true);
            PlayerManager.alterPlayerAttributesWithEquippedStuff(player);
        });
    }
}
