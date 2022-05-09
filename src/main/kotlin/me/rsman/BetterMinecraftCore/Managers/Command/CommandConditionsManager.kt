package me.rsman.BetterMinecraftCore.Managers.Command

import co.aikar.commands.*
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer.Companion.getShapedKeys
import me.rsman.BetterMinecraftCore.configs.containers.BmcCraftContainer.Companion.getShapelessKeys
import me.rsman.BetterMinecraftCore.enums.EEnchants
import me.rsman.BetterMinecraftCore.enums.EAttributes
import org.bukkit.entity.Player
import me.rsman.BetterMinecraftCore.configs.containers.BmcItemContainer
import java.util.ArrayList

object CommandConditionsManager {
    fun init() {
        registerCommandCompletions()
    }

    private fun registerCommandCompletions() {
        val commandConditions: CommandConditions<BukkitCommandIssuer, BukkitCommandExecutionContext, BukkitConditionContext>? = CommandManager.get()!!.commandConditions
    }
}