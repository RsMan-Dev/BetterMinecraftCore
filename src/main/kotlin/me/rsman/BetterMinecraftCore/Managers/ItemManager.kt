package me.rsman.BetterMinecraftCore.Managers

import dev.lone.itemsadder.api.ItemsAdder
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.utils.NBT.remove
import me.rsman.BetterMinecraftCore.utils.NBT.set
import me.rsman.BetterMinecraftCore.utils.NBT.get
import me.rsman.BetterMinecraftCore.enums.EEnchants
import org.bukkit.NamespacedKey
import me.rsman.BetterMinecraftCore.enums.EAttributes
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import me.rsman.BetterMinecraftCore.Enchantments.CustomEnchantClass
import me.rsman.BetterMinecraftCore.formatters.LoreFormatter
import me.rsman.BetterMinecraftCore.interfaces.CoreSourceFrom
import me.rsman.BetterMinecraftCore.interfaces.ItemDropPattern
import me.rsman.BetterMinecraftCore.utils.NBT.hasMainData
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import java.util.ArrayList

object ItemManager {
    fun isCustom(item: ItemStack?) : Boolean {
        return hasMainData(item?.itemMeta)
    }
    fun setItemName(item: ItemStack?, value: String) {
        if (value == "") {
            remove(item!!, "name")
        } else {
            set(item!!, "name", PersistentDataType.STRING, value)
        }
    }

    fun getItemName(item: ItemStack?): String {
        return if(item != null) get(item, "name", PersistentDataType.STRING) ?: "" else return ""
    }

    fun setItemRev(item: ItemStack?, value: Int) {
        set(item!!, "rev", PersistentDataType.INTEGER, value)
    }

    fun getItemRev(item: ItemStack?): Int {
        val rev = get(item!!, "rev", PersistentDataType.INTEGER)
        if (rev == null) {
            setItemRev(item, 1)
            return 1
        }
        return rev
    }

    fun setUnbreakable(item: ItemStack, `val`: Boolean) {
        val meta = item.itemMeta ?: return
        meta.isUnbreakable = `val`
        item.itemMeta = meta
        updateItemLore(item)
    }

    fun setRenamable(item: ItemStack?, `val`: Boolean) {
        set(item!!, "renamable", PersistentDataType.BYTE, (if (`val`) 1 else 0).toByte())
    }

    fun isRenamable(item: ItemStack?): Boolean {
        val renamable = get(item!!, "renamable", PersistentDataType.BYTE)
        return renamable == null || renamable.toInt() == 1
    }

    fun setItemAttr(item: ItemStack, attr: String, value: Long) {
        set(item, "attributes/$attr", PersistentDataType.LONG, value)
        updateItemLore(item)
    }

    fun getItemAttr(item: ItemStack?, attr: String): Long {
        return get(item!!, "attributes/$attr", PersistentDataType.LONG) ?: 0L
    }

    fun getItemEnchantAttr(item: ItemStack, attr: String?): Long {
        var value: Long = 0
        for ((key, value1) in item.enchantments) {
            val enchKey = key.key.toString()
            if (enchKey.startsWith(NamespacedKey.minecraft("bmc_").toString())) {
                val ench = EEnchants.valueOf(EEnchants.getEnumKeyFromKey(enchKey.replace("minecraft:", ""))!!)
                if (ench.enchant is CustomEnchantClass && ench.enchant.hasAttributesModifiers()) {
                    val modifiers = ench.enchant.attributeModifiers
                    if (modifiers.containsKey(attr)) {
                        value += modifiers[attr]!! * value1
                    }
                }
            }
        }
        return value
    }

    fun getFinalItemAttr(item: ItemStack, attr: String): Long {
        return getItemAttr(item, attr) + getItemEnchantAttr(item, attr)
    }

    fun hasItemAttr(item: ItemStack?, attr: String): Boolean {
        return get(item!!, "attributes/$attr", PersistentDataType.LONG) != null
    }

    fun setCustomLore(item: ItemStack?, text: String, line: Int?) {
        val lore = get(item!!, "lore", PersistentDataType.STRING)
        val loreArr: MutableList<String> = ArrayList()
        if (lore != null) {
            if (lore != "") loreArr.addAll(listOf(*lore.split("\\|".toRegex()).toTypedArray()))
            if (text == "null") {
                if (line != null && loreArr.size - 1 <= line) {
                    loreArr.removeAt(line - 1)
                }
            } else {
                if (line != null) {
                    for (i in loreArr.size until line) {
                        loreArr.add("")
                    }
                    loreArr[line - 1] = text
                } else {
                    loreArr.add(text)
                }
            }
        } else {
            if (line != null) {
                for (i in 1 until line) {
                    loreArr.add("")
                }
            }
            loreArr.add(text)
        }
        set(item, "lore", PersistentDataType.STRING, java.lang.String.join("|", loreArr))
    }

    fun setCustomLoreAll(item: ItemStack?, texts: List<String>, replaceAll: Boolean) {
        if (replaceAll) {
            set(item!!, "lore", PersistentDataType.STRING, "")
        }
        setCustomLoreAll(item, texts)
    }

    fun setCustomLoreAll(item: ItemStack?, texts: List<String>) {
        for (text in texts) {
            setCustomLore(item, text, null)
        }
    }

    fun getCustomLoreAll(item: ItemStack?): List<String> {
        val lore = get(item!!, "lore", PersistentDataType.STRING)
                ?: return ArrayList()
        return listOf(*lore.split("\\|".toRegex()).toTypedArray())
    }

    fun updateItemLore(item: ItemStack) {
        val itemMeta = item.itemMeta!!
        itemMeta.lore = LoreFormatter.format(item)
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
        if (itemMeta.attributeModifiers != null) {
            itemMeta.attributeModifiers!!.clear()
        }
        item.itemMeta = itemMeta
    }

    fun updateItem(item: ItemStack) {
        if (!BmcItemContainer.instance!!.items!!.containsKey(getItemName(item))) {
            if (getItemName(item) != "") {
                setItemName(item, "")
                setItemRev(item, 0)
            }
            return
        }
        val itemInConf = BmcItemContainer.instance!!.items!![getItemName(item)]!!.itemStack
        if (getItemRev(item) >= getItemRev(itemInConf)) return
        item.type = itemInConf.type
        setItemRev(item, getItemRev(itemInConf))
        item.data = itemInConf.data
        val meta = item.itemMeta
        val metaConf = itemInConf.itemMeta
        assert(meta != null)
        assert(metaConf != null)
        meta!!.setDisplayName(metaConf!!.displayName)
        meta.isUnbreakable = metaConf.isUnbreakable
        item.itemMeta = meta
        for (attr in EAttributes.allKeys) {
            if (hasItemAttr(itemInConf, attr)) {
                setItemAttr(item, attr, getItemAttr(itemInConf, attr))
            }
        }
        for ((key, value) in itemInConf.enchantments) {
            item.addUnsafeEnchantment(key!!, value!!)
        }
        setCustomLoreAll(item, getCustomLoreAll(itemInConf), true)
        updateItemLore(item)
    }

    fun getDrops(item: ItemStack?, source: String): List<String>?{
        if(!listOf("block", "entity").contains(source)) return null
        if(item == null) return null
        return get(item, "drops_from_$source", PersistentDataType.STRING)?.split(",")
    }

    fun addDrop(item: ItemStack?, pattern: String, source: String){
        if(!listOf("block", "entity").contains(source)) return
        if(item == null || pattern.contains(",")) return
        val drops = getDrops(item, source)?.toMutableList() ?: return
        val parsed = ItemDropPattern.parsePattern(pattern) ?: return
        if(
                when(parsed.sourceFrom){
                    CoreSourceFrom.Vanilla -> Material.values().map { it.name }.contains(parsed.id)
                    CoreSourceFrom.ItemsAdder -> BetterMinecraftCore.isItemsAdderInstalled && ItemsAdder.getAllItems().map { it.id }.contains(parsed.id)
                    CoreSourceFrom.MythicMobs -> Material.values().map { it.name }.contains(parsed.id)
                }
        ){
            drops += pattern
        } else {
            return
        }
        set(item, "drops_from_$source", PersistentDataType.STRING, drops.joinToString(","))
    }

    fun removeDrop(item: ItemStack?, pattern: String, source: String){
        if(!listOf("block", "entity").contains(source)) return
        if(item == null || pattern.contains(",")) return
        val drops = getDrops(item, source)?.toMutableList() ?: return
        for (drop in drops){
            if(drop.startsWith(pattern)) drops.remove(drop)
        }
        set(item, "drops_from_$source", PersistentDataType.STRING, drops.joinToString(","))
    }

    fun removeAllDrops(item: ItemStack?, source: String){
        if(item == null) return
        remove(item, "drops_from_$source")
    }
}