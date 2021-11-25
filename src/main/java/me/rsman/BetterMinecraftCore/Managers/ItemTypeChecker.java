package me.rsman.BetterMinecraftCore.Managers;

import org.bukkit.inventory.ItemStack;

public final class ItemTypeChecker {
    public static boolean isSword(ItemStack item){
        String name = item.getType().name();
        return name.contains("_SWORD");
    }
    public static boolean isAxe(ItemStack item){
        String name = item.getType().name();
        return name.contains("_AXE");
    }
    public static boolean isPickaxe(ItemStack item){
        String name = item.getType().name();
        return name.contains("_PICKAXE");
    }
    public static boolean isShovel(ItemStack item){
        String name = item.getType().name();
        return name.contains("_SHOVEL");
    }
    public static boolean isHoe(ItemStack item){
        String name = item.getType().name();
        return name.contains("_HOE");
    }
    public static boolean isShears(ItemStack item){
        String name = item.getType().name();
        return name.contains("SHEARS");
    }
    public static boolean isBow(ItemStack item){
        String name = item.getType().name();
        return name.contains("BOW");
    }
    public static boolean isHead(ItemStack item){
        String name = item.getType().name();
        return name.contains("_HEAD");
    }
    public static boolean isHelmet(ItemStack item){
        String name = item.getType().name();
        return name.contains("_HELMET");
    }
    public static boolean isChestplate(ItemStack item){
        String name = item.getType().name();
        return name.contains("_CHESTPLATE");
    }
    public static boolean isLeggings(ItemStack item){
        String name = item.getType().name();
        return name.contains("_LEGGINGS");
    }
    public static boolean isBoots(ItemStack item){
        String name = item.getType().name();
        return name.contains("_BOOTS");
    }
    public static boolean isWeapon(ItemStack item){
        String name = item.getType().name();
        return name.contains("_SWORD") || name.contains("_AXE") || name.contains("BOW");
    }
    public static boolean isTool(ItemStack item){
        String name = item.getType().name();
        return name.contains("_AXE") || name.contains("_PICKAXE") || name.contains("_SHOVEL") || name.contains("_HOE") || name.contains("SHEARS");
    }
    public static boolean isToolOrWeapon(ItemStack item){
        return isWeapon(item) || isTool(item);
    }
    public static boolean isArmor(ItemStack item){
        String name = item.getType().name();
        return name.contains("_CHESTPLATE") || name.contains("_LEGGINGS") || name.contains("_BOOTS") || name.contains("_HELMET");
    }
    public static boolean isArmorOrHead(ItemStack item){
        String name = item.getType().name();
        return name.contains("_CHESTPLATE") || name.contains("_LEGGINGS") || name.contains("_BOOTS") || name.contains("_HELMET") || name.contains("_HEAD");
    }
}
