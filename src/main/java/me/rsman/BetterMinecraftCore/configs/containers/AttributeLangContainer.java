package me.rsman.BetterMinecraftCore.configs.containers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.configs.ConfigLoader;
import me.rsman.BetterMinecraftCore.enums.EAttributes;
import me.rsman.BetterMinecraftCore.enums.EEnchants;

import java.util.HashMap;
import java.util.Map;

public class AttributeLangContainer {
    private static AttributeLangContainer instance;

    private static void setInstance(AttributeLangContainer instance) {
        AttributeLangContainer.instance = instance;
    }

    public static AttributeLangContainer getInstance() {
        return instance;
    }

    public static void load(){
        setInstance(null);
        BetterMinecraftCore.getInstance().getLogger().info("§3Loading Attributes lang...");
        AttributeLangContainer enchantLangContainerInstance = ConfigLoader.loadConfig("lang/attributes", AttributeLangContainer.class);
        BetterMinecraftCore.getInstance().getLogger().info("§bLoaded Attributes lang.");
        setInstance(enchantLangContainerInstance);
    }
    private Map<String, String> translations;

    public AttributeLangContainer() {}

    public void setTranslations(Map<String, String> translations) {
        for (Map.Entry<String, String> item: translations.entrySet()) {
            if(!EAttributes.getAllKeys().contains(item.getKey())){
                BetterMinecraftCore.getInstance().getLogger().info(item.getKey());
                translations.remove(item.getKey());
            }
        }
        this.translations = translations;
    }

    public Map<String, String> getTranslations() {
        return translations;
    }
    public String getTranslation(String key) {
        return translations.get(key);
    }
}
