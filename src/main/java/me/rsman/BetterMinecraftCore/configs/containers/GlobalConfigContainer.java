package me.rsman.BetterMinecraftCore.configs.containers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.configs.ConfigLoader;
import me.rsman.BetterMinecraftCore.configs.models.BmcItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GlobalConfigContainer {
    private static GlobalConfigContainer instance;

    private static void setInstance(GlobalConfigContainer instance) {
        GlobalConfigContainer.instance = instance;
    }

    public static GlobalConfigContainer getInstance() {
        return instance;
    }

    public static void load(){
        setInstance(null);
        BetterMinecraftCore.getInstance().getLogger().info("§3Loading BMC config...");
        GlobalConfigContainer globalConfigContainerInstance = ConfigLoader.loadConfig("global", GlobalConfigContainer.class);
        if(globalConfigContainerInstance == null){
            BetterMinecraftCore.getInstance().getLogger().severe("§4Config cannot be loaded");
        } else {
            BetterMinecraftCore.getInstance().getLogger().info("§bLoaded global config." );
            BetterMinecraftCore.getInstance().getLogger().info("§bverbose mode §6" + (globalConfigContainerInstance.verbose ? "on" : "off") );
        }
        setInstance(globalConfigContainerInstance);
    }

    private boolean verbose;

    private String db_host;
    private String db_port;
    private String db_database;
    private String db_user;
    private String db_password;

    private String attribute_display_format;
    private String attribute_modifier_display_format;
    private String enchant_display_format;
    private String enchant_separator_display_format;
    private String action_bar_display_format;


    public GlobalConfigContainer() { }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getDb_host() {
        return db_host;
    }

    public void setDb_host(String db_host) {
        this.db_host = db_host;
    }

    public String getDb_port() {
        return db_port;
    }

    public void setDb_port(String db_port) {
        this.db_port = db_port;
    }

    public String getDb_database() {
        return db_database;
    }

    public void setDb_database(String db_database) {
        this.db_database = db_database;
    }

    public String getDb_user() {
        return db_user;
    }

    public void setDb_user(String db_user) {
        this.db_user = db_user;
    }

    public String getDb_password() {
        return db_password;
    }

    public void setDb_password(String db_password) {
        this.db_password = db_password;
    }

    public String getAttribute_display_format() {
        return attribute_display_format;
    }

    public void setAttribute_display_format(String attribute_display_format) {
        this.attribute_display_format = attribute_display_format;
    }

    public String getAttribute_modifier_display_format() {
        return attribute_modifier_display_format;
    }

    public void setAttribute_modifier_display_format(String attribute_modifier_display_format) {
        this.attribute_modifier_display_format = attribute_modifier_display_format;
    }

    public String getEnchant_display_format() {
        return enchant_display_format;
    }

    public void setEnchant_display_format(String enchant_display_format) {
        this.enchant_display_format = enchant_display_format;
    }

    public String getEnchant_separator_display_format() {
        return enchant_separator_display_format;
    }

    public void setEnchant_separator_display_format(String enchant_separator_display_format) {
        this.enchant_separator_display_format = enchant_separator_display_format;
    }

    public String getAction_bar_display_format() {
        return action_bar_display_format;
    }

    public void setAction_bar_display_format(String action_bar_display_format) {
        this.action_bar_display_format = action_bar_display_format;
    }
}
