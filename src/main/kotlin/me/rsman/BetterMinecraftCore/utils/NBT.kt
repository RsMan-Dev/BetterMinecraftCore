package me.rsman.BetterMinecraftCore.utils

import me.rsman.BetterMinecraftCore.BetterMinecraftCore.Companion.instance
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import org.bukkit.inventory.ItemStack
import org.bukkit.Bukkit

object NBT {
    private fun getMainData(itemMeta: ItemMeta?): PersistentDataContainer? {
        val namespacedKeyGlobal = NamespacedKey(instance, "mainItemData")
        var data = itemMeta!!.persistentDataContainer.get(namespacedKeyGlobal, PersistentDataType.TAG_CONTAINER)
        val dataContext = itemMeta.persistentDataContainer.adapterContext
        if (data == null) {
            itemMeta.persistentDataContainer.set(namespacedKeyGlobal, PersistentDataType.TAG_CONTAINER, dataContext.newPersistentDataContainer())
            data = itemMeta.persistentDataContainer.get(namespacedKeyGlobal, PersistentDataType.TAG_CONTAINER)
        }
        return data
    }

    fun hasMainData(itemMeta: ItemMeta?): Boolean{
        val namespacedKeyGlobal = NamespacedKey(instance, "mainItemData")
        return itemMeta!!.persistentDataContainer.get(namespacedKeyGlobal, PersistentDataType.TAG_CONTAINER) != null
    }

    private fun setMainData(itemMeta: ItemMeta, ctn: PersistentDataContainer): ItemMeta {
        val namespacedKeyGlobal = NamespacedKey(instance, "mainItemData")
        itemMeta.persistentDataContainer.set(namespacedKeyGlobal, PersistentDataType.TAG_CONTAINER, ctn)
        return itemMeta
    }

    @JvmStatic
    operator fun <T : Any> set(item: ItemStack, key: String?, type: PersistentDataType<T, T>, value: T): ItemStack {
        val namespacedKey = NamespacedKey(instance, key!!)
        var itemMeta = item.itemMeta
        val itemData = getMainData(itemMeta)
        if (itemData == null) {
            Bukkit.getLogger().warning("Cannot get item global data")
            return item
        }
        itemData.set(namespacedKey, type, value)
        itemMeta = setMainData(itemMeta!!, itemData)
        item.itemMeta = itemMeta
        return item
    }

    @JvmStatic
    operator fun <T> get(item: ItemStack, key: String?, type: PersistentDataType<T, T>): T? {
        val namespacedKey = NamespacedKey(instance, key!!)
        val itemMeta = item.itemMeta ?: return null
        val itemData = getMainData(itemMeta)
                ?: //Bukkit.getLogger().warning("Cannot get item global data");
                return null
        return itemData.get(namespacedKey, type)
    }

    @JvmStatic
    fun remove(item: ItemStack, key: String?): Boolean {
        val namespacedKey = NamespacedKey(instance, key!!)
        var itemMeta = item.itemMeta ?: return false
        val itemData = getMainData(itemMeta)
        if (itemData == null) {
            Bukkit.getLogger().warning("Cannot get item global data")
            return false
        }
        itemData.remove(namespacedKey)
        itemMeta = setMainData(itemMeta, itemData)
        item.itemMeta = itemMeta
        return true
    }
}