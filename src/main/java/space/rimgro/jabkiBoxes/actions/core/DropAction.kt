package space.rimgro.jabkiBoxes.actions.core

import org.bukkit.entity.Player
import space.rimgro.jabkiBoxes.JabkiBoxesPlugin

abstract class DropAction(val plugin: JabkiBoxesPlugin) {
    abstract val id: String
    abstract val instanceDataType: Class<out IDropActionInstanceData>
    abstract fun apply(player: Player, data: IDropActionInstanceData)
}