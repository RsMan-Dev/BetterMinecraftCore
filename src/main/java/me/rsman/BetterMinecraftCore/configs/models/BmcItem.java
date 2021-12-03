package me.rsman.BetterMinecraftCore.configs.models;

import java.util.HashMap;
import java.util.List;

public class BmcItem {
    private String displayName;
    private int materialId;
    private String material;
    private List<String> lore;
    private HashMap<String, Integer> attributes;
    private HashMap<String, Integer> enchants;
    private boolean unbreakable;
    private boolean renamable;

    public BmcItem() {}

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
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

    public HashMap<String, Integer> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Integer> attributes) {
        this.attributes = attributes;
    }

    public HashMap<String, Integer> getEnchants() {
        return enchants;
    }

    public void setEnchants(HashMap<String, Integer> enchants) {
        this.enchants = enchants;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    public boolean isRenamable() {
        return renamable;
    }

    public void setRenamable(boolean renamable) {
        this.renamable = renamable;
    }

    @Override
    public String toString() {
        return "BmcItem{" +
                "displayName='" + displayName + '\'' +
                ", materialId=" + materialId +
                ", material='" + material + '\'' +
                ", lore=" + lore +
                ", attributes=" + attributes +
                ", enchants=" + enchants +
                ", unbreakable=" + unbreakable +
                ", renamable=" + renamable +
                '}';
    }
}
