package me.rsman.BetterMinecraftCore.enums;

import me.rsman.BetterMinecraftCore.Enchantments.Aiming;
import me.rsman.BetterMinecraftCore.Enchantments.Protection;
import me.rsman.BetterMinecraftCore.Enchantments.Telekinesis;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.*;

public enum EEnchants {
    AQUA_INFINITY           ("aqua_affinity",           Enchantment.WATER_WORKER),
    BANE_OF_ARTHROPODS      ("bane_of_arthropods",      Enchantment.DAMAGE_ARTHROPODS),
    BLAST_PROTECTION        ("blast_protection",        Enchantment.PROTECTION_EXPLOSIONS),
    CHANNELING              ("channeling",              Enchantment.CHANNELING),
    BINDING_CURSE           ("binding_curse",           Enchantment.BINDING_CURSE),
    VANISHING_CURSE         ("vanishing_curse",         Enchantment.VANISHING_CURSE),
    DEPTH_STRIDER           ("depth_strider",           Enchantment.DEPTH_STRIDER),
    EFFICIENCY              ("efficiency",              Enchantment.DIG_SPEED),
    FEATHER_FALLING         ("efficiency",              Enchantment.DIG_SPEED),
    FIRE_ASPECT             ("fire_aspect",             Enchantment.FIRE_ASPECT),
    FIRE_PROTECTION         ("fire_protection",         Enchantment.PROTECTION_FIRE),
    FLAME                   ("flame",                   Enchantment.ARROW_FIRE),
    FORTUNE                 ("fortune",                 Enchantment.LOOT_BONUS_BLOCKS),
    FROST_WALKER            ("frost_walker",            Enchantment.FROST_WALKER),
    IMPALING                ("impaling",                Enchantment.IMPALING),
    INFINITY                ("infinity",                Enchantment.ARROW_INFINITE),
    KNOCKBACK               ("knockback",               Enchantment.KNOCKBACK),
    LOYALTY                 ("loyalty",                 Enchantment.LOYALTY),
    LUCK_OF_THE_SEA         ("luck_of_the_sea",         Enchantment.LUCK),
    LURE                    ("lure",                    Enchantment.LURE),
    MENDING                 ("mending",                 Enchantment.MENDING),
    MULTISHOT               ("multishot",               Enchantment.MULTISHOT),
    PIERCING                ("piercing",                Enchantment.PIERCING),
    POWER                   ("power",                   Enchantment.ARROW_DAMAGE),
    PROJECTILE_PROTECTION   ("projectile_protection",   Enchantment.PROTECTION_PROJECTILE),
    OLD_PROTECTION          ("protection",              Enchantment.PROTECTION_ENVIRONMENTAL),
    PUNCH                   ("punch",                   Enchantment.ARROW_KNOCKBACK),
    QUICK_CHARGE            ("quick_charge",            Enchantment.QUICK_CHARGE),
    RESPIRATION             ("respiration",             Enchantment.OXYGEN),
    RIPTIDE                 ("riptide",                 Enchantment.RIPTIDE),
    SHARPNESS               ("sharpness",               Enchantment.DAMAGE_ALL),
    SILK_TOUCH              ("silk_touch",              Enchantment.SILK_TOUCH),
    SMITE                   ("smite",                   Enchantment.DAMAGE_UNDEAD),
    SOUL_SPEED              ("soul_speed",              Enchantment.SOUL_SPEED),
    SWEEPING                ("sweeping",                Enchantment.SWEEPING_EDGE),
    UNBREAKING              ("unbreaking",              Enchantment.DURABILITY),
    THORNS                  ("thorns",                  Enchantment.THORNS),
    LOOTING                 ("looting",                 Enchantment.LOOT_BONUS_MOBS),


    PROTECTION              ("bmc_protection",          Protection.getEnchant(), new EEnchants[]{BLAST_PROTECTION, OLD_PROTECTION, PROJECTILE_PROTECTION, FIRE_PROTECTION}),
    TELEKINESIS             ("bmc_telekinesis",         Telekinesis.getEnchant()),
    AIMING                  ("bmc_aiming",              Aiming.getEnchant())
    ;

    private final String key;
    private final Enchantment enchant;
    private final EEnchants[] replaces;

    private static  List<String> enumKeys;
    private static  List<String> nonReplacedEnumKeys;
    private static  HashMap<String, Enchantment> replacesMap;

    EEnchants(String key, Enchantment enchant, EEnchants[] replaces) {
        this.key = key;
        this.enchant = enchant;
        this.replaces = replaces;
    }
    EEnchants(String key, Enchantment enchant) {
        this.key = key;
        this.enchant = enchant;
        this.replaces = new EEnchants[]{};
    }

    public String getKey() {
        return key;
    }

    public Enchantment getEnchant() {
        return enchant;
    }

    public EEnchants[] getReplaces() {
        return replaces;
    }

    public static HashMap<String, Enchantment> getReplacesMapFromNamespaces(){
        if(replacesMap != null) return replacesMap;
        HashMap<String, Enchantment> map = new HashMap<>();
        for (EEnchants ee: EEnchants.values()) {
            for (EEnchants ee2: ee.getReplaces()) {
                map.put(NamespacedKey.minecraft(ee2.getKey()).toString(), ee.getEnchant());
            }
        }
        replacesMap = map; return replacesMap;
    }
    public static List<String> getEnumKeys(){
        if(enumKeys != null) return enumKeys;
        List<String> keys = new ArrayList<>();
        for (EEnchants ee: EEnchants.values()) {
            keys.add(ee.name());
        }
        enumKeys = keys; return enumKeys;
    }
    public static List<String> getNonReplacedEnumKeys(){
        if(nonReplacedEnumKeys != null) return nonReplacedEnumKeys;
        List<String> keys = new ArrayList<>();
        List<EEnchants> replaced = new ArrayList<>();
        for (EEnchants ee: EEnchants.values()) {
            keys.add(ee.name());
            replaced.addAll(Arrays.asList(ee.getReplaces()));
        }
        for(EEnchants ee: replaced){
            keys.remove(ee.name());
        }
        nonReplacedEnumKeys = keys; return nonReplacedEnumKeys;
    }

    public NamespacedKey getMinecraftKey(){
        return NamespacedKey.minecraft(key);
    }

    public static String getEnumKeyFromKey(String key){
        for (EEnchants ee: EEnchants.values()) {
            if(ee.getKey().equals(key)) return ee.name();
        }
        return null;
    }
}
