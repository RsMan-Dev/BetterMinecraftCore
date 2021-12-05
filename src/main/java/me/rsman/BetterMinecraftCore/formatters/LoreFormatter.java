package me.rsman.BetterMinecraftCore.formatters;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Managers.EnchantManager;
import me.rsman.BetterMinecraftCore.Managers.ItemManager;
import me.rsman.BetterMinecraftCore.configs.containers.AttributeLangContainer;
import me.rsman.BetterMinecraftCore.configs.containers.EnchantLangContainer;
import me.rsman.BetterMinecraftCore.configs.containers.GlobalConfigContainer;
import me.rsman.BetterMinecraftCore.enums.EAttributes;
import me.rsman.BetterMinecraftCore.enums.EEnchants;
import me.rsman.BetterMinecraftCore.utils.NBT;
import me.rsman.BetterMinecraftCore.utils.RomanNumber;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public final class LoreFormatter {
    public static List<String> format(ItemStack item){

        List<String> lore = new ArrayList<>(formatAttributesLore(item));

        lore.addAll(formatEnchantsLore(item));

        //custom lore
        String CustomLore = (String) NBT.get(item, "lore", PersistentDataType.STRING);
        if(CustomLore != null && !CustomLore.equals("")) {
            lore.add("");
            CustomLore = CustomLore.replace("&", "§");
            lore.addAll(Arrays.asList(CustomLore.split("\\|")));
        }

        //unbreakable lore
        if(item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).isUnbreakable()){
            lore.add("");
            lore.add("§cUnbreakable");
        }
        return lore;
    }

    public static List<String> formatAttributesLore(ItemStack item){
        List<String> attributesLore = new ArrayList<>();
        for (String attr : EAttributes.getAllKeys()) {
            long attrVal = ItemManager.getItemAttr(item, attr);
            long attrModifierVal = ItemManager.getItemEnchantAttr(item, attr);
            if(attrVal != 0 || attrModifierVal != 0){
                boolean percent = EAttributes.getAllPercentKeys().contains(attr);
                String attrTrans = AttributeLangContainer.getInstance().getTranslation(attr);
                if(attrTrans == null) attrTrans = attr.substring(0, 1).toUpperCase() + attr.substring(1);
                attributesLore.add(
                        GlobalConfigContainer.getInstance().getAttribute_display_format().replace("&","§")
                        .replace("{attr_name}", attrTrans.replace("&", "§"))
                        .replace("{value}", attrVal + (percent ? "%" : "")) +
                            (attrModifierVal >0 ?
                                GlobalConfigContainer.getInstance().getAttribute_modifier_display_format().replace("&","§")
                                    .replace("{value}", attrModifierVal + (percent ? "%" : ""))
                            : "")
                );
            }
        }
        return attributesLore;
    }

    public static List<String> formatEnchantsLore(ItemStack item){
        List<String> enchantsLore = new ArrayList<>();
        boolean first = true;
        StringBuilder line = new StringBuilder("§7");
        for (Map.Entry<Enchantment, Integer> enchant: item.getEnchantments().entrySet()) {
            if(enchant.getValue() <= 0)continue;
            if(first){ first = false; enchantsLore.add(""); }

            String enchantTrans = EnchantLangContainer.getInstance().getTranslation(
                    EEnchants.getEnumKeyFromKey(
                            enchant.getKey().getKey().toString().replaceFirst("minecraft:", "")
                    )
            );
            if(enchantTrans == null) enchantTrans = enchant.getKey().getKey().toString();
            String enchantName = GlobalConfigContainer.getInstance().getEnchant_display_format().replace("&","§")
                    .replace("{ench_name}", enchantTrans.replace("&", "§"))
                    .replace("{value}", RomanNumber.toRoman(enchant.getValue()))
                    .replace("{value_int}", enchant.getValue().toString());

            if(line.length() < 30){
                if(!line.toString().equals("§7")) line.append(GlobalConfigContainer.getInstance().getEnchant_separator_display_format().replace("&", "§")).append(" ");
                line.append(enchantName);
            } else {
                enchantsLore.add(line + GlobalConfigContainer.getInstance().getEnchant_separator_display_format().replace("&", "§"));
                line = new StringBuilder("§7" + enchantName);
            }
        }
        if(!line.toString().equals("§7")){
            enchantsLore.add(line.toString());
        }
        return enchantsLore;
    }

}
