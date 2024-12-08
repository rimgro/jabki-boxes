package space.rimgro.jabkibox.actions

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import space.rimgro.jabkibox.Box
import space.rimgro.jabkibox.Drop
import space.rimgro.jabkibox.JabkiBoxPlugin
import space.rimgro.jabkibox.actions.core.DropAction
import space.rimgro.jabkibox.actions.core.IDropActionInstanceData
import space.rimgro.jabkibox.utils.Locale

class CommandDropAction(plugin: JabkiBoxPlugin) : DropAction(plugin) {
    private val locale: Locale
        get() = plugin.locale

    override val id = "COMMAND"
    override val instanceDataType: Class<out IDropActionInstanceData> = CommandActionInstanceData::class.java
    override fun apply(player: Player, data: IDropActionInstanceData, box: Box, boxName: String, drop: Drop?) {
        val cmdData = data as? CommandActionInstanceData ?: return
        val placeholders = mutableMapOf<String, Any>(
            "player-name" to player.name,
            "player-uuid" to player.uniqueId,
            "box-name" to boxName,
            "box-displayname" to box.displayname
        )
        if (drop != null)
            placeholders["prize-displayname"] = drop.displayname
        val command = locale.replaceAll(cmdData.command, placeholders)
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
    }
}

data class CommandActionInstanceData(
    override val action: String,
    val command: String,
) : IDropActionInstanceData
