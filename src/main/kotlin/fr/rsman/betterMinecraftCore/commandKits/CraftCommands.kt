package fr.rsman.betterMinecraftCore.commandKits

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import fr.rsman.betterMinecraftCore.managers.CraftManager
import fr.rsman.betterMinecraftCore.managers.command.CommandManager
import org.bukkit.NamespacedKey

@CommandAlias("bmc|betterminecraftcore")
@Subcommand("craft|c")
class CraftCommands : BaseCommand() {
    // all config wiped out, using config for database data is useless.
    private val commandManager = CommandManager.get()
    @Subcommand("setShaped")
    @CommandCompletion("@shapedCrafts @itemOrVanilla <resultCount> [force] @nothing")
    @CommandPermission("bmc.craft.set_shaped")
    @Description("{@@bmc.command.description.craft.set_shaped}")
    @Syntax("<name.key> <result> <resultCount> [force]")
    fun onSetShaped(playerSender: Player?, nameKeyPair: String?, @Optional result: String?, @Optional resultCount: Int?, @Optional force: String?) {
        CraftManager.openCraftingEditorInventory(commandManager!!, playerSender!!, NAMESPACE_KEY, result, nameKeyPair!!, resultCount, "shaped", force)
    }

    @Subcommand("setShapeless")
    @CommandCompletion("@shapelessCrafts @itemOrVanilla <resultCount> [force] @nothing")
    @CommandPermission("bmc.craft.set_shapeless")
    @Description("{@@bmc.command.description.craft.set_shapeless}")
    @Syntax("<name.key> <result> <resultCount> [force]")
    fun onSetShapeless(playerSender: Player?, nameKeyPair: String?, @Optional result: String?, @Optional resultCount: Int?, @Optional force: String?) {
        CraftManager.openCraftingEditorInventory(commandManager!!, playerSender!!, NAMESPACE_KEY, result, nameKeyPair!!, resultCount, "shapeless", force)
    }

    companion object {
        @JvmField
        var NAMESPACE_KEY = NamespacedKey.minecraft("bmc_modify_craft")
    }
}