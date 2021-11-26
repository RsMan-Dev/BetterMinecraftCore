package me.rsman.BetterMinecraftCore.Listeners;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Managers.CraftManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Objects;

public class CraftingListener implements Listener {

    @EventHandler
    public void onCraftChange(PrepareItemCraftEvent e){
        CraftingInventory inv = e.getInventory();
        ItemStack[] matrix = e.getInventory().getMatrix();
        ItemStack[] ingredientMatrix;
        if(e.getRecipe() instanceof ShapedRecipe){
            ingredientMatrix = CraftManager.convertIngredientMapToMatrix(inv.getMatrix(), ((ShapedRecipe) Objects.requireNonNull(inv.getRecipe())).getIngredientMap());
        } else if(e.getRecipe() instanceof ShapelessRecipe){
            ingredientMatrix = CraftManager.convertIngredientListToMatrix(inv.getMatrix(), ((ShapelessRecipe) Objects.requireNonNull(inv.getRecipe())).getIngredientList());
            if(ingredientMatrix == null) {
                e.getInventory().setResult(null);
                return;
            }
        } else {
            return;
        }

        int i=-1;
        for (ItemStack item : ingredientMatrix) {
            i++;
            if(matrix[i] == null || item == null)continue;
            if(!(matrix[i].getAmount() >= item.getAmount())){
                e.getInventory().setResult(null);
            }
            if(matrix[i].getItemMeta() != null && item.getItemMeta() != null){
                ItemMeta ingredientMeta = item.getItemMeta().clone();
                ItemMeta matrixMeta = Objects.requireNonNull(matrix[i].getItemMeta()).clone();
                if(ingredientMeta instanceof Damageable){
                    ((Damageable)ingredientMeta).setDamage(0);
                    ((Damageable)matrixMeta).setDamage(0);
                }
                if(!(Objects.equals(ingredientMeta, matrixMeta))){
                    e.getInventory().setResult(null);
                }
            } else if (matrix[i].getItemMeta() != item.getItemMeta()){
                e.getInventory().setResult(null);
            }
        }

    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getInventory().getHolder() instanceof Player && ((Player)e.getInventory().getHolder()).getGameMode() == GameMode.CREATIVE && ((Player)e.getInventory().getHolder()).getGameMode() == GameMode.SPECTATOR)return;
        if(e.getInventory() instanceof CraftingInventory && e.getClickedInventory() != null){
            CraftingInventory inv = (CraftingInventory)e.getInventory();
            boolean isCraftInventory = e.getClickedInventory().getClass().getName().endsWith("CraftInventoryCrafting")
                    || e.getClickedInventory().getClass().getName().endsWith("CraftInventoryPlayer");
            if(isCraftInventory || e.isShiftClick()){
                if(isCraftInventory && e.getSlot() == 0){return;}
                BetterMinecraftCore.getInstance().getServer().getScheduler().runTaskLater(BetterMinecraftCore.getInstance(),
                        () -> inv.setMatrix(inv.getMatrix()),1);
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){
        if(!(e.getWhoClicked() instanceof Player))return;
        Player p = (Player) e.getWhoClicked();
        PlayerInventory pinv= p.getInventory();
        CraftingInventory inv = e.getInventory();
        if(e.getCurrentItem() != null && !e.getCurrentItem().equals(inv.getResult())){ return; }
        ItemStack[] newMatrix = inv.getMatrix();
        ItemStack[] ingredientMatrix;
        ItemStack result = Objects.requireNonNull(inv.getResult()).clone();
        if(e.getRecipe() instanceof ShapedRecipe){
            ingredientMatrix = CraftManager.convertIngredientMapToMatrix(inv.getMatrix(), ((ShapedRecipe) Objects.requireNonNull(inv.getRecipe())).getIngredientMap());
        } else if(e.getRecipe() instanceof ShapelessRecipe){
            ingredientMatrix = CraftManager.convertIngredientListToMatrix(inv.getMatrix(), ((ShapelessRecipe) Objects.requireNonNull(inv.getRecipe())).getIngredientList());
            if(ingredientMatrix == null) {
                e.setCancelled(true);
                return;
            }
        } else {
            return;
        }


        e.setCancelled(true);
        int maxItemsPossible = result.getMaxStackSize() / result.getAmount();
        int i=0;
        for (ItemStack item: ingredientMatrix) {
            if(newMatrix[i] != null && item != null){
                maxItemsPossible = (int) Math.min(maxItemsPossible,
                        Math.floor((double)newMatrix[i].getAmount()/item.getAmount())
                );
            }
            i++;
        }


        if(!e.isShiftClick()){
            if( maxItemsPossible >=1 ) maxItemsPossible = result.getAmount();
            result.setAmount(maxItemsPossible);
            if(p.getItemOnCursor().getType() != Material.AIR) {
                if(p.getItemOnCursor().hasItemMeta() && result.hasItemMeta()){
                    if(!Objects.equals(p.getItemOnCursor().getItemMeta(), result.getItemMeta()))return;
                }
                if(p.getItemOnCursor().getAmount() + maxItemsPossible > p.getItemOnCursor().getMaxStackSize()) return;
                p.getItemOnCursor().setAmount(p.getItemOnCursor().getAmount() + maxItemsPossible);
            } else {
                p.setItemOnCursor(result);
            }
        } else {
            result.setAmount(maxItemsPossible * result.getAmount());
            Map<Integer, ItemStack> notadded = pinv.addItem(result);
            if(!notadded.isEmpty()){
                maxItemsPossible -= notadded.get(0).getAmount();
            }
        }


        i=0;
        for (ItemStack item: ingredientMatrix) {
            if(newMatrix[i] != null){
                int newAmount = newMatrix[i].getAmount()-(item.getAmount()*maxItemsPossible);
                if(newAmount <= 0){
                    newMatrix[i] = null;
                } else {
                    newMatrix[i].setAmount(newAmount);
                }
            }
            i++;
        }

        inv.setMatrix(newMatrix);
    }
}
