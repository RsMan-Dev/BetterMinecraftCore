package fr.rsman.betterMinecraftCore.managers

import org.bukkit.inventory.ItemStack

@Suppress("BooleanMethodIsAlwaysInverted")
object ItemTypeChecker {
    fun isSword(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("_SWORD")
    }

    fun isAxe(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("_AXE")
    }

    fun isPickaxe(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("_PICKAXE")
    }

    fun isShovel(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("_SHOVEL")
    }

    fun isHoe(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("_HOE")
    }

    fun isShears(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("SHEARS")
    }

    fun isBow(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("BOW")
    }

    fun isHead(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("_HEAD")
    }

    fun isHelmet(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("_HELMET")
    }

    fun isChestplate(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("_CHESTPLATE")
    }

    fun isLeggings(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("_LEGGINGS")
    }

    fun isBoots(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("_BOOTS")
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun isWeapon(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("_SWORD") || name.contains("_AXE") || name.contains("BOW")
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun isTool(item: ItemStack): Boolean {
        val name = item.type.name
        return name.contains("_AXE") || name.contains("_PICKAXE") || name.contains("_SHOVEL") || name.contains("_HOE") || name.contains("SHEARS")
    }

    fun isToolOrWeapon(item: ItemStack): Boolean {
        return isWeapon(item) || isTool(item)
    }

    fun isArmor(item: ItemStack?): Boolean {
        val name = item!!.type.name
        return name.contains("_CHESTPLATE") || name.contains("_LEGGINGS") || name.contains("_BOOTS") || name.contains("_HELMET")
    }

    fun isArmorOrHead(item: ItemStack?): Boolean {
        val name = item!!.type.name
        return name.contains("_CHESTPLATE") || name.contains("_LEGGINGS") || name.contains("_BOOTS") || name.contains("_HELMET") || name.contains("_HEAD")
    }
}