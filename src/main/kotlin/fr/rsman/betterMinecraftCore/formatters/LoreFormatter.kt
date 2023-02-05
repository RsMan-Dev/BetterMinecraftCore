package fr.rsman.betterMinecraftCore.formatters

import fr.rsman.betterMinecraftCore.enums.EEnchants
import fr.rsman.betterMinecraftCore.enums.EAttributes
import fr.rsman.betterMinecraftCore.configs.containers.GlobalConfigContainer
import org.bukkit.inventory.ItemStack
import fr.rsman.betterMinecraftCore.configs.containers.AttributeLangContainer
import java.lang.StringBuilder
import fr.rsman.betterMinecraftCore.configs.containers.EnchantLangContainer
import fr.rsman.betterMinecraftCore.extensions.customLore
import fr.rsman.betterMinecraftCore.extensions.getEnchAtribute
import fr.rsman.betterMinecraftCore.extensions.getStoredAtribute
import fr.rsman.betterMinecraftCore.extensions.isUnbreakable
import fr.rsman.betterMinecraftCore.managers.PapiManager
import fr.rsman.betterMinecraftCore.utils.romanString
import java.util.*

object LoreFormatter {
    fun format(item: ItemStack): List<String> {
        val lore = formatAttributesLore(item).toMutableList()
        lore.addAll(formatEnchantsLore(item))

        //custom lore
        lore.addAll(item.customLore?.map { it.replace("&", "§") } ?: listOf())

        //unbreakable lore
        if (item.isUnbreakable) {
            lore.add("")
            lore.add("§cUnbreakable")
        }
        return lore.map { PapiManager.parseText(null, it) }.toMutableList()
    }

    private fun formatAttributesLore(item: ItemStack): List<String> {
        val attributesLore: MutableList<String> = ArrayList()
        for (attr in EAttributes.values()) {
            val attrVal = item.getStoredAtribute(attr)
            val attrModifierVal = item.getEnchAtribute(attr)
            if (attrVal != 0L || attrModifierVal != 0L) {
                val attrTrans = AttributeLangContainer.instance!!.translations?.get(attr.key) ?: attr.key.replaceFirstChar { it.uppercase() }
                attributesLore.add(
                        GlobalConfigContainer.instance?.attribute_display_format?.replace("&", "§")
                                ?.replace("{attr_name}", attrTrans.replace("&", "§"))
                                ?.replace("{value}", attrVal.toString() + if (attr.isPercent) "%" else "") +
                                if (attrModifierVal > 0) GlobalConfigContainer.instance?.attribute_modifier_display_format?.replace("&", "§")
                                        ?.replace("{value}", attrModifierVal.toString() + if (attr.isPercent) "%" else "") else ""
                )
            }
        }
        return attributesLore
    }

    private fun formatEnchantsLore(item: ItemStack): List<String> {
        val enchantsLore: MutableList<String> = ArrayList()
        var first = true
        var line = StringBuilder("§7")
        for ((key, value) in item.enchantments) {
            if (value <= 0) continue
            if (first) {
                first = false
                enchantsLore.add("")
            }
            val enchantTrans = EnchantLangContainer.instance?.translations?.get(
                    EEnchants.fromKey(
                            key.key.toString().replaceFirst("minecraft:", "")
                    )?.key?.uppercase()
            ) ?: key.key.toString()
            val enchantName = GlobalConfigContainer.instance!!.enchant_display_format!!.replace("&", "§")
                    .replace("{ench_name}", enchantTrans.replace("&", "§"))
                    .replace("{value}", value.romanString!!)
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