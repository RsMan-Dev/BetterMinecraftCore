package me.rsman.BetterMinecraftCore.Managers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class ConfigManager {
    public static Map<String, FileConfiguration> config = new HashMap<>();

    public static FileConfiguration getConfig(String name){
        return ConfigManager.getConfig(name, false);
    }
    public static FileConfiguration getConfig(String name, boolean force){

        if(config.containsKey(name) && !force) return config.get(name);

        File file = new File(BetterMinecraftCore.getInstance().getDataFolder(), name + ".yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            BetterMinecraftCore.getInstance().saveResource(name + ".yml", false);
        }

        FileConfiguration configObj = new YamlConfiguration();
        try {
            configObj.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        config.put(name, configObj);

        return ConfigManager.config.get(name);
    }

    public static void setConfig(String name, FileConfiguration configObj){
        config.put(name, configObj);
    }

    public static Object getKey(String name, String key, String type, Object defaultValue) {
        FileConfiguration config = ConfigManager.getConfig(name);
        try {
            Method method = config.getClass().getMethod("get" + type, String.class);
            Object value = method.invoke(config, key);
            if (!type.equals("Boolean") && value != (Boolean) false && value != null) {
                return value;
            } else if (type.equals("Boolean") && config.isBoolean(name)) {
                return value;
            } else {
                ConfigManager.setKey(name, key, defaultValue);
                ConfigManager.setConfig(name, config);
                return method.invoke(config, key);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            BetterMinecraftCore.getInstance().getLogger().warning(e.toString());
        }
        return null;
    }

    public static Set<String> getKeys(String name, String key) {
        FileConfiguration config = ConfigManager.getConfig(name);

        if(config.getConfigurationSection(key) == null){
            ConfigManager.setKey(name, key, new HashMap<>());
            ConfigManager.setConfig(name, config);
            config = ConfigManager.getConfig(name);
        }

        return Objects.requireNonNull(config.getConfigurationSection(key)).getKeys(false);
    }

    public static void setKey(String name, String key, Object value) {
        FileConfiguration config = ConfigManager.getConfig(name);

        config.set(key, value);

        try{
            config.save(new File(BetterMinecraftCore.getInstance().getDataFolder(), name + ".yml"));
        } catch (IOException e){
            BetterMinecraftCore.getInstance().getLogger().warning(e.toString());
        }
    }
}
