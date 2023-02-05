package fr.rsman.betterMinecraftCore.managers.command

import co.aikar.commands.*

object CommandConditionsManager {
    fun init() {
        registerCommandCompletions()
    }

    private fun registerCommandCompletions() {
        val commandConditions: CommandConditions<BukkitCommandIssuer, BukkitCommandExecutionContext, BukkitConditionContext>? = CommandManager.get()!!.commandConditions
    }
}