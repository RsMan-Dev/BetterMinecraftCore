package me.rsman.BetterMinecraftCore.enums;

import java.util.ArrayList;
import java.util.List;

public enum EAttributes{
    DAMAGE("damage"),
    STRENGTH("strength"),
    INTELLIGENCE("intelligence"),
    CRIT_CHANCE("critChance", true),
    CRIT_DAMAGE("critDamage", true),
    DEFENSE("defense"),
    HEALTH("health"),
    ATTACK_SPEED("attackSpeed", true),
    MANA("mana"),
    SPEED("speed", true),
    ;

    private final String key;
    private final boolean percent;
    private static List<String> allKeys;
    private static List<String> allPercentKeys;

    EAttributes(String key, boolean percent) {
        this.key = key;
        this.percent = percent;
    }
    EAttributes(String key) {
        this.key = key;
        this.percent = false;
    }

    public String getKey() {
        return key;
    }

    public boolean isPercent() {
        return percent;
    }

    public static List<String> getAllKeys(){
        if(allKeys != null) return allKeys;
        List<String> keys = new ArrayList<>();
        for (EAttributes ea: EAttributes.values()) {
            keys.add(ea.getKey());
        }
        allKeys = keys; return allKeys;
    }

    public static List<String> getAllPercentKeys(){
        if(allPercentKeys != null) return allPercentKeys;
        List<String> keys = new ArrayList<>();
        for (EAttributes ea: EAttributes.values()) {
            if(ea.isPercent())keys.add(ea.getKey());
        }
        allPercentKeys = keys; return allPercentKeys;
    }
}