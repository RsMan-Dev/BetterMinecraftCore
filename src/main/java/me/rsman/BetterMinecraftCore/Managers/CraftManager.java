package me.rsman.BetterMinecraftCore.Managers;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Managers.Command.CommandManager;
import me.rsman.BetterMinecraftCore.Managers.Command.Lang.MessageKeys;
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer;
import me.rsman.BetterMinecraftCore.configs.models.BmcCraftSubContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CraftManager {

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

    public static void openCraftingEditorInventory(CommandManager commandManager, Player playerSender, NamespacedKey NAMESPACE_KEY, String result, String nameKeyPair, Integer resultCount, String type, String force){
        CommandIssuer issuerSender = commandManager.getCommandIssuer(playerSender);
        String[] nameKeyPairDettached = nameKeyPair.split("\\.");
        if (nameKeyPairDettached.length != 2) { issuerSender.sendInfo(MessageKeys.INVALID_NAME_KEY_PAIR);return;}

        Recipe r = Bukkit.getRecipe(NamespacedKey.minecraft("bmc_" + type + "_" + nameKeyPairDettached[0].toLowerCase() + "_" + nameKeyPairDettached[1]));

        if(result != null){
            ItemStack querriedItem = BmcCraftSubContainer.convertItemSchemeToItemStack(result);
            if(querriedItem == null) {
                issuerSender.sendInfo(MessageKeys.INVALID_RESULT);
                return;
            }
            boolean needToForce = false;
            if(r != null){
                if(querriedItem.equals(r.getResult())){
                    if(querriedItem.hasItemMeta() && r.getResult().hasItemMeta()){
                        ItemMeta querriedItemMeta = Objects.requireNonNull(querriedItem.getItemMeta()).clone();
                        ItemMeta resultMeta = Objects.requireNonNull(r.getResult().getItemMeta()).clone();
                        if(!Objects.equals(querriedItemMeta, resultMeta)) needToForce = true;
                    }
                } else {
                    needToForce = true;
                }
            }
            if(needToForce){
                if(force == null || !force.equals("force")){
                    issuerSender.sendInfo(MessageKeys.NEED_TO_FORCE);
                    return;
                }
            }
        } else if (r == null){
            issuerSender.sendInfo(MessageKeys.NEED_RESULT);
            return;
        }else{
            ItemStack resultItem = r.getResult();
            result = ItemManager.getItemName(resultItem).equals("") ? "m." + resultItem.getType().name().toUpperCase(Locale.ENGLISH) : ItemManager.getItemName(resultItem);
            resultCount = resultItem.getAmount();
        }


        Inventory cInv = Bukkit.createInventory(playerSender, InventoryType.DISPENSER, "Craft " + nameKeyPair);
        if(r != null){
            List<ItemStack> itemList;
            if(type.equals("shaped")){
                itemList = new ArrayList<>(((ShapedRecipe)r).getIngredientMap().values());
            } else {
                itemList = ((ShapelessRecipe)r).getIngredientList();
            }
            int i = 0;
            for (ItemStack item: itemList) {
                if(item != null) cInv.setItem(i, item.clone());
                i++;
            }
        }
        playerSender.openInventory(cInv);

        if(resultCount == null)resultCount = 1;

        playerSender.getPersistentDataContainer().set(NAMESPACE_KEY, PersistentDataType.STRING, nameKeyPair + "|" + result + "|" + resultCount + "|" + type);
    }
}
