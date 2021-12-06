package me.rsman.BetterMinecraftCore.configs.models;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Managers.ConfigManager;
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.*;

public class BmcShapedCraft {
    private List<String> shape;
    private String result;
    private String name;
    private String key;

    public BmcShapedCraft() {}

    public List<String> getShape() {
        return shape;
    }

    public void setShape(List<String> shape) {
        this.shape = shape;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void registerCraft(){
        String[] shapeArr = {"","",""};
        Map<Character, RecipeChoice> ingredients = new HashMap<>();

        assert result != null;
        Map.Entry<Character, RecipeChoice> resultItemFromScheme = BmcCraftSubContainer.convertSchemeToRecipeChoice(result, 'X');
        if(resultItemFromScheme == null || resultItemFromScheme.getValue() == null){
            BetterMinecraftCore.getInstance().getLogger().warning("§cskipping shaped recipe §3"+name+" "+key+"§c, result is invalid or not registered");
            return;
        }
        ShapedRecipe rec;
        if(resultItemFromScheme.getValue() instanceof RecipeChoice.ExactChoice){
            rec = new ShapedRecipe(NamespacedKey.minecraft("bmc_shaped_"+name.toLowerCase()+"_"+key),((RecipeChoice.ExactChoice)resultItemFromScheme.getValue()).getItemStack());
        } else if (resultItemFromScheme.getValue() instanceof RecipeChoice.MaterialChoice){
            rec = new ShapedRecipe(NamespacedKey.minecraft("bmc_shaped_"+name.toLowerCase()+"_"+key),((RecipeChoice.MaterialChoice)resultItemFromScheme.getValue()).getItemStack());
        } else {
            rec = new ShapedRecipe(NamespacedKey.minecraft("bmc_shaped_"+name.toLowerCase()+"_"+key),((RecipeChoice.ExactChoice)resultItemFromScheme.getValue()).getItemStack());
        }
        assert shape != null;
        for (int i = 0; i < shape.toArray().length; i++){
            String[] itemSchemes = shape.get(i).split("\\|");
            for (String itemScheme: itemSchemes) {
                Map.Entry<Character, RecipeChoice> itemFromScheme = BmcCraftSubContainer.convertSchemeToRecipeChoice(itemScheme, (char)(97+i));

                if(itemFromScheme == null){
                    BetterMinecraftCore.getInstance().getLogger().warning("§cskipping shaped recipe §3"+name+" "+key+"&c, some ingredients are invalid or not registered");
                    return;
                }
                shapeArr[i] += itemFromScheme.getKey();
                if(itemFromScheme.getValue() != null)
                    ingredients.put(itemFromScheme.getKey(), itemFromScheme.getValue());
            }
        }
        rec.shape(shapeArr[0],shapeArr[1],shapeArr[2]);
        for (Map.Entry<Character, RecipeChoice> ingredient: ingredients.entrySet()) {
            rec.setIngredient(ingredient.getKey(),ingredient.getValue());
        }
        BetterMinecraftCore.getInstance().getServer().addRecipe(rec);
    }

    public BmcShapedCraft cloneForConfig() {
        BmcShapedCraft sc = new BmcShapedCraft();
        sc.setKey(null);
        sc.setName(null);
        sc.setShape(shape);
        sc.setResult(result);
        return sc;
    }

    public void registerSelfInConfig(){
        Map<String, BmcCraftSubContainer> bmcCraftSubContainerMap = BmcCraftContainer.getInstance().getRecipes();
        if(bmcCraftSubContainerMap == null){
            bmcCraftSubContainerMap = new HashMap<>();
        }
        BmcCraftSubContainer bmcCraftSubContainer = bmcCraftSubContainerMap.get(name);
        if(bmcCraftSubContainer == null){
            bmcCraftSubContainer = new BmcCraftSubContainer();
        }
        Map<String, BmcShapedCraft> bmcCraftMap = bmcCraftSubContainer.getShaped();
        if(bmcCraftMap == null){
            bmcCraftMap = new HashMap<>();
        }
        BmcShapedCraft bmcCraft = bmcCraftMap.get(key);
        if(bmcCraft == null){
            bmcCraft = new BmcShapedCraft();
        }
        bmcCraft.setKey(key);
        bmcCraft.setName(name);
        bmcCraft.setShape(shape);
        bmcCraft.setResult(result);
        bmcCraftMap.put(key,bmcCraft);
        bmcCraftSubContainer.setShaped(bmcCraftMap);
        bmcCraftSubContainerMap.put(name,bmcCraftSubContainer);
        BmcCraftContainer.getInstance().setRecipes(bmcCraftSubContainerMap);
        BmcCraftContainer.save();
        BmcCraftContainer.registerCrafts();
    }
}
