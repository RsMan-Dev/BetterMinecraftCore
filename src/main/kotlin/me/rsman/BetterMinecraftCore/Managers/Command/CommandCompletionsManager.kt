package me.rsman.BetterMinecraftCore.Managers.Command

import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer.Companion.getShapedKeys
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer.Companion.getShapelessKeys
import me.rsman.BetterMinecraftCore.enums.EEnchants
import me.rsman.BetterMinecraftCore.enums.EAttributes
import co.aikar.commands.CommandCompletions
import co.aikar.commands.BukkitCommandCompletionContext
import dev.lone.itemsadder.api.ItemsAdder
import io.lumine.mythic.api.MythicProvider
import io.lumine.mythic.api.mobs.MobManager
import me.rsman.BetterMinecraftCore.Managers.ItemsAdderManager
import me.rsman.BetterMinecraftCore.Managers.MythicMobsManager
import org.bukkit.entity.Player
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer
import org.bukkit.Material
import org.bukkit.entity.EntityType
import java.util.ArrayList

object CommandCompletionsManager {
    fun init() {
        registerCommandCompletions()
    }

    private fun registerCommandCompletions() {
        val commandCompletions: CommandCompletions<BukkitCommandCompletionContext> = CommandManager.get()!!.getCommandCompletions()
        commandCompletions.registerAsyncCompletion("item") { c: BukkitCommandCompletionContext ->
            val sender = c.sender
            if (sender is Player) {
                val itemList: Set<String> = BmcItemContainer.instance!!.items!!.keys
                return@registerAsyncCompletion ArrayList(itemList)
            }
            null
        }
        commandCompletions.registerAsyncCompletion("itemOrVanilla") { c: BukkitCommandCompletionContext ->
            val sender = c.sender
            if (sender is Player) {
                val itemList: Set<String> = Material.values().map { "m." + it.name }.toSet() + BmcItemContainer.instance!!.items!!.keys
                return@registerAsyncCompletion ArrayList(itemList)
            }
            null
        }
        commandCompletions.registerAsyncCompletion("attribute") { c: BukkitCommandCompletionContext ->
            val sender = c.sender
            if (sender is Player) {
                return@registerAsyncCompletion EAttributes.Companion.allKeys
            }
            null
        }
        commandCompletions.registerAsyncCompletion("enchantment") { c: BukkitCommandCompletionContext ->
            val sender = c.sender
            if (sender is Player) {
                return@registerAsyncCompletion EEnchants.Companion.nonReplacedEnumKeys
            }
            null
        }
        commandCompletions.registerAsyncCompletion("shapedCrafts") { c: BukkitCommandCompletionContext ->
            val sender = c.sender
            if (sender is Player) {
                return@registerAsyncCompletion getShapedKeys()
            }
            null
        }
        commandCompletions.registerAsyncCompletion("shapelessCrafts") { c: BukkitCommandCompletionContext ->
            val sender = c.sender
            if (sender is Player) {
                return@registerAsyncCompletion getShapelessKeys()
            }
            null
        }
        commandCompletions.registerAsyncCompletion("blockDropSource") { c: BukkitCommandCompletionContext ->
            val sender = c.sender
            if (sender is Player) {
                val itemList: Set<String> = Material.values().filter { it.isBlock }.map { "m." + it.name }.toSet() +
                        if(ItemsAdderManager.isItemsAdderInstalled) ItemsAdder.getAllItems().filter { it.isBlock }.map { "ia."+it.id } else listOf()
                return@registerAsyncCompletion ArrayList(itemList)
            }
            null
        }
        commandCompletions.registerAsyncCompletion("entityDropSource") { c: BukkitCommandCompletionContext ->
            val sender = c.sender
            if (sender is Player) {
                val itemList: Set<String> = EntityType.values().map { "m." + it.name }.toSet() +
                        //if(ItemsAdderManager.isItemsAdderInstalled) ItemsAdderLoadDataEvent.filter { it. }.map { "ia."+it.id } else listOf()
                        if(MythicMobsManager.isMythicMobsInstalled) MythicProvider.get().mobManager.mobNames.map { "mm.$it" } else listOf()
                return@registerAsyncCompletion ArrayList(itemList)
            }
            null
        }
    }
}