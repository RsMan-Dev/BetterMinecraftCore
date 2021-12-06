package me.rsman.BetterMinecraftCore.configs;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.configs.containers.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class ConfigLoader {


    public static File getFile(String configFile) {
        File file = new File(BetterMinecraftCore.getInstance().getDataFolder(), configFile + ".yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            BetterMinecraftCore.getInstance().saveResource(configFile + ".yml", false);
            file = new File(BetterMinecraftCore.getInstance().getDataFolder(), configFile + ".yml");
        }

        return file;
    }


    public static <T> T loadConfig(String configFile, Class<T> tClass) {
        try {
            Yaml yaml = new Yaml(new CustomClassLoaderConstructor(ConfigLoader.class.getClassLoader()), getRepresenter());
            File file = getFile(configFile);

            FileInputStream fileInputStream = new FileInputStream(file);
            return yaml.loadAs(fileInputStream, tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> boolean saveConfig(String configFile, T obj) {
        try {
            DumperOptions options = new DumperOptions();
            options.setSplitLines(false);
            options.setIndent(2);
            options.setPrettyFlow(true);
            // Fix below - additional configuration
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            Yaml yaml = new Yaml(new CustomClassLoaderConstructor(ConfigLoader.class.getClassLoader()),getRepresenter(), options);
            File file = getFile(configFile);

            BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            yaml.dump(obj, fileWriter);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Representer getRepresenter(){
        Representer representer = new Representer(){
            @Override
            protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
                // if value of property is null, ignore it.
                if (propertyValue == null) { return null; }
                else { return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag); }
            }
        };
        representer.getPropertyUtils().setSkipMissingProperties(true);
        return representer;
    }

    public static void init() {
        GlobalConfigContainer.load();
        BmcItemContainer.load();
        AttributeLangContainer.load();
        EnchantLangContainer.load();
        BmcCraftContainer.load();
    }

}
