package me.rsman.BetterMinecraftCore.Managers;

import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.Listeners.*;
import me.rsman.BetterMinecraftCore.Listeners.Enchantments.*;
import org.bukkit.event.Listener;

public final class ListenersManager {
    public static void registerAllEvents(){
        Listener[] listeners = {
            new EntityListener(),
            new PlayerConnectListener(),
            new EquipmentListener(),
            new BlockListener(),
            new CraftingListener(),
            new EnchantListener(),

            //enchantments
            new AimingListener(),
            new TelekinesisListener(),

            // Commands listener
            new CraftCommandListener(),
        };

        for (Listener listener: listeners) {
            BetterMinecraftCore.getInstance().getServer().getPluginManager().registerEvents(listener, BetterMinecraftCore.getInstance());
        }
    }
}
