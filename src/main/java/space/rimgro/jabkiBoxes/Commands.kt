package space.rimgro.jabkiBoxes

import Locale
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.SuggestionInfo
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.persistence.PersistentDataType

class Commands(plugin: JabkiBoxesPlugin) {
    init {
        CommandAPICommand("jabkiboxes")
            .withSubcommand(
                CommandAPICommand("bind")
                    .withArguments(StringArgument("id").replaceSuggestions(ArgumentSuggestions.strings {
                        return@strings plugin.boxManager.config.boxes.keys.toTypedArray()
                    }))
                    .executesPlayer(PlayerCommandExecutor{ player, args ->
                        val item = player.inventory.itemInMainHand
                        val itemMeta = item.itemMeta

                        itemMeta.persistentDataContainer.set(
                            NamespacedKey(plugin, "jabkibox_id"),
                            PersistentDataType.STRING,
                            args[0].toString()
                        )

                        item.itemMeta = itemMeta
                        Locale.COMMANDS__BIND.send(player)
                    })
            )
            .withSubcommand(
                CommandAPICommand("unbind")
                    .executesPlayer(PlayerCommandExecutor{ player, _ ->
                        val item = player.inventory.itemInMainHand
                        val itemMeta = item.itemMeta

                        itemMeta.persistentDataContainer.remove(
                            NamespacedKey(plugin, "jabkibox_id")
                        )

                        item.itemMeta = itemMeta
                        Locale.COMMANDS__UNBIND.send(player)
                    })
            )
            .withSubcommand(
                CommandAPICommand("reload")
                    .executesPlayer(PlayerCommandExecutor{ player, _ ->
                        plugin.reload()
                        Locale.COMMANDS__RELOAD.send(player)
                    })
            )
            .register()
    }
}