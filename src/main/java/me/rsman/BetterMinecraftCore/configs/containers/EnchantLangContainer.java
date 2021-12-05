package me.rsman.BetterMinecraftCore.configs.containers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.configs.ConfigLoader;
import me.rsman.BetterMinecraftCore.enums.EEnchants;

import java.util.HashMap;
import java.util.Map;

public class EnchantLangContainer {
    private static EnchantLangContainer instance;

    private static void setInstance(EnchantLangContainer instance) {
        EnchantLangContainer.instance = instance;
    }

    public static EnchantLangContainer getInstance() {
        return instance;
    }

    public static void load(){
        setInstance(null);
        BetterMinecraftCore.getInstance().getLogger().info("§3Loading Enchants lang...");
        EnchantLangContainer enchantLangContainerInstance = ConfigLoader.loadConfig("lang/enchants", EnchantLangContainer.class);
        BetterMinecraftCore.getInstance().getLogger().info("§bLoaded Enchants lang.");
        setInstance(enchantLangContainerInstance);
    }
    private Map<String, String> translations;

    public EnchantLangContainer() {}

    public void setTranslations(Map<String, String> translations) {
        for (Map.Entry<String, String> item: translations.entrySet()) {
            if(!EEnchants.getEnumKeys().contains(item.getKey())){
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
