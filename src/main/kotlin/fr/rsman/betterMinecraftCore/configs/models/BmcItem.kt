package fr.rsman.betterMinecraftCore.configs.models

import fr.rsman.betterMinecraftCore.enums.EAttributes
import fr.rsman.betterMinecraftCore.enums.EEnchants
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import fr.rsman.betterMinecraftCore.enums.EDropSourceType
import fr.rsman.betterMinecraftCore.extensions.*

data class BmcItem(
    var name: String? = null,
    var displayName: String? = null,
    var materialId: Int? = null,
    var material: String? = null,
    var lore: List<String>? = null,
    var attributes: MutableMap<String, Long>? = null,
    var enchants: MutableMap<String, Int>? = null,
    var unbreakable: Boolean? = null,
    var renamable: Boolean? = null,
    var rev: Int = 0,
    var dropsFromBlock: List<String>? = null,
    var dropsFromEntity: List<String>? = null
){

    fun cloneForConfig(): BmcItem = this.copy(
        name=null,
        attributes = attributes?.filterKeys { EAttributes.keys.contains(it) }?.toMutableMap(),
        enchants = enchants?.filterKeys { EEnchants.keys.contains(it) }?.toMutableMap(),
    )


    val itemStack: ItemStack
        get() {
            val self = this
            return ItemStack(Material.valueOf(material!!)).apply {
                saveName = self.name
                customLore = self.lore
                if (self.attributes != null) {
                    for ((key, value) in self.attributes!!) {
                        setStoredAtribute(EAttributes.fromKey(key) ?: continue, value)
                    }
                }
                if (self.enchants != null) {
                    for ((key, value) in self.enchants!!) {
                        addUnsafeEnchantment(EEnchants.fromKey(key)?.enchant ?: continue, value)
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