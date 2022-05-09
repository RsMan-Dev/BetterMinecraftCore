package me.rsman.BetterMinecraftCore.configs.models

import java.util.HashMap
import me.rsman.BetterMinecraftCore.enums.EAttributes
import me.rsman.BetterMinecraftCore.enums.EEnchants
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import me.rsman.BetterMinecraftCore.Managers.ItemManager

class BmcItem {
    private var name: String? = null
    private var displayName: String? = null
    private var materialId: Int? = null
    private var material: String? = null
    private var lore: List<String>? = null
    private var attributes: HashMap<String, Long>? = null
    private var enchants: HashMap<String, Int>? = null
    private var unbreakable: Boolean? = null
    private var renamable: Boolean? = null
    private var rev = 0
    private var dropsFromBlock: List<String>? = null
    private var dropsFromEntity: List<String>? = null

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getDisplayName(): String? {
        return displayName
    }

    fun setDisplayName(displayName: String?) {
        this.displayName = displayName
    }

    fun getMaterialId(): Int? {
        return materialId
    }

    fun setMaterialId(materialId: Int?) {
        this.materialId = materialId
    }

    fun getMaterial(): String? {
        return material
    }

    fun setMaterial(material: String?) {
        this.material = material
    }

    fun getLore(): List<String>? {
        return lore
    }

    fun setLore(lore: List<String>?) {
        this.lore = lore
    }

    fun getAttributes(): HashMap<String, Long>? {
        return attributes
    }

    fun setAttributes(attributes: HashMap<String, Long>?) {
        if (attributes != null) {
            for ((key) in attributes) {
                if (!EAttributes.allKeys.contains(key)) {
                    attributes.remove(key)
                }
            }
        }
        this.attributes = attributes
    }

    fun getEnchants(): HashMap<String, Int>? {
        return enchants
    }

    fun setEnchants(enchants: HashMap<String, Int>?) {
        if (enchants != null) {
            for ((key) in enchants) {
                if (!EEnchants.enumKeys.contains(key)) {
                    enchants.remove(key)
                }
            }
        }
        this.enchants = enchants
    }

    fun isUnbreakable(): Boolean? {
        return unbreakable
    }

    fun setUnbreakable(unbreakable: Boolean?) {
        this.unbreakable = unbreakable
    }

    fun isRenamable(): Boolean? {
        return renamable
    }

    fun setRenamable(renamable: Boolean?) {
        this.renamable = renamable
    }

    fun getRev(): Int {
        return rev
    }

    fun setRev(rev: Int) {
        this.rev = rev
    }

    fun getDropsFromBlock(): List<String>? = dropsFromBlock

    fun setDropsFromBlock(dropsFromBlock: List<String>?){this.dropsFromBlock = dropsFromBlock}

    fun getDropsFromEntity(): List<String>? = dropsFromEntity

    fun setDropsFromEntity(dropsFromEntity: List<String>?){this.dropsFromEntity = dropsFromEntity}

    override fun toString(): String {
        return "BmcItem(name=$name, displayName=$displayName, materialId=$materialId, material=$material, lore=$lore, attributes=$attributes, enchants=$enchants, unbreakable=$unbreakable, renamable=$renamable, rev=$rev, iaDropsFromBlock=$dropsFromBlock, iaDropsFromEntity=$dropsFromEntity)"
    }


    fun cloneForConfig(): BmcItem {
        val i = BmcItem()
        i.setName(null)
        i.setDisplayName(displayName)
        i.setMaterialId(materialId)
        i.setAttributes(attributes)
        i.setMaterial(material)
        i.setLore(lore)
        i.setEnchants(enchants)
        i.setUnbreakable(unbreakable)
        i.setRenamable(renamable)
        i.setRev(rev)
        i.setDropsFromBlock(dropsFromBlock)
        i.setDropsFromEntity(dropsFromEntity)
        return i
    }

    val itemStack: ItemStack
        get() {
            val itemTR = ItemStack(Material.valueOf(material!!))
            if (name != null) ItemManager.setItemName(itemTR, name!!)
            if (lore != null && lore!!.isNotEmpty()) ItemManager.setCustomLoreAll(itemTR, lore!!)
            if (attributes != null) {
                for ((key, value) in attributes!!) {
                    ItemManager.setItemAttr(itemTR, key, value)
                }
            }
            if (enchants != null) {
                for ((key, value) in enchants!!) {
                    itemTR.addUnsafeEnchantment(EEnchants.valueOf(key).enchant, value)
                }
            }
            if (renamable != null) ItemManager.setRenamable(itemTR, renamable!!)
            if (renamable == null) ItemManager.setRenamable(itemTR, false)
            ItemManager.setItemRev(itemTR, rev)
            val itemTRMeta = itemTR.itemMeta
            if (itemTRMeta != null) {
                if (displayName != null) itemTRMeta.setDisplayName(displayName)
                if (materialId != null) itemTRMeta.setCustomModelData(materialId)
                if (unbreakable != null) itemTRMeta.isUnbreakable = unbreakable!!
            }
            itemTR.itemMeta = itemTRMeta
            ItemManager.updateItemLore(itemTR)
            return itemTR
        }

    companion object {
        fun parseItemStack(item: ItemStack): BmcItem {
            val im = item.itemMeta
            val itemTS = BmcItem()
            itemTS.material = item.type.name
            if (ItemManager.getItemName(item) != "") itemTS.name = ItemManager.getItemName(item)
            if (ItemManager.getCustomLoreAll(item).isNotEmpty()) itemTS.lore = ItemManager.getCustomLoreAll(item)
            val itemAttrs = HashMap<String, Long>()
            for (ea in EAttributes.values()) {
                if (ItemManager.hasItemAttr(item, ea.key)) itemAttrs[ea.key] = ItemManager.getItemAttr(item, ea.key)
            }
            itemTS.attributes = itemAttrs
            val itemEnchs = HashMap<String, Int>()
            for ((key, value) in item.enchantments) {
                itemEnchs[EEnchants.getEnumKeyFromKey(key.key.toString().replaceFirst("minecraft:", "")).toString()] = value
            }
            itemTS.enchants = itemEnchs
            itemTS.renamable = if (ItemManager.isRenamable(item)) true else null
            if (im != null) {
                if (im.hasDisplayName()) itemTS.displayName = im.displayName
                if (im.hasCustomModelData()) itemTS.materialId = im.customModelData
                itemTS.unbreakable = if (im.isUnbreakable) true else null
            }
            itemTS.rev = ItemManager.getItemRev(item)
            return itemTS
        }
    }
}