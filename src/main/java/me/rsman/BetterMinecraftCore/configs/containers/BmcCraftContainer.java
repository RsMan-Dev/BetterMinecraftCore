package me.rsman.BetterMinecraftCore.configs.containers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.configs.ConfigLoader;
import me.rsman.BetterMinecraftCore.configs.models.BmcCraftSubContainer;
import me.rsman.BetterMinecraftCore.configs.models.BmcItem;
import me.rsman.BetterMinecraftCore.configs.models.BmcShapedCraft;
import me.rsman.BetterMinecraftCore.configs.models.BmcShapelessCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BmcCraftContainer {
    private static BmcCraftContainer instance;
    private static List<String> keys;
    private static List<String> shapedKeys;
    private static List<String> shapelessKeys;

    public static List<String> getKeys() {
        return keys;
    }

    public static List<String> getShapedKeys() {
        return shapedKeys;
    }

    public static List<String> getShapelessKeys() {
        return shapelessKeys;
    }

    private static void setInstance(BmcCraftContainer instance) {
        BmcCraftContainer.instance = instance;
    }

    public static BmcCraftContainer getInstance() {
        return instance;
    }

    public static void load(){
        setInstance(null);
        BetterMinecraftCore.getInstance().getLogger().info("§3Loading BMC items...");
        BmcCraftContainer bmcCraftContainerInstance = ConfigLoader.loadConfig("crafts/all", BmcCraftContainer.class);
        if(bmcCraftContainerInstance == null){
            BetterMinecraftCore.getInstance().getLogger().severe("§4Crafts cannot be loaded");
        } else {
            keys = new ArrayList<>();
            shapedKeys = new ArrayList<>();
            shapelessKeys = new ArrayList<>();
            for (Map.Entry<String, BmcCraftSubContainer> craftSc : bmcCraftContainerInstance.getRecipes().entrySet()) {
                if(craftSc.getValue().getShaped() != null){
                    for(Map.Entry<String, BmcShapedCraft> shapedCraft : craftSc.getValue().getShaped().entrySet()){
                        BmcShapedCraft shapedCraftObj = shapedCraft.getValue();
                        shapedCraftObj.setName(craftSc.getKey());
                        shapedCraftObj.setKey(shapedCraft.getKey());
                        bmcCraftContainerInstance.getRecipes().get(craftSc.getKey()).getShaped().put(shapedCraft.getKey(),shapedCraftObj);
                        keys.add(shapedCraftObj.getName() + "." + shapedCraftObj.getKey());
                        shapedKeys.add(shapedCraftObj.getName() + "." + shapedCraftObj.getKey());
                    }
                }
                if(craftSc.getValue().getShapeless() != null){
                    for(Map.Entry<String, BmcShapelessCraft> shapedCraft : craftSc.getValue().getShapeless().entrySet()){
                        BmcShapelessCraft shapelessCraftObj = shapedCraft.getValue();
                        shapelessCraftObj.setName(craftSc.getKey());
                        shapelessCraftObj.setKey(shapedCraft.getKey());
                        bmcCraftContainerInstance.getRecipes().get(craftSc.getKey()).getShapeless().put(shapedCraft.getKey(),shapelessCraftObj);
                        keys.add(shapelessCraftObj.getName() + "." + shapelessCraftObj.getKey());
                        shapelessKeys.add(shapelessCraftObj.getName() + "." + shapelessCraftObj.getKey());
                    }
                }
            }
            BetterMinecraftCore.getInstance().getLogger().info("§bLoaded §6" + keys.size() + " §bcrafts." );
            if(GlobalConfigContainer.getInstance().isVerbose()){
                BetterMinecraftCore.getInstance().getLogger().info("§bLoaded crafts: §6" + keys );
            }
        }
        setInstance(bmcCraftContainerInstance);
    }

    public static void save(){
        BetterMinecraftCore.getInstance().getLogger().info("§3Saving BMC crafts...");
        BmcCraftContainer bmcCraftContainerClone = BmcCraftContainer.getInstance().cloneForConfig();
        ConfigLoader.saveConfig("crafts/all", bmcCraftContainerClone);
        BetterMinecraftCore.getInstance().getLogger().info("§bSaved BMC crafts." );
    }

    public static void registerCrafts(){
        BetterMinecraftCore.getInstance().getServer().resetRecipes();
        for (Map.Entry<String, BmcCraftSubContainer> craftSc : instance.getRecipes().entrySet()) {
            if(craftSc.getValue().getShaped() != null){
                for(Map.Entry<String, BmcShapedCraft> shapedCraft : craftSc.getValue().getShaped().entrySet()){
                    shapedCraft.getValue().registerCraft();
                }
            }
            if(craftSc.getValue().getShapeless() != null){
                for(Map.Entry<String, BmcShapelessCraft> shapedCraft : craftSc.getValue().getShapeless().entrySet()){
                    shapedCraft.getValue().registerCraft();
                }
            }
        }
    }

    private Map<String, BmcCraftSubContainer> recipes;

    public BmcCraftContainer() {}

    public Map<String, BmcCraftSubContainer> getRecipes() {
        return recipes;
    }

    public void setRecipes(Map<String, BmcCraftSubContainer> recipes) {
        this.recipes = recipes;
    }

    public BmcCraftContainer cloneForConfig() {
        BmcCraftContainer cc = new BmcCraftContainer();
        HashMap<String, BmcCraftSubContainer> craftsClone = new HashMap<>();
        for (Map.Entry<String, BmcCraftSubContainer> craftToClone: recipes.entrySet()) {
            craftsClone.put(craftToClone.getKey(), craftToClone.getValue().cloneForConfig());
        }
        cc.setRecipes(craftsClone);
        return cc;
    }
}
