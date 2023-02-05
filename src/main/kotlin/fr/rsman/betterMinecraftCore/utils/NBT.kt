package fr.rsman.betterMinecraftCore.utils

import fr.rsman.betterMinecraftCore.BetterMinecraftCore.Companion.instance
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import org.bukkit.inventory.ItemStack
import org.bukkit.Bukkit



@Suppress("BooleanMethodIsAlwaysInverted")
val ItemStack.isCustom : Boolean
    get() = hasMainData(this.itemMeta)

inline fun <reified T: Any> ItemStack.setNbt(name: String, value: T)
{
    when (T::class) {
        String::class -> set(this, name, PersistentDataType.STRING, value as String)

        Boolean::class -> set(this, name, PersistentDataType.BYTE, if (value as Boolean) 1.toByte() else 0.toByte())

        Byte::class -> set(this, name, PersistentDataType.BYTE, value as Byte)
        Short::class -> set(this, name, PersistentDataType.SHORT, value as Short)
        Int::class -> set(this, name, PersistentDataType.INTEGER, value as Int)
        Long::class -> set(this, name, PersistentDataType.LONG, value as Long)
        Float::class -> set(this, name, PersistentDataType.FLOAT, value as Float)
        Double::class -> set(this, name, PersistentDataType.DOUBLE, value as Double)

        PersistentDataContainer::class -> set(this, name, PersistentDataType.TAG_CONTAINER, value as PersistentDataContainer)
    }
}

inline fun <reified T: Any> ItemStack.setNbtList(name: String, value: List<T>)
{
    when (T::class) {
        Byte::class -> set(this, name, PersistentDataType.BYTE_ARRAY, value.filterIsInstance<Byte>().toByteArray())
        Int::class -> set(this, name, PersistentDataType.INTEGER_ARRAY, value.filterIsInstance<Int>().toIntArray())
        Long::class -> set(this, name, PersistentDataType.LONG_ARRAY, value.filterIsInstance<Long>().toLongArray())

        PersistentDataContainer::class -> set(this, name, PersistentDataType.TAG_CONTAINER_ARRAY, value.filterIsInstance<PersistentDataContainer>().toTypedArray())
    }
}

inline fun <reified T: Any> ItemStack.getNbt(name: String) : T?
{
    return when (T::class) {
        String::class -> get(this, name, PersistentDataType.STRING) as T?

        Boolean::class -> (get(this, name, PersistentDataType.BYTE) == 1.toByte()) as T?

        Byte::class -> get(this, name, PersistentDataType.BYTE) as T?
        Short::class -> get(this, name, PersistentDataType.SHORT) as T?
        Int::class -> get(this, name, PersistentDataType.INTEGER) as T?
        Long::class -> get(this, name, PersistentDataType.LONG) as T?
        Float::class -> get(this, name, PersistentDataType.FLOAT) as T?
        Double::class -> get(this, name, PersistentDataType.DOUBLE) as T?

        PersistentDataContainer::class -> get(this, name, PersistentDataType.TAG_CONTAINER) as T?

        else -> null
    }
}

inline fun <reified T: Any> ItemStack.getNbtList(name: String) : List<T>?
{
    return when (T::class) {
        Byte::class -> get(this, name, PersistentDataType.BYTE_ARRAY)?.toList()?.filterIsInstance<T>()
        Int::class -> get(this, name, PersistentDataType.INTEGER_ARRAY)?.toList()?.filterIsInstance<T>()
        Long::class -> get(this, name, PersistentDataType.LONG_ARRAY)?.toList()?.filterIsInstance<T>()

        PersistentDataContainer::class -> get(this, name, PersistentDataType.TAG_CONTAINER_ARRAY)?.toList()?.filterIsInstance<T>()

        else -> null
    }
}

@Suppress("BooleanMethodIsAlwaysInverted")
fun ItemStack.removeNbt(name: String): Boolean {
    return remove(this, name)
}

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

fun <T : Any> set(item: ItemStack, key: String?, type: PersistentDataType<T, T>, value: T): ItemStack {
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

fun <T> get(item: ItemStack, key: String?, type: PersistentDataType<T, T>): T? {
    val namespacedKey = NamespacedKey(instance, key!!)
    val itemMeta = item.itemMeta ?: return null
    val itemData = getMainData(itemMeta)
            ?: //Bukkit.getLogger().warning("Cannot get item global data");
            return null
    return itemData.get(namespacedKey, type)
}

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