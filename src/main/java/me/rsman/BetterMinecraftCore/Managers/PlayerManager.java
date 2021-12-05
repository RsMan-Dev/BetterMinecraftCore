package me.rsman.BetterMinecraftCore.Managers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.db.models.PlayerBaseAttr;
import me.rsman.BetterMinecraftCore.enums.EAttributes;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class PlayerManager {
    public static Map<String, Map<String, Long>> playersAttributes = new HashMap<>();

    public static Map<String, Long> getBaseAttributes(String uuid){
        return PlayerManager.getBaseAttributes(uuid, false);
    }
    public static Map<String, Long> getBaseAttributes(String uuid,Boolean force){
        if (PlayerManager.playersAttributes.containsKey(uuid) && !force) {
            return PlayerManager.playersAttributes.get(uuid);
        }
        try {
            PlayerBaseAttr pba = DBManager.playerBaseAttrDao.queryForId(uuid);
            if(pba == null){
                DBManager.playerBaseAttrDao.create(new PlayerBaseAttr(uuid));
                pba = DBManager.playerBaseAttrDao.queryForId(uuid);
            }

            Map<String, Long> playerStats = new HashMap<>();
            for(Map.Entry<String, Long> attrs : pba.getMap().entrySet()){
                playerStats.put(attrs.getKey() + "_base", attrs.getValue());
                playerStats.put(attrs.getKey() + "_equip", 0L);
                playerStats.put(attrs.getKey() + "_skill", 0L);
                playerStats.put(attrs.getKey() + "_talisman", 0L);
            }
            PlayerManager.playersAttributes.put(uuid, playerStats);
            return playerStats;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void setBaseAttribute(String uuid, String attr, Long value){
        try{
            PlayerBaseAttr pba = DBManager.playerBaseAttrDao.queryForId(uuid);
            pba.setByName(attr, value);
            DBManager.playerBaseAttrDao.update(pba);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        PlayerManager.getBaseAttributes(uuid, true);
    }

    public static void setEquippedAttributes(String uuid, Map<String, Long>attr){
        Map<String,Long> attributes = PlayerManager.playersAttributes.get(uuid);
        for (Map.Entry<String, Long> entry : attr.entrySet()) {
            attributes.put(entry.getKey()+"_equip", entry.getValue());
        }
        PlayerManager.playersAttributes.put(uuid, attributes);
    }

    public static void setSkillAttributes(String uuid, Map<String, Long>attr){
        Map<String,Long> attributes = PlayerManager.playersAttributes.get(uuid);
        for (Map.Entry<String, Long> entry : attr.entrySet()) {
            attributes.put(entry.getKey()+"_skill", entry.getValue());
        }
        PlayerManager.playersAttributes.put(uuid, attributes);
    }

    public static void setTalismansAttributes(String uuid, Map<String, Long>attr){
        Map<String,Long> attributes = PlayerManager.playersAttributes.get(uuid);
        for (Map.Entry<String, Long> entry : attr.entrySet()) {
            attributes.put(entry.getKey()+"_talisman", entry.getValue());
        }
        PlayerManager.playersAttributes.put(uuid, attributes);
    }


    public static Map<String, Long> getAttributes(String uuid){
        Map<String,Long> returnAttributes = new HashMap<>();
        Map<String,Long> attributes = PlayerManager.playersAttributes.get(uuid);
        if(attributes == null){
            PlayerManager.getBaseAttributes(uuid, true);
        }
        attributes = PlayerManager.playersAttributes.get(uuid);
        for (String attr : EAttributes.getAllKeys()) {
            returnAttributes.put(attr,
                attributes.get(attr+"_base")+attributes.get(attr+"_equip")+attributes.get(attr+"_skill")+attributes.get(attr+"_talisman")
            );
        }
        return returnAttributes;
    }

    public static void alterPlayerAttributesWithEquippedStuff(Player player){
        BetterMinecraftCore.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(BetterMinecraftCore.getInstance(),() -> {
            EntityEquipment equipment = player.getEquipment();
            assert equipment != null;
            ItemStack mainHand = equipment.getItemInMainHand();
            if(ItemTypeChecker.isArmorOrHead(mainHand)) mainHand = null;
            ItemStack offHand = equipment.getItemInOffHand();
            if(ItemTypeChecker.isArmorOrHead(offHand)) mainHand = null;
            ItemStack[] items = {
                    mainHand,
                    offHand,
                    equipment.getHelmet(),
                    equipment.getChestplate(),
                    equipment.getLeggings(),
                    equipment.getBoots(),
            };
            Map<String, Long> finalAttributes= new HashMap<>();
            for (String attr : EAttributes.getAllKeys()) { finalAttributes.put(attr, 0L); }
            for (ItemStack item : items) {
                if(item == null || item.getType() == Material.AIR || item.getType() == Material.WRITABLE_BOOK) continue;
                ItemManager.updateItem(item);
                for (String attr : EAttributes.getAllKeys()) {
                    long attrValue = ItemManager.getFinalItemAttr(item,attr);
                    finalAttributes.put(attr, finalAttributes.get(attr) + attrValue);
                }
            }
            setEquippedAttributes(player.getUniqueId().toString(), finalAttributes);
        },1);
    }

    public static void updatePlayerAttributes(Player player){
        String uuid = player.getUniqueId().toString();
        Map<String, Long> totalAttributes = PlayerManager.getAttributes(uuid);
        Map<String, Long> attributes = PlayerManager.playersAttributes.get(uuid);

        Long mana = attributes.get("currentMana");
        if(mana == null){
            attributes.put("currentMana", 0L);
            PlayerManager.playersAttributes.put(uuid, attributes);
            attributes = PlayerManager.playersAttributes.get(uuid);
            mana = attributes.get("currentMana");
        }
        mana = (long) Math.round(Math.min(Math.round(mana + totalAttributes.get("mana") * 0.02),totalAttributes.get("mana")));
        attributes.put("currentMana", mana);
        PlayerManager.playersAttributes.put(uuid, attributes);



        AttributeInstance playerAttackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if(playerAttackSpeed != null){
            playerAttackSpeed.setBaseValue(4*(1+ (double)totalAttributes.get("attackSpeed")/100));

        }
        AttributeInstance playerSpeed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if(playerSpeed != null){
            playerSpeed.setBaseValue(0.1*(1+ (double)totalAttributes.get("speed")/100));
        }
        AttributeInstance playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if(playerMaxHealth != null){
            playerMaxHealth.setBaseValue(totalAttributes.get("health"));
        }

        player.setHealth(Math.min(totalAttributes.get("health"), player.getHealth()+(double)(totalAttributes.get("strength"))/200 + 1));
        player.setHealthScale(20);


        ActionBarManager.updateActionBar(player, totalAttributes, mana);
    }
}
