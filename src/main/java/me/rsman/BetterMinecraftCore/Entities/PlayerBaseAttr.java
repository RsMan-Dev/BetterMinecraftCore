package me.rsman.BetterMinecraftCore.Entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.rsman.BetterMinecraftCore.BetterMinecraftCore;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@DatabaseTable(tableName = "player_base_attrs")
public class PlayerBaseAttr {

    @DatabaseField(dataType = DataType.STRING, id = true, width = 36)
    private String uuid;

    @DatabaseField()
    private long health = 100;

    @DatabaseField()
    private long damage = 1;

    @DatabaseField()
    private long defense = 0;

    @DatabaseField()
    private long strength = 0;

    @DatabaseField()
    private long speed = 0;

    @DatabaseField()
    private long critChance = 0;

    @DatabaseField()
    private long critDamage = 0;

    @DatabaseField()
    private long attackSpeed = 0;

    @DatabaseField()
    private long intelligence = 0;

    @DatabaseField()
    private long mana = 100;

    public PlayerBaseAttr() { }
    public PlayerBaseAttr(String uuid) {
        this.uuid = uuid;
    }

    public Map<String, Long> getMap(){
        return new HashMap<String, Long>(){{
            put("health", health);
            put("damage", damage);
            put("defense", defense);
            put("strength", strength);
            put("speed", speed);
            put("critChance", critChance);
            put("critDamage", critDamage);
            put("attackSpeed", attackSpeed);
            put("intelligence", intelligence);
            put("mana", mana);
        }};
    }

    public PlayerBaseAttr setByName(String attrName, long value){
        try{
            Field field = this.getClass().getDeclaredField(attrName);
            field.setLong(this, value);
            return this;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            BetterMinecraftCore.getInstance().getLogger().info("trying to set "+attrName+" attribute, but does not exists");
            return this;
        }
    }

    public PlayerBaseAttr setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public long getHealth() {
        return health;
    }

    public PlayerBaseAttr setHealth(long health) {
        this.health = health;
        return this;
    }

    public long getDamage() {
        return damage;
    }

    public PlayerBaseAttr setDamage(long damage) {
        this.damage = damage;
        return this;
    }

    public long getDefense() {
        return defense;
    }

    public PlayerBaseAttr setDefense(long defense) {
        this.defense = defense;
        return this;
    }

    public long getStrength() {
        return strength;
    }

    public PlayerBaseAttr setStrength(long strength) {
        this.strength = strength;
        return this;
    }

    public long getSpeed() {
        return speed;
    }

    public PlayerBaseAttr setSpeed(long speed) {
        this.speed = speed;
        return this;
    }

    public long getCritChance() {
        return critChance;
    }

    public PlayerBaseAttr setCritChance(long critChance) {
        this.critChance = critChance;
        return this;
    }

    public long getCritDamage() {
        return critDamage;
    }

    public PlayerBaseAttr setCritDamage(long critDamage) {
        this.critDamage = critDamage;
        return this;
    }

    public long getAttackSpeed() {
        return attackSpeed;
    }

    public PlayerBaseAttr setAttackSpeed(long attackSpeed) {
        this.attackSpeed = attackSpeed;
        return this;
    }

    public long getIntelligence() {
        return intelligence;
    }

    public PlayerBaseAttr setIntelligence(long intelligence) {
        this.intelligence = intelligence;
        return this;
    }

    public long getMana() {
        return mana;
    }

    public PlayerBaseAttr setMana(long mana) {
        this.mana = mana;
        return this;
    }
}
