package me.rsman.BetterMinecraftCore.configs.models;

import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer;
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BmcCraftSubContainer {
    private Map<String, BmcShapedCraft> shaped;
    private Map<String, BmcShapelessCraft> shapeless;

    public BmcCraftSubContainer() {}

    public Map<String, BmcShapedCraft> getShaped() {
        return shaped;
    }

    public void setShaped(Map<String, BmcShapedCraft> shaped) {
        this.shaped = shaped;
    }

    public Map<String, BmcShapelessCraft> getShapeless() {
        return shapeless;
    }

    public void setShapeless(Map<String, BmcShapelessCraft> shapeless) {
        this.shapeless = shapeless;
    }
    public static ItemStack convertItemSchemeToItemStack(String item){
        if(item.equals("m.AIR") || item.equals("AIR") || item.equals("null")){
            return null;
        } else if(item.startsWith("m.")){
            if(Material.matchMaterial(item.substring(2)) == null) return null;
            return new ItemStack(Objects.requireNonNull(Material.matchMaterial(item.substring(2))));
        } else {
            if(BmcItemContainer.getInstance().getItems().containsKey(item)){
                return BmcItemContainer.getInstance().getItems().get(item).getItemStack();
            } else {
                return null;
            }
        }
    }

    public static Map.Entry<Character, RecipeChoice> convertSchemeToRecipeChoice(String scheme, Character character) {
        Map.Entry<Character, RecipeChoice> out;
        String[] splitted = scheme.trim().split(" ");
        String item = splitted[0];
        int number = splitted.length == 1 ? 1 : Integer.parseInt(splitted[1]);
        if(item.equals("m.AIR") || item.equals("AIR") || item.equals("null")){
            out = new AbstractMap.SimpleEntry<>(' ', null);
        } else if(item.startsWith("m.")){
            if(Material.matchMaterial(item.substring(2)) == null) return null;
            out = new AbstractMap.SimpleEntry<>(character, new RecipeChoice.ExactChoice(new ItemStack(Objects.requireNonNull(Material.matchMaterial(item.substring(2))), number)));
        }  else if(item.startsWith("all.")){
            boolean added = false;
            Tag<Material> tag =  Bukkit.getTag("blocks", NamespacedKey.minecraft(item.substring(4).toLowerCase()), Material.class);
            assert tag != null;
            if(tag.getValues().toArray().length == 0){
                tag =  Bukkit.getTag("items", NamespacedKey.minecraft(item.substring(4).toLowerCase()), Material.class);
            }
            assert tag != null;
            if(tag.getValues().toArray().length == 0){
                tag =  Bukkit.getTag("fluids", NamespacedKey.minecraft(item.substring(4).toLowerCase()), Material.class);
            }
            assert tag != null;
            out = new AbstractMap.SimpleEntry<>(character, new RecipeChoice.MaterialChoice(tag));
        } else {
            if(BmcItemContainer.getInstance().getItems().containsKey(item)){
                ItemStack temp = BmcItemContainer.getInstance().getItems().get(item).getItemStack();
                temp.setAmount(number);
                out = new AbstractMap.SimpleEntry<>(character, new RecipeChoice.ExactChoice(temp));
            } else {
                return null;
            }
        }
        return out;
    }

    public BmcCraftSubContainer cloneForConfig() {
        BmcCraftSubContainer csc = new BmcCraftSubContainer();
        if (shaped != null) {
            HashMap<String, BmcShapedCraft> shapedClone = new HashMap<>();
            for (Map.Entry<String, BmcShapedCraft> shapedToClone: shaped.entrySet()) {
                shapedClone.put(shapedToClone.getKey(), shapedToClone.getValue().cloneForConfig());
            }
            csc.setShaped(shapedClone);
        }
        if (shapeless != null) {
            HashMap<String, BmcShapelessCraft> shapelessClone = new HashMap<>();
            for (Map.Entry<String, BmcShapelessCraft> shapelessToClone: shapeless.entrySet()) {
                shapelessClone.put(shapelessToClone.getKey(), shapelessToClone.getValue().cloneForConfig());
            }
            csc.setShapeless(shapelessClone);
        }
        return csc;
    }
}
