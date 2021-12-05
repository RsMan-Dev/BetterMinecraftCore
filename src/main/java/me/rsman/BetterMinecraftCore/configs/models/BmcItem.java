package me.rsman.BetterMinecraftCore.configs.models;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.rsman.BetterMinecraftCore.Managers.ItemManager;
import me.rsman.BetterMinecraftCore.enums.EAttributes;
import me.rsman.BetterMinecraftCore.enums.EEnchants;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BmcItem {
    private String name;
    private String displayName;
    private Integer materialId;
    private String material;
    private List<String> lore;
    private HashMap<String, Long> attributes;
    private HashMap<String, Integer> enchants;
    private Boolean unbreakable;
    private Boolean renamable;
    private int rev;

    public BmcItem() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public HashMap<String, Long> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Long> attributes) {
        if(attributes != null) {
            for (Map.Entry<String, Long> attr: attributes.entrySet()) {
                if(!EAttributes.getAllKeys().contains(attr.getKey())){
                    attributes.remove(attr.getKey());
                }
            }
        }
        this.attributes = attributes;
    }

    public HashMap<String, Integer> getEnchants() {
        return enchants;
    }

    public void setEnchants(HashMap<String, Integer> enchants) {
        if(enchants != null){
            for (Map.Entry<String, Integer> ench: enchants.entrySet()) {
                if(!EEnchants.getEnumKeys().contains(ench.getKey())){
                    enchants.remove(ench.getKey());
                }
            }
        }
        this.enchants = enchants;
    }

    public Boolean isUnbreakable() {
        return unbreakable;
    }

    public void setUnbreakable(Boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    public Boolean isRenamable() {
        return renamable;
    }

    public void setRenamable(Boolean renamable) {
        this.renamable = renamable;
    }

    public int getRev() {
        return rev;
    }

    public void setRev(int rev) {
        this.rev = rev;
    }

    @Override
    public String toString() {
        return "BmcItem{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", materialId=" + materialId +
                ", material='" + material + '\'' +
                ", lore=" + lore +
                ", attributes=" + attributes +
                ", enchants=" + enchants +
                ", unbreakable=" + unbreakable +
                ", renamable=" + renamable +
                ", rev=" + rev +
                '}';
    }

    @Override
    public BmcItem clone() {
        BmcItem i = new BmcItem();
        i.setName(name);
        i.setDisplayName(displayName);
        i.setMaterialId(materialId);
        i.setAttributes(attributes);
        i.setMaterial(material);
        i.setLore(lore);
        i.setEnchants(enchants);
        i.setUnbreakable(unbreakable);
        i.setRenamable(renamable);
        i.setRev(rev);
        return i;
    }

    public ItemStack getItemStack() {
        ItemStack itemTR = new ItemStack(Material.valueOf(material));
        if(name != null) ItemManager.setItemName(itemTR, name);
        if(lore != null && lore.size() != 0) ItemManager.setCustomLoreAll(itemTR, lore);
        if(attributes != null){
            for(Map.Entry<String, Long> attr: attributes.entrySet()){
                ItemManager.setItemAttr(itemTR, attr.getKey(), attr.getValue());
            }
        }
        if(enchants != null){
            for(Map.Entry<String, Integer> ench: enchants.entrySet()){
                itemTR.addUnsafeEnchantment(EEnchants.valueOf(ench.getKey()).getEnchant(), ench.getValue());
            }
        }
        if(renamable != null)ItemManager.setRenamable(itemTR, renamable);
        if(renamable == null)ItemManager.setRenamable(itemTR, false);
        ItemManager.setItemRev(itemTR, rev);
        ItemMeta itemTRMeta = itemTR.getItemMeta();
        if(itemTRMeta != null){
            if(displayName != null) itemTRMeta.setDisplayName(displayName);
            if(materialId != null)itemTRMeta.setCustomModelData(materialId);
            if(unbreakable != null)itemTRMeta.setUnbreakable(unbreakable);
        }
        itemTR.setItemMeta(itemTRMeta);
        ItemManager.updateItemLore(itemTR);
        return itemTR;
    }

    public static BmcItem parseItemStack(ItemStack item) {
        ItemMeta im = item.getItemMeta();
        BmcItem itemTS = new BmcItem();
        itemTS.material = item.getType().name();
        if(!ItemManager.getItemName(item).equals("")) itemTS.name = ItemManager.getItemName(item);
        if(ItemManager.getCustomLoreAll(item).size() > 0) itemTS.lore = ItemManager.getCustomLoreAll(item);
        HashMap<String, Long> itemAttrs = new HashMap<>();
        for (EAttributes ea : EAttributes.values()){
            if(ItemManager.hasItemAttr(item, ea.getKey()))
                itemAttrs.put(ea.getKey(), ItemManager.getItemAttr(item, ea.getKey()));
        }
        itemTS.attributes = itemAttrs;
        HashMap<String, Integer> itemEnchs = new HashMap<>();
        for (Map.Entry<Enchantment, Integer> ench: item.getEnchantments().entrySet()){
            itemEnchs.put(EEnchants.getEnumKeyFromKey(ench.getKey().getKey().toString().replaceFirst("minecraft:", "")), ench.getValue());
        }
        itemTS.enchants = itemEnchs;
        itemTS.renamable = ItemManager.isRenamable(item) ? true : null;
        if(im != null){
            if(im.hasDisplayName()) itemTS.displayName = im.getDisplayName();
            if(im.hasCustomModelData()) itemTS.materialId = im.getCustomModelData();
            itemTS.unbreakable = im.isUnbreakable() ? true : null;
        }
        itemTS.rev = ItemManager.getItemRev(item);


        return itemTS;
    }
}
