package me.rsman.BetterMinecraftCore.extensions

import me.rsman.BetterMinecraftCore.Enchantments.CustomEnchantClass
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer
import me.rsman.BetterMinecraftCore.configs.models.BmcItem
import me.rsman.BetterMinecraftCore.enums.EAttributes
import me.rsman.BetterMinecraftCore.enums.EDropSourceType
import me.rsman.BetterMinecraftCore.enums.EEnchants
import me.rsman.BetterMinecraftCore.formatters.LoreFormatter
import me.rsman.BetterMinecraftCore.interfaces.ItemDropPattern
import me.rsman.BetterMinecraftCore.utils.getNbt
import me.rsman.BetterMinecraftCore.utils.removeNbt
import me.rsman.BetterMinecraftCore.utils.setNbt
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType


val ItemStack.bmcItem: BmcItem?
    get() = BmcItemContainer.instance?.items?.get(saveName)


var ItemStack.saveName: String?
    get() = this.getNbt("name")
    set(value) { if (value != null) this.setNbt("name", value) else this.removeNbt("name") }

var ItemStack.rev: Int
    get() = this.getNbt("rev") ?: 0
    set(value) { this.setNbt("rev", value) }

var ItemStack.isUnbreakable: Boolean
    get() = itemMeta?.isUnbreakable ?: false
    set(value) {itemMeta = itemMeta?.apply { isUnbreakable = value } }

var ItemStack.displayName: String?
    get() = itemMeta?.displayName
    set(value) {itemMeta = itemMeta?.apply { setDisplayName(value) } }

var ItemStack.materialId: Int?
    get() = itemMeta?.customModelData
    set(value) {itemMeta = itemMeta?.apply { setCustomModelData(value) } }

var ItemStack.isRenamable: Boolean
    get() = this.getNbt("renamable") ?: true
    set(value) = this.setNbt("renamable", value)


fun ItemStack.getAtribute(attr: EAttributes): Long = this.getStoredAtribute(attr) + this.getEnchAtribute(attr)

fun ItemStack.getStoredAtribute(attr: EAttributes): Long = this.getNbt("attributes/${attr.key}") ?: 0L
fun ItemStack.setStoredAtribute(attr: EAttributes, value: Long) {
    if (value == 0L) this.removeNbt("attributes/${attr.key}") else this.setNbt("attributes/${attr.key}", value)
}

fun ItemStack.getEnchAtribute(attr: EAttributes): Long = this.getNbt("ench_attributes/${attr.key}") ?: 0L
private fun ItemStack.setEnchAtribute(attr: EAttributes, value: Long) {
    if (value == 0L) this.removeNbt("ench_attributes/${attr.key}") else this.setNbt("ench_attributes/${attr.key}", value)
}

fun ItemStack.updateEnchantAttributes() {
    this.enchantments.entries.filter {it.key.key.toString().startsWith(NamespacedKey.minecraft("bmc_").toString())}
        .mapNotNull { EEnchants.fromKey(it.key.key.toString().replace("minecraft:", ""))?.enchant?.let { it2 -> Pair(it2, it.value) } }
        .forEach { if(it.first is CustomEnchantClass) (it.first as CustomEnchantClass).attributeModifiers.forEach { (k, v) -> this.setEnchAtribute(k, v * it.second)  } }
}

fun ItemStack.setCustomLoreLine(value: String, line: Int?) {
    var result = customLore?.toMutableList()
    if (result != null) {
        if(value == "null"){
            if (line != null && result.size - 1 <= line) {
                result.removeAt(line - 1)
            }
        } else {
            if (line != null) {
                for (i in result.size..line) {
                    result.add("")
                }
                result[line - 1] = value
            } else {
                result.add(value)
            }
        }
    } else {
        result = MutableList(line ?: 1) { "" }
    }

    customLore = result
}

var ItemStack.customLore: List<String>?
    get() = this.getNbt<String>("lore")?.split("|")
    set(value) {
        if (value == null) this.removeNbt("lore") else this.setNbt(
            "lore",
            "${this.getNbt<String>("lore")?.plus("|") ?: ""}${value.toTypedArray().joinToString(separator = "|")}"
        )
    }

fun ItemStack.updateCustomLore() {
    val self = this;
    itemMeta = itemMeta?.apply {
        lore = LoreFormatter.format(self)
        addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        addItemFlags(ItemFlag.HIDE_ENCHANTS)
        addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
        attributeModifiers?.clear()
    }
}

fun ItemStack.update() {
    val itemInConf = BmcItemContainer.instance?.items?.get(saveName)?.itemStack
    if (itemInConf == null) {
        saveName = null
        rev = 0
        return
    }
    if(itemInConf.rev == rev) return
    type = itemInConf.type
    rev = itemInConf.rev
    materialId = itemInConf.materialId
    displayName = itemInConf.displayName
    isUnbreakable = itemInConf.isUnbreakable
    isRenamable = itemInConf.isRenamable
    for (attr in EAttributes.values()) {
        setStoredAtribute(attr, itemInConf.getStoredAtribute(attr))
    }
    for ((key, value) in itemInConf.enchantments) {
        addUnsafeEnchantment(key, value)
    }
    customLore = itemInConf.customLore
    updateCustomLore()
}

fun ItemStack.getDropSources(type: EDropSourceType): List<String>? =
    getNbt<String>("drops_from_${type.key}")?.split(",")

fun ItemStack.addDropSource(pattern: String, type: EDropSourceType){
    if(pattern.contains(",")) return
    val drops = getDropSources(type)?.toMutableList() ?: mutableListOf()
    ItemDropPattern.parsePattern(pattern) ?: return
    drops += pattern
    setNbt("drops_from_${type.key}", drops.joinToString(","))
}

fun ItemStack.removeDropSource(pattern: String, type: EDropSourceType){
    if(pattern.contains(",")) return
    val drops = getDropSources(type)?.toMutableList() ?: mutableListOf()
    for (drop in drops){
        if(drop.split(" ")[0] == pattern.split(" ")[0]) drops.remove(drop)
    }
    setNbt("drops_from_${type.key}", drops.joinToString(","))
}

fun ItemStack.removeAllDropSources(type: EDropSourceType){
    removeNbt("drops_from_${type.key}")
}