package me.rsman.BetterMinecraftCore.Managers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CraftManager {
    public static Map<String, Map<String, List<String>>> registeredCrafts = new HashMap<>();

    public static void initCrafts(){
        BetterMinecraftCore.getInstance().getServer().resetRecipes();
        ConfigManager.getConfig("crafts/all", true);
        Set<String> recipes = ConfigManager.getKeys("crafts/all", "recipes");
        recipes.forEach(name->{
            Set<String> types = ConfigManager.getKeys("crafts/all", "recipes."+name);
            registeredCrafts.put(name, new HashMap<>());
            if(types.contains("shaped")){
                Set<String> crafts = ConfigManager.getKeys("crafts/all", "recipes."+name+".shaped");
                List<String> shapedCrafts = new ArrayList<>();
                crafts.forEach(c->{
                    List<String> itemsShapes = (List<String>) ConfigManager.getKey("crafts/all", "recipes."+name+".shaped."+c+".shape", "StringList", new ArrayList<>());
                    String result = (String) ConfigManager.getKey("crafts/all", "recipes."+name+".shaped."+c+".result", "String", "null");
                    String[] shape = {"","",""};
                    Map<Character, RecipeChoice> ingredients = new HashMap<>();

                    assert result != null;
                    Map.Entry<Character, RecipeChoice> resultItemFromScheme = convertSchemeToItem(result, 'X');
                    if(resultItemFromScheme == null || resultItemFromScheme.getValue() == null){
                        BetterMinecraftCore.getInstance().getLogger().info("skipping shaped recipe "+name+" "+c+", result is invalid or not registered");
                        return;
                    }
                    ShapedRecipe rec;
                    if(resultItemFromScheme.getValue() instanceof RecipeChoice.ExactChoice){
                        rec = new ShapedRecipe(NamespacedKey.minecraft("bmc_shaped_"+name.toLowerCase()+"_"+c),((RecipeChoice.ExactChoice)resultItemFromScheme.getValue()).getItemStack());
                    } else if (resultItemFromScheme.getValue() instanceof RecipeChoice.MaterialChoice){
                        rec = new ShapedRecipe(NamespacedKey.minecraft("bmc_shaped_"+name.toLowerCase()+"_"+c),((RecipeChoice.MaterialChoice)resultItemFromScheme.getValue()).getItemStack());
                    } else {
                        rec = new ShapedRecipe(NamespacedKey.minecraft("bmc_shaped_"+name.toLowerCase()+"_"+c),((RecipeChoice.ExactChoice)resultItemFromScheme.getValue()).getItemStack());
                    }
                    assert itemsShapes != null;
                    for (int i = 0; i < itemsShapes.toArray().length; i++){
                        String[] itemSchemes = itemsShapes.get(i).split("\\|");
                        for (String itemScheme: itemSchemes) {
                            Map.Entry<Character, RecipeChoice> itemFromScheme = convertSchemeToItem(itemScheme, (char)(97+i));

                            if(itemFromScheme == null){
                                BetterMinecraftCore.getInstance().getLogger().info("skipping shaped recipe "+name+" "+c+", one ingredient is invalid or not registered");
                                return;
                            }
                            shape[i] += itemFromScheme.getKey();
                            if(itemFromScheme.getValue() != null)
                            ingredients.put(itemFromScheme.getKey(), itemFromScheme.getValue());
                        }
                    }
                    rec.shape(shape[0],shape[1],shape[2]);
                    for (Map.Entry<Character, RecipeChoice> ingredient: ingredients.entrySet()) {
                        rec.setIngredient(ingredient.getKey(),ingredient.getValue());
                    }
                    Bukkit.addRecipe(rec);
                    shapedCrafts.add(c);
                });
                Map<String, List<String>> subCraftsMap = registeredCrafts.get(name);
                subCraftsMap.put("shaped", shapedCrafts);
                registeredCrafts.put(name,subCraftsMap);
            }
            if(types.contains("shapeless")){
                Set<String> crafts = ConfigManager.getKeys("crafts/all", "recipes."+name+".shapeless");
                List<String> shapelessCrafts = new ArrayList<>();
                crafts.forEach(c->{
                    List<String> itemSchemes = (List<String>) ConfigManager.getKey("crafts/all", "recipes."+name+".shapeless."+c+".ingredients", "StringList", new ArrayList<>());
                    String result = (String) ConfigManager.getKey("crafts/all", "recipes."+name+".shapeless."+c+".result", "String", "null");
                    assert result != null;
                    Map.Entry<Character, RecipeChoice> resultItemFromScheme = convertSchemeToItem(result, 'X');
                    if(resultItemFromScheme == null || resultItemFromScheme.getValue() == null){
                        BetterMinecraftCore.getInstance().getLogger().info("skipping shapeless recipe "+name+" "+c+", result is invalid or not registered");
                        return;
                    }
                    ShapelessRecipe rec;
                    if(resultItemFromScheme.getValue() instanceof RecipeChoice.ExactChoice){
                        rec = new ShapelessRecipe(NamespacedKey.minecraft("bmc_shapeless_"+name.toLowerCase()+"_"+c),((RecipeChoice.ExactChoice)resultItemFromScheme.getValue()).getItemStack());
                    } else if (resultItemFromScheme.getValue() instanceof RecipeChoice.MaterialChoice){
                        rec = new ShapelessRecipe(NamespacedKey.minecraft("bmc_shapeless_"+name.toLowerCase()+"_"+c),((RecipeChoice.MaterialChoice)resultItemFromScheme.getValue()).getItemStack());
                    } else {
                        rec = new ShapelessRecipe(NamespacedKey.minecraft("bmc_shapeless_"+name.toLowerCase()+"_"+c),((RecipeChoice.ExactChoice)resultItemFromScheme.getValue()).getItemStack());
                    }
                    assert itemSchemes != null;
                    for (int i = 0; i < itemSchemes.toArray().length; i++){
                        Map.Entry<Character, RecipeChoice> itemFromScheme = convertSchemeToItem(itemSchemes.get(i), 'X');
                        if(itemFromScheme == null || itemFromScheme.getValue() == null){
                            BetterMinecraftCore.getInstance().getLogger().info("skipping shapeless recipe "+name+" "+c+", one ingredient is invalid or not registered");
                            return;
                        }
                        rec.addIngredient(itemFromScheme.getValue());
                    }
                    BetterMinecraftCore.getInstance().getServer().addRecipe(rec);
                    shapelessCrafts.add(c);
                });
                Map<String, List<String>> subCraftsMap = registeredCrafts.get(name);
                subCraftsMap.put("shapeless", shapelessCrafts);
                registeredCrafts.put(name,subCraftsMap);
            }
        });
    }

    public static Map.Entry<Character, RecipeChoice> convertSchemeToItem(String scheme, Character character) {
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

    public static ItemStack[] convertIngredientMapToMatrix(ItemStack[] model, Map<Character, ItemStack> ingredients){
        ItemStack[] returned = new ItemStack[model.length];
        ingredients.values().removeAll(Collections.singleton(null));
        int i=0;
        for (ItemStack item: model) {
            if(item==null){
                returned[i] = null;
            } else {
                Optional<Map.Entry<Character, ItemStack>> optionnal = ingredients.entrySet().stream().findFirst();
                if(!optionnal.isPresent()){
                    returned[i] = null;
                }else{
                    Map.Entry<Character, ItemStack> itemFound = optionnal.get();
                    ingredients.remove(itemFound.getKey());
                    returned[i] = itemFound.getValue();
                }
            }
            i++;
        }
        return returned;
    }

    public static ItemStack[] convertIngredientListToMatrix(ItemStack[] model, List<ItemStack> ingredients){
        List<ItemStack> returned = new ArrayList<>();
        int i=0;
        for (ItemStack item: model) {
            if(item==null){
                returned.add(null);
            } else {
                int index = -1;
                for (int j=0; j < ingredients.toArray().length; j++) {
                    if(item.getItemMeta() != null && ingredients.get(j).getItemMeta() != null){
                        if(Objects.equals(item.getItemMeta(), ingredients.get(j).getItemMeta())){
                            if(item.getAmount() >= ingredients.get(j).getAmount()){
                                if (index != -1) {
                                    if(ingredients.get(j).getAmount() > ingredients.get(index).getAmount()){
                                        index = j;
                                    }
                                } else {
                                    index = j;
                                }
                            }
                        }
                    }
                }
                if(index == -1) return null;
                returned.add(ingredients.remove(index));
            }
            i++;
        }

        return returned.toArray(new ItemStack[model.length]);
    }
}
