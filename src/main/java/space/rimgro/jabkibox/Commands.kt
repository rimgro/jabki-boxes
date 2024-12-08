package space.rimgro.jabkibox

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import space.rimgro.jabkibox.error.UnknownBoxException
import space.rimgro.jabkibox.utils.Locale

class Commands(private val plugin: JabkiBoxPlugin) {
    private val locale: Locale
        get() = plugin.locale

    init {
        CommandAPICommand("jabkibox")
            .withSubcommands(
                CommandAPICommand("bind")
                    .withPermission(Perm.BIND_UNBIND)
                    .withArguments(
                        StringArgument("id").replaceSuggestions(ArgumentSuggestions.strings {
                            return@strings plugin.conf.boxes.keys.toTypedArray()
                        })
                    )
                    .executesPlayer(PlayerCommandExecutor { player, args ->
                        val boxId = args["id"] as String
                        try {
                            plugin.bindBox(player.inventory.itemInMainHand, boxId)
                            locale.COMMANDS__BIND.send(player, "box" to boxId)
                        } catch (e: UnknownBoxException) {
                            locale.UNKNOWN_BOX.send(player, "box" to boxId)
                        }
                    }),
                CommandAPICommand("unbind")
                    .withPermission(Perm.BIND_UNBIND)
                    .executesPlayer(PlayerCommandExecutor { player, _ ->
                        plugin.unbindBox(player.inventory.itemInMainHand)
                        locale.COMMANDS__UNBIND.send(player)
                    }),
                CommandAPICommand("reload")
                    .executesPlayer(PlayerCommandExecutor { player, _ ->
                        plugin.loadConfigs()
                        locale.COMMANDS__RELOAD.send(player)
                    })
            )
            .register()
    }
}