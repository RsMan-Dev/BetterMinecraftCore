package me.rsman.BetterMinecraftCore.configs.models

import java.util.HashMap
import me.rsman.BetterMinecraftCore.enums.EAttributes
import me.rsman.BetterMinecraftCore.enums.EEnchants
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import me.rsman.BetterMinecraftCore.enums.EDropSourceType
import me.rsman.BetterMinecraftCore.extensions.*

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
                if (!EAttributes.keys.contains(key)) {
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
                if (!EEnchants.keys.contains(key)) {
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
            val self = this;
            return ItemStack(Material.valueOf(material!!)).apply {
                saveName = self.name
                customLore = self.lore
                if (self.attributes != null) {
                    for ((key, value) in self.attributes!!) {
                        val ea = EAttributes.fromKey(key) ?: continue
                        setStoredAtribute(ea, value)
                    }
                }
                if (self.enchants != null) {
                    for ((key, value) in self.enchants!!) {
                        addUnsafeEnchantment(EEnchants.valueOf(key).enchant, value)
                    }
                }
                isRenamable = self.renamable ?: false
                rev = self.rev
                displayName = self.displayName
                materialId = self.materialId
                isUnbreakable = self.unbreakable ?: false

                updateEnchantAttributes()
                updateCustomLore()
                self.dropsFromBlock?.forEach { addDropSource(it, EDropSourceType.BLOCK) }
                self.dropsFromEntity?.forEach { addDropSource(it, EDropSourceType.ENTITY) }
            }

        }

    companion object {
        fun parseItemStack(item: ItemStack): BmcItem {
            return BmcItem().apply {
                material = item.type.name
                name = item.saveName
                lore = item.customLore
                attributes = hashMapOf<String, Long>().apply { putAll( EAttributes.values().map { Pair(it.key, item.getStoredAtribute(it)) } ) }
                enchants = hashMapOf<String, Int>().apply {
                    putAll( item.enchantments
                        .filter { EEnchants.fromKey(it.key.key.toString().replaceFirst("minecraft:", "")) != null }
                        .map { Pair(EEnchants.fromKey(it.key.key.toString().replaceFirst("minecraft:", ""))!!.key, it.value) }
                    )
                }
                renamable = item.isRenamable
                displayName = item.displayName
                materialId = item.materialId
                unbreakable = item.isUnbreakable
                rev = item.rev
                dropsFromBlock = item.getDropSources(EDropSourceType.BLOCK)
                dropsFromEntity = item.getDropSources(EDropSourceType.ENTITY)
            }
        }
    }
}