package space.rimgro.jabkibox.actions.core

import org.bukkit.entity.Player
import space.rimgro.jabkibox.Box
import space.rimgro.jabkibox.Drop
import space.rimgro.jabkibox.JabkiBoxPlugin

abstract class DropAction(val plugin: JabkiBoxPlugin) {
    abstract val id: String
    abstract val instanceDataType: Class<out IDropActionInstanceData>
    abstract fun apply(player: Player, data: IDropActionInstanceData, box: Box, boxName: String, drop: Drop?)
}