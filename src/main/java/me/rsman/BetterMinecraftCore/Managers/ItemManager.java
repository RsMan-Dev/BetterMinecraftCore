package me.rsman.BetterMinecraftCore.Managers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Enchantments.CustomEnchantClass;
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer;
import me.rsman.BetterMinecraftCore.enums.EAttributes;
import me.rsman.BetterMinecraftCore.enums.EEnchants;
import me.rsman.BetterMinecraftCore.formatters.LoreFormatter;
import me.rsman.BetterMinecraftCore.utils.NBT;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public final class ItemManager {

    public static void setItemName(ItemStack item, String value){
        if(value.equals("")){
            NBT.remove(item, "name");
        }else{
            NBT.set(item, "name", PersistentDataType.STRING, value);
        }
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
    public static void setRenamable(ItemStack item, boolean val){
        NBT.set(item,"renamable", PersistentDataType.BYTE, (byte)(val? 1: 0));
    }
    public static boolean isRenamable(ItemStack item){
        Byte renamable = (Byte)NBT.get(item,"renamable", PersistentDataType.BYTE);
        return (renamable == null || renamable == 1);
    }
    public static void setItemAttr(ItemStack item, String attr, long value){
        NBT.set(item, "attributes/"+attr, PersistentDataType.LONG, value);
        updateItemLore(item);
    }
    public static long getItemAttr(ItemStack item, String attr){
        Long value = (Long) NBT.get(item, "attributes/"+attr, PersistentDataType.LONG);
        if (value == null) value = 0L;
        return value;
    }
    public static long getItemEnchantAttr(ItemStack item, String attr){
        long value = 0;
        for (Map.Entry<Enchantment, Integer> enchant: item.getEnchantments().entrySet()) {
            String enchKey = enchant.getKey().getKey().toString();
            if(enchKey.startsWith(NamespacedKey.minecraft("bmc_").toString())){
                EEnchants ench = EEnchants.valueOf(EEnchants.getEnumKeyFromKey(enchKey.replace("minecraft:", "")));
                if(((CustomEnchantClass)ench.getEnchant()).hasAttributesModifiers()) {
                    Map<String, Long> modifiers = ((CustomEnchantClass) ench.getEnchant()).getAttributesModifiers();
                    if(modifiers.containsKey(attr)){
                        value += modifiers.get(attr) * enchant.getValue();
                    }
                }
            }
        }
        return value;
    }
    public static long getFinalItemAttr(ItemStack item, String attr){
        return getItemAttr(item, attr) + getItemEnchantAttr(item, attr);
    }
    public static boolean hasItemAttr(ItemStack item, String attr){
        return NBT.get(item, "attributes/"+attr, PersistentDataType.LONG) != null;
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

        itemMeta.setLore(LoreFormatter.format(item));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        if(itemMeta.getAttributeModifiers() != null){
            itemMeta.getAttributeModifiers().clear();
        }
        item.setItemMeta(itemMeta);
    }
    public static void updateItem(ItemStack item){
        if(!BmcItemContainer.getInstance().getItems().containsKey(ItemManager.getItemName(item))) {
            if(!ItemManager.getItemName(item).equals("")){
                ItemManager.setItemName(item, "");
                ItemManager.setItemRev(item, 0);
            }
            return;
        }
        ItemStack itemInConf = BmcItemContainer.getInstance().getItems().get(ItemManager.getItemName(item)).getItemStack();
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
        for (String attr : EAttributes.getAllKeys()) {
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


}
