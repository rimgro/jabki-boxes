package space.rimgro.jabkiBoxes.actions

import org.bukkit.entity.Player
import space.rimgro.jabkiBoxes.Item
import space.rimgro.jabkiBoxes.JabkiBoxesPlugin
import space.rimgro.jabkiBoxes.actions.core.DropAction
import space.rimgro.jabkiBoxes.actions.core.IDropActionInstanceData

class GiveDropAction(plugin: JabkiBoxesPlugin) : DropAction(plugin) {
    override val id = "GIVE"
    override val instanceDataType: Class<out IDropActionInstanceData> = GiveDropActionInstanceData::class.java
    override fun apply(player: Player, data: IDropActionInstanceData) {
        val giveData = data as? GiveDropActionInstanceData ?: return

        player.inventory.addItem(giveData.item.toItemStack())
    }
}

data class GiveDropActionInstanceData(override val action: String, val item: Item) : IDropActionInstanceData
