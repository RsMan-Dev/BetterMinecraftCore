package fr.rsman.betterMinecraftCore.managers.command

import fr.rsman.betterMinecraftCore.configs.containers.BmcCraftContainer.Companion.getShapedKeys
import fr.rsman.betterMinecraftCore.configs.containers.BmcCraftContainer.Companion.getShapelessKeys
import fr.rsman.betterMinecraftCore.enums.EEnchants
import fr.rsman.betterMinecraftCore.enums.EAttributes
import co.aikar.commands.CommandCompletions
import co.aikar.commands.BukkitCommandCompletionContext
import dev.lone.itemsadder.api.ItemsAdder
import io.lumine.mythic.api.MythicProvider
import fr.rsman.betterMinecraftCore.managers.ItemsAdderManager
import fr.rsman.betterMinecraftCore.managers.MythicMobsManager
import org.bukkit.entity.Player
import fr.rsman.betterMinecraftCore.configs.containers.BmcItemContainer
import org.bukkit.Material
import org.bukkit.entity.EntityType
import java.util.ArrayList

object CommandCompletionsManager {
    fun init() {
        registerCommandCompletions()
    }

    private fun registerCommandCompletions() {
        val commandCompletions: CommandCompletions<BukkitCommandCompletionContext> = CommandManager.get()!!.commandCompletions
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
                return@registerAsyncCompletion EAttributes.keys
            }
            null
        }
        commandCompletions.registerAsyncCompletion("enchantment") { c: BukkitCommandCompletionContext ->
            val sender = c.sender
            if (sender is Player) {
                return@registerAsyncCompletion EEnchants.nonReplacedKeys
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