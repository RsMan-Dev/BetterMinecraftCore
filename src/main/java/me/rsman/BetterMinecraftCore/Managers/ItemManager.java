package me.rsman.BetterMinecraftCore.Managers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.utils.NBT;
import me.rsman.BetterMinecraftCore.utils.RomanNumber;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public final class ItemManager {
    public static final String[] allowedAttrs = {"damage", "strength", "intelligence", "critChance", "critDamage", "defense", "health", "attackSpeed", "mana", "speed"};
    private static final String[] percentAttrs = {"critChance", "critDamage", "speed", "attackSpeed"};

    public static Map<String, ItemStack> registeredItems = new HashMap<>();

    public static void setItemName(ItemStack item, String value){
        NBT.set(item, "name", PersistentDataType.STRING, value);
    }
    public static String getItemName(ItemStack item){
        String name = (String) NBT.get(item, "name", PersistentDataType.STRING);
        if(name == null) {
            return "";
        }
        return name;
    }
    public static void setItemRev(ItemStack item, int value){
        NBT.set(item, "rev", PersistentDataType.INTEGER, value);
    }
    public static Integer getItemRev(ItemStack item){
        Integer rev = (Integer) NBT.get(item, "rev", PersistentDataType.INTEGER);
        if(rev == null) {
            setItemRev(item, 1);
            return 1;
        }
        return rev;
    }
    public static void setUnbreakable(ItemStack item, boolean val){
        ItemMeta meta = item.getItemMeta();
        if(meta==null)return;
        meta.setUnbreakable(val);
        item.setItemMeta(meta);
        updateItemLore(item);
    }
    public static void setItemAttr(ItemStack item, String stat, int value){
        NBT.set(item, "attributes/"+stat, PersistentDataType.INTEGER, value);
        updateItemLore(item);
    }
    public static Integer getItemAttr(ItemStack item, String attr){
        Integer value = (Integer) NBT.get(item, "attributes/"+attr, PersistentDataType.INTEGER);
        if (value == null) value = 0;
        return value;
    }
    public static Integer getItemEnchantAttr(ItemStack item, String attr){
        int value = 0;
        for (Map.Entry<Enchantment, Integer> enchant: item.getEnchantments().entrySet()) {
            if(EnchantManager.isCustom(enchant.getKey())){
                Map<String, Integer> modifiers = EnchantManager.enchantsAttributesModifiers.get(enchant.getKey().getKey().toString());
                if( modifiers != null ) {
                    if(modifiers.containsKey(attr)){
                        value += modifiers.get(attr) * enchant.getValue();
                    }
                }
            }
        }
        return value;
    }
    public static Integer getFinalItemAttr(ItemStack item, String attr){
        return getItemAttr(item, attr) + getItemEnchantAttr(item, attr);
    }
    public static boolean hasItemAttr(ItemStack item, String attr){
        return NBT.get(item, "attributes/"+attr, PersistentDataType.INTEGER) != null;
    }
    public static void setCustomLore(ItemStack item, String text, Integer line){
        String lore = (String) NBT.get(item, "lore", PersistentDataType.STRING);
        List<String> loreArr = new ArrayList<>();
        if(lore != null) {
            if(!lore.equals("")) loreArr.addAll(Arrays.asList(lore.split("\\|")));
            if(text.equals("null")){
                if(line != null && loreArr.size()-1 <= line){
                    loreArr.remove(line-1);
                }
            }else{
                if(line != null){
                    for(int i=loreArr.size();i<line;i++){
                        loreArr.add("");
                    }
                    loreArr.set(line-1, text);
                } else {
                    loreArr.add(text);
                }
            }
        } else {
            if(line != null){
                for(int i=1;i<line;i++){
                    loreArr.add("");
                }
            }
            loreArr.add(text);
        }
        NBT.set(item, "lore", PersistentDataType.STRING, String.join("|",loreArr));
    }
    public static void setCustomLoreAll(ItemStack item, List<String> texts, boolean replaceAll){
        if(replaceAll){
            NBT.set(item, "lore", PersistentDataType.STRING, "");
        }
        setCustomLoreAll(item, texts);
    }
    public static void setCustomLoreAll(ItemStack item, List<String> texts){
        for (String text: texts) {
            setCustomLore(item, text, null);
        }
    }
    public static List<String> getCustomLoreAll(ItemStack item){
        String lore = (String) NBT.get(item, "lore", PersistentDataType.STRING);
        if(lore == null) return new ArrayList<>();
        return Arrays.asList(lore.split("\\|"));
    }
    public static void updateItemLore(ItemStack item){
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        List<String> lore = new ArrayList<>();
        for (String attr : ItemManager.allowedAttrs) {
            Integer attrVal = getItemAttr(item, attr);
            Integer enchAttrVal = getItemEnchantAttr(item, attr);
            if(attrVal != 0 || enchAttrVal != 0){
                boolean percent = Arrays.asList(ItemManager.percentAttrs).contains(attr);
                lore.add(
                    "§7" + attr.substring(0, 1).toUpperCase() + attr.substring(1) + ":" +
                    "§a +" + attrVal + (percent ? "%" : "") + " " +
                    (enchAttrVal >0 ?"§6(+" + enchAttrVal + (percent ? "%" : "") + ")" : "")
                );
            }
        }

        boolean first = true;
        StringBuilder line = new StringBuilder("§7");
        for (Map.Entry<Enchantment, Integer> enchant: item.getEnchantments().entrySet()) {
            if(enchant.getValue() <= 0)continue;
            if(first){ first = false; lore.add(""); }

            String enchantName = EnchantManager.humanizedNames.get(enchant.getKey().getKey().toString())
                    + " " + RomanNumber.toRoman(enchant.getValue());

            if((line + enchantName).length() < 30){
                if(!line.toString().equals("§7")) line.append(", ");
                line.append(enchantName);
            } else {
                lore.add(line + ",");
                line = new StringBuilder("§7" + enchantName);
            }
        }
        if(!line.toString().equals("§7")){
            lore.add(line.toString());
        }

        String CustomLore = (String) NBT.get(item, "lore", PersistentDataType.STRING);
        if(CustomLore != null && !CustomLore.equals("")) {
            lore.add("");
            CustomLore = CustomLore.replace("&", "§");
            lore.addAll(Arrays.asList(CustomLore.split("\\|")));
        }

        if(itemMeta.isUnbreakable()){
            lore.add("");
            lore.add("§cUnbreakable");
        }

        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        if(itemMeta.getAttributeModifiers() != null){
            itemMeta.getAttributeModifiers().clear();
        }
        item.setItemMeta(itemMeta);
    }
    public static void registerAllItemsWithConfig(){
        ConfigManager.getConfig("items/all", true);
        Set<String> itemNames = ConfigManager.getKeys("items/all", "items");
        itemNames.forEach(name->{
            ItemStack itemTR = new ItemStack(
                    Material.valueOf((String)ConfigManager.getKey("items/all","items."+name+".material", "String", "AIR"))
            );

            ItemManager.setItemName(itemTR, name);

            Integer rev = (Integer)ConfigManager.getKey("items/all","items."+name+".rev", "Int", 1);
            if(rev == null) rev = 1;
            ItemManager.setItemRev(itemTR, rev);

            final ItemMeta[] itemTRMeta = {itemTR.getItemMeta()};
            if(itemTRMeta[0] == null){
                registeredItems.put(name, itemTR);
            } else {
                ConfigManager.getKeys("items/all", "items."+name).forEach(option->{
                    switch (option){
                        case "materialID":
                            itemTRMeta[0].setCustomModelData((Integer)ConfigManager.getKey("items/all","items."+name+".materialID", "Int", 0));
                            break;
                        case "displayName":
                            itemTRMeta[0].setDisplayName((String) ConfigManager.getKey("items/all","items."+name+".displayName", "String", ""));
                            break;
                        case "lore":
                            itemTR.setItemMeta(itemTRMeta[0]);
                            setCustomLoreAll(itemTR, (List<String>) Objects.requireNonNull(ConfigManager.getKey("items/all", "items." + name + ".lore", "StringList", "[]")));
                            itemTRMeta[0] = itemTR.getItemMeta();
                            break;
                        case "unbreakable":
                            itemTRMeta[0].setUnbreakable(((Integer)ConfigManager.getKey("items/all","items."+name+".unbreakable", "Int", "")) != 0);
                            break;
                        case "attributes":
                            itemTR.setItemMeta(itemTRMeta[0]);
                            ConfigManager.getKeys("items/all", "items."+name+".attributes").forEach(attribute->{
                                if(Arrays.asList(ItemManager.allowedAttrs).contains(attribute)){
                                    Integer attrVal = (Integer)ConfigManager.getKey("items/all","items."+name+".attributes."+attribute, "Int", 0);
                                    if(attrVal != null)
                                    ItemManager.setItemAttr(itemTR, attribute, attrVal);
                                }
                            });
                            itemTRMeta[0] = itemTR.getItemMeta();
                            break;
                        case "enchants":
                            itemTR.setItemMeta(itemTRMeta[0]);
                            ConfigManager.getKeys("items/all", "items."+name+".enchants").forEach(enchant->{
                                if(EnchantManager.enchants.containsKey("minecraft:"+enchant)){
                                    Integer enchVal = (Integer)ConfigManager.getKey("items/all","items."+name+".enchants."+enchant, "Int", 0);
                                    if(enchVal != null)
                                    itemTR.addUnsafeEnchantment(EnchantManager.enchants.get("minecraft:"+enchant), enchVal);
                                }
                            });
                            itemTRMeta[0] = itemTR.getItemMeta();
                            break;
                    }
                });
            }
            itemTR.setItemMeta(itemTRMeta[0]);
            if(itemTRMeta[0] != null)
            ItemManager.updateItemLore(itemTR);
            ItemManager.registeredItems.put(name, itemTR);
        });
    }
    public static void deleteItemFromConfig(String name){
        ConfigManager.setKey("items/all", "items."+name, null);
    }
    public static void setItemInConfig(ItemStack item, String name){
        ConfigManager.setKey("items/all", "items."+name, new HashMap<>());
        ConfigManager.setKey("items/all", "items."+name+".material", item.getType().name());
        ConfigManager.setKey("items/all", "items."+name+".rev", getItemRev(item));

        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            if(meta.hasCustomModelData()){
                ConfigManager.setKey("items/all", "items."+name+".materialID", meta.getCustomModelData());
            }
            if(meta.hasDisplayName()){
                ConfigManager.setKey("items/all", "items."+name+".displayName", meta.getDisplayName());
            }
            if(meta.isUnbreakable()){
                ConfigManager.setKey("items/all", "items."+name+".unbreakable", 1);
            }
            String CustomLore = (String) NBT.get(item, "lore", PersistentDataType.STRING);
            if(CustomLore != null){
                ConfigManager.setKey("items/all", "items."+name+".lore", Arrays.asList(CustomLore.split("\\|")));
            }
            boolean first = true;
            for (String attr : ItemManager.allowedAttrs) {
                Integer value = ItemManager.getItemAttr(item, attr);
                if(value != 0){
                    if(first){
                        ConfigManager.setKey("items/all", "items."+name+".attributes", new HashMap<>());
                        first = false;
                    }
                    ConfigManager.setKey("items/all", "items."+name+".attributes."+attr, value);
                }
            }
            if(!item.getEnchantments().isEmpty()){
                ConfigManager.setKey("items/all", "items."+name+".enchants", new HashMap<>());
                for (Map.Entry<Enchantment, Integer> enchant: item.getEnchantments().entrySet()) {
                    ConfigManager.setKey("items/all", "items."+name+".enchants."+enchant.getKey().getKey().toString().replace("minecraft:", ""), enchant.getValue());
                }
            }
        }
    }
    public static void updateItem(ItemStack item){
        ItemStack itemInConf = registeredItems.get(ItemManager.getItemName(item));
        if (itemInConf == null) return;
        if(getItemRev(item).compareTo(getItemRev(itemInConf)) >= 0)return;
        item.setType(itemInConf.getType());
        setItemRev(item, getItemRev(itemInConf));
        item.setData(itemInConf.getData());
        ItemMeta meta = item.getItemMeta();
        ItemMeta metaConf = itemInConf.getItemMeta();
        assert meta != null;
        assert metaConf != null;
        meta.setDisplayName(metaConf.getDisplayName());
        meta.setUnbreakable(metaConf.isUnbreakable());
        item.setItemMeta(meta);
        for (String attr : allowedAttrs) {
            if(hasItemAttr(itemInConf, attr)){
                setItemAttr(item, attr, getItemAttr(itemInConf, attr));
            }
        }
        for (Map.Entry<Enchantment, Integer> ench : itemInConf.getEnchantments().entrySet()) {
            item.addUnsafeEnchantment(ench.getKey(), ench.getValue());
        }
        setCustomLoreAll(item, getCustomLoreAll(itemInConf), true);
        updateItemLore(item);
    }

    public static ItemStack convertItemSchemeToItemStack(String item){
        if(item.equals("m.AIR") || item.equals("AIR") || item.equals("null")){
            return null;
        } else if(item.startsWith("m.")){
            if(Material.matchMaterial(item.substring(2)) == null) return null;
            return new ItemStack(Objects.requireNonNull(Material.matchMaterial(item.substring(2))));
        } else {
            if(ItemManager.registeredItems.containsKey(item)){
                return ItemManager.registeredItems.get(item).clone();
            } else {
                return null;
            }
        }
    }
}
