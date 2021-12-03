package me.rsman.BetterMinecraftCore.Managers.Command;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.CommandKits.CraftCommands;
import me.rsman.BetterMinecraftCore.CommandKits.ItemCommands;
import me.rsman.BetterMinecraftCore.CommandKits.PlayerCommands;
import me.rsman.BetterMinecraftCore.CommandKits.RootCommands;
import me.rsman.BetterMinecraftCore.Managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

public final class CommandManager extends PaperCommandManager {
    public static CommandManager instance;
    public CommandManager(Plugin plugin) {
        super(plugin);
        this.enableUnstableAPI("help");

        this.setFormat(MessageType.SYNTAX, ChatColor.AQUA, ChatColor.BLUE);
        this.setFormat(MessageType.INFO, ChatColor.AQUA, ChatColor.BLUE);
        this.setFormat(MessageType.HELP, ChatColor.AQUA, ChatColor.BLUE, ChatColor.GRAY);
        this.setFormat(MessageType.ERROR, ChatColor.RED, ChatColor.YELLOW);
        try {
            this.getLocales().loadYamlLanguageFile("commands/commandMessages.yml", Locale.ENGLISH);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().severe("Failed to load ACF core language file");
            e.printStackTrace();
        }

        this.getLocales().setDefaultLocale(Locale.ENGLISH);
    }

    public static void init(){
        ConfigManager.getConfig("commands/commandMessages");
        if(instance == null) {
            instance = new CommandManager(BetterMinecraftCore.getInstance());
            instance.registerCommand(new RootCommands());
            instance.registerCommand(new ItemCommands());
            instance.registerCommand(new PlayerCommands());
            instance.registerCommand(new CraftCommands());
        }
    }

    public static CommandManager get(){
        if(instance == null) { init(); }
        return instance;
    }
}
