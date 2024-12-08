package space.rimgro.jabkibox.actions

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import space.rimgro.jabkibox.Box
import space.rimgro.jabkibox.Drop
import space.rimgro.jabkibox.JabkiBoxPlugin
import space.rimgro.jabkibox.actions.core.DropAction
import space.rimgro.jabkibox.actions.core.IDropActionInstanceData

class GiveDropAction(plugin: JabkiBoxPlugin) : DropAction(plugin) {
    override val id = "GIVE"
    override val instanceDataType: Class<out IDropActionInstanceData> = GiveDropActionInstanceData::class.java
    override fun apply(player: Player, data: IDropActionInstanceData, box: Box, boxName: String, drop: Drop?) {
        val giveData = data as? GiveDropActionInstanceData ?: return
        player.inventory.addItem(giveData.item)
    }
}

data class GiveDropActionInstanceData(override val action: String, val item: ItemStack) :
    IDropActionInstanceData
