package me.rsman.BetterMinecraftCore.configs.models;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Managers.ConfigManager;
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BmcShapelessCraft {
    private List<String> ingredients;
    private String result;
    private String name;
    private String key;

    public BmcShapelessCraft() {}

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
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
        assert result != null;
        Map.Entry<Character, RecipeChoice> resultItemFromScheme = BmcCraftSubContainer.convertSchemeToRecipeChoice(result, 'X');
        if(resultItemFromScheme == null || resultItemFromScheme.getValue() == null){
            BetterMinecraftCore.getInstance().getLogger().warning("§cskipping shapeless recipe §3"+name+" "+key+"§c, result is invalid or not registered");
            return;
        }
        ShapelessRecipe rec;
        if(resultItemFromScheme.getValue() instanceof RecipeChoice.ExactChoice){
            rec = new ShapelessRecipe(NamespacedKey.minecraft("bmc_shapeless_"+name.toLowerCase()+"_"+key),((RecipeChoice.ExactChoice)resultItemFromScheme.getValue()).getItemStack());
        } else if (resultItemFromScheme.getValue() instanceof RecipeChoice.MaterialChoice){
            rec = new ShapelessRecipe(NamespacedKey.minecraft("bmc_shapeless_"+name.toLowerCase()+"_"+key),((RecipeChoice.MaterialChoice)resultItemFromScheme.getValue()).getItemStack());
        } else {
            rec = new ShapelessRecipe(NamespacedKey.minecraft("bmc_shapeless_"+name.toLowerCase()+"_"+key),((RecipeChoice.ExactChoice)resultItemFromScheme.getValue()).getItemStack());
        }
        assert ingredients != null;
        for (int i = 0; i < ingredients.toArray().length; i++){
            Map.Entry<Character, RecipeChoice> itemFromScheme = BmcCraftSubContainer.convertSchemeToRecipeChoice(ingredients.get(i), 'X');
            if(itemFromScheme == null || itemFromScheme.getValue() == null){
                BetterMinecraftCore.getInstance().getLogger().warning("§cskipping shapeless recipe §3"+name+" "+key+"§c, one ingredient is invalid or not registered");
                return;
            }
            rec.addIngredient(itemFromScheme.getValue());
        }
        BetterMinecraftCore.getInstance().getServer().addRecipe(rec);
    }

    public BmcShapelessCraft cloneForConfig() {
        BmcShapelessCraft sc = new BmcShapelessCraft();
        sc.setKey(null);
        sc.setName(null);
        sc.setIngredients(ingredients);
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
        Map<String, BmcShapelessCraft> bmcCraftMap = bmcCraftSubContainer.getShapeless();
        if(bmcCraftMap == null){
            bmcCraftMap = new HashMap<>();
        }
        BmcShapelessCraft bmcCraft = bmcCraftMap.get(key);
        if(bmcCraft == null){
            bmcCraft = new BmcShapelessCraft();
        }
        bmcCraft.setKey(key);
        bmcCraft.setName(name);
        bmcCraft.setIngredients(ingredients);
        bmcCraft.setResult(result);
        bmcCraftMap.put(key,bmcCraft);
        bmcCraftSubContainer.setShapeless(bmcCraftMap);
        bmcCraftSubContainerMap.put(name,bmcCraftSubContainer);
        BmcCraftContainer.getInstance().setRecipes(bmcCraftSubContainerMap);
        BmcCraftContainer.save();
        BmcCraftContainer.registerCrafts();
    }
}
