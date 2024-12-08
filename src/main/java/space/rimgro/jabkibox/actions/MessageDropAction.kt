package space.rimgro.jabkibox.actions

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import space.rimgro.jabkibox.Box
import space.rimgro.jabkibox.Drop
import space.rimgro.jabkibox.JabkiBoxPlugin
import space.rimgro.jabkibox.actions.core.DropAction
import space.rimgro.jabkibox.actions.core.IDropActionInstanceData
import space.rimgro.jabkibox.utils.Locale

class MessageDropAction(plugin: JabkiBoxPlugin) : DropAction(plugin) {
    override val id = "MESSAGE"
    override val instanceDataType: Class<out IDropActionInstanceData> = MessageDropActionInstanceData::class.java
    override fun apply(player: Player, data: IDropActionInstanceData, box: Box, boxName: String, drop: Drop?) {
        val msgData = data as? MessageDropActionInstanceData ?: return
        val placeholders = mutableMapOf<String, Any>(
            "player-name" to player.name,
            "player-uuid" to player.uniqueId,
            "box-name" to boxName,
            "box-displayname" to box.displayname
        )
        if (drop != null)
            placeholders["prize-displayname"] = drop.displayname

        val comp = Locale.instance.process(msgData.message, placeholders)

        if (msgData.broadcast)
            Bukkit.broadcast(comp)
        else
            player.sendMessage(comp)
    }
}

data class MessageDropActionInstanceData(
    override val action: String,
    val message: String,
    val broadcast: Boolean = false
) : IDropActionInstanceData
