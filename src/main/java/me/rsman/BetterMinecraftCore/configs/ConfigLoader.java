package me.rsman.BetterMinecraftCore.configs;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer;
import me.rsman.BetterMinecraftCore.configs.models.BmcItem;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.*;
import java.util.Map;

public class ConfigLoader {

    public static <T> T loadConfig(String configFile, Class<T> tClass) {
        try {
            Yaml yaml = new Yaml(new CustomClassLoaderConstructor(ConfigLoader.class.getClassLoader()));
            File file = new File(BetterMinecraftCore.getInstance().getDataFolder(), configFile);
            FileInputStream fileInputStream = new FileInputStream(file);
            return yaml.loadAs(fileInputStream, tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> boolean saveConfig(String configFile, T obj) {
        try {
            Yaml yaml = new Yaml(new CustomClassLoaderConstructor(ConfigLoader.class.getClassLoader()));
            File file = new File(BetterMinecraftCore.getInstance().getDataFolder(), configFile);
            FileWriter fileWriter = new FileWriter(file);
            yaml.dump(obj, fileWriter);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void testSnakeYaml() {
        try {
            BetterMinecraftCore.getInstance().getLogger().warning("Test yaml loader start");
            BmcItemContainer config = loadConfig("items/all.yml", BmcItemContainer.class);
            for (Map.Entry<String, BmcItem> entry : config.getItems().entrySet()) {
                BetterMinecraftCore.getInstance().getLogger().warning(entry.getKey() + " : " + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
