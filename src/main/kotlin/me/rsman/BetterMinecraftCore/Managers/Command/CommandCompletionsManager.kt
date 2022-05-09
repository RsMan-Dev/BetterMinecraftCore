package me.rsman.BetterMinecraftCore.Managers.Command

import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer.Companion.getShapedKeys
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer.Companion.getShapelessKeys
import me.rsman.BetterMinecraftCore.enums.EEnchants
import me.rsman.BetterMinecraftCore.enums.EAttributes
import co.aikar.commands.CommandCompletions
import co.aikar.commands.BukkitCommandCompletionContext
import org.bukkit.entity.Player
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer
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
                val itemList: Set<String> = setOf("<vanilla_item_ex:_m.ITEM_ID>") + BmcItemContainer.instance!!.items!!.keys
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
    }
}