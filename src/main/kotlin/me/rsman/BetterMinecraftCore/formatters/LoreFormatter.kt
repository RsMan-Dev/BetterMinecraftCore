package me.rsman.BetterMinecraftCore.formatters

import me.rsman.BetterMinecraftCore.utils.NBT.get
import me.rsman.BetterMinecraftCore.utils.RomanNumber.toRoman
import me.rsman.BetterMinecraftCore.enums.EEnchants
import me.rsman.BetterMinecraftCore.enums.EAttributes
import me.rsman.BetterMinecraftCore.configs.containers.GlobalConfigContainer
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import me.rsman.BetterMinecraftCore.Managers.ItemManager
import me.rsman.BetterMinecraftCore.configs.containers.AttributeLangContainer
import java.lang.StringBuilder
import me.rsman.BetterMinecraftCore.configs.containers.EnchantLangContainer
import java.util.*

object LoreFormatter {
    fun format(item: ItemStack): List<String> {
        var lore: MutableList<String> = ArrayList(formatAttributesLore(item))
        lore.addAll(formatEnchantsLore(item))

        //custom lore
        var customLore = get(item, "lore", PersistentDataType.STRING)
        if (customLore != null && customLore != "") {
            lore.add("")
            customLore = customLore.replace("&", "§")
            lore.addAll(listOf(*customLore.split("\\|".toRegex()).toTypedArray()))
        }

        //unbreakable lore
        if (item.hasItemMeta() && Objects.requireNonNull(item.itemMeta)!!.isUnbreakable) {
            lore.add("")
            lore.add("§cUnbreakable")
        }
        lore = lore.map { loreEl -> PapiManager.parseText(null, loreEl) }.toMutableList()
        return lore
    }

    fun formatAttributesLore(item: ItemStack): List<String> {
        val attributesLore: MutableList<String> = ArrayList()
        for (attr in EAttributes.allKeys) {
            val attrVal = ItemManager.getItemAttr(item, attr)
            val attrModifierVal = ItemManager.getItemEnchantAttr(item, attr)
            if (attrVal != 0L || attrModifierVal != 0L) {
                val percent: Boolean = EAttributes.allPercentKeys.contains(attr)
                var attrTrans = AttributeLangContainer.instance!!.getTranslation(attr)
                if (attrTrans == null) attrTrans = attr.substring(0, 1).uppercase() + attr.substring(1)
                attributesLore.add(
                        GlobalConfigContainer.instance!!.attribute_display_format!!.replace("&", "§")
                                .replace("{attr_name}", attrTrans.replace("&", "§"))
                                .replace("{value}", attrVal.toString() + if (percent) "%" else "") +
                                if (attrModifierVal > 0) GlobalConfigContainer.instance!!.attribute_modifier_display_format!!.replace("&", "§")
                                        .replace("{value}", attrModifierVal.toString() + if (percent) "%" else "") else ""
                )
            }
        }
        return attributesLore
    }

    fun formatEnchantsLore(item: ItemStack): List<String> {
        val enchantsLore: MutableList<String> = ArrayList()
        var first = true
        var line = StringBuilder("§7")
        for ((key, value) in item.enchantments) {
            if (value <= 0) continue
            if (first) {
                first = false
                enchantsLore.add("")
            }
            var enchantTrans = EnchantLangContainer.instance!!.getTranslation(
                    EEnchants.getEnumKeyFromKey(
                            key.key.toString().replaceFirst("minecraft:".toRegex(), "")
                    )
            )
            if (enchantTrans == null) enchantTrans = key.key.toString()
            val enchantName = GlobalConfigContainer.instance!!.enchant_display_format!!.replace("&", "§")
                    .replace("{ench_name}", enchantTrans.replace("&", "§"))
                    .replace("{value}", toRoman(value)!!)
                    .replace("{value_int}", value.toString())
            if (line.length < 30) {
                if (line.toString() != "§7") line.append(GlobalConfigContainer.instance!!.enchant_separator_display_format!!.replace("&", "§")).append(" ")
                line.append(enchantName)
            } else {
                enchantsLore.add(line.toString() + GlobalConfigContainer.instance!!.enchant_separator_display_format!!.replace("&", "§"))
                line = StringBuilder("§7$enchantName")
            }
        }
        if (line.toString() != "§7") {
            enchantsLore.add(line.toString())
        }
        return enchantsLore
    }
}