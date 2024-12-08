package space.rimgro.jabkibox

import org.bukkit.inventory.ItemStack
import space.outbreak.outbreaklib.itemutils.ItemRepresentation
import space.rimgro.jabkibox.actions.core.IDropActionInstanceData

data class Config(
    val boxes: Map<String, Box>
)

data class Box(
    val displayname: String,
    val drops: Map<String, Drop>,
    val gui: BoxGuiData,
    val permission: String? = null,
    val on_any_drop: List<IDropActionInstanceData> = listOf()
)

data class Drop(
    val displayname: String,
    val chance: Int,
    val display_item: ItemStack,
    val on_drop: List<IDropActionInstanceData>
)

data class BoxGuiData(
    val title: String,
    val placeholder_item: ItemStack,
    val center_placeholder_item: ItemStack = placeholder_item,
    val scroll_time: Int = 130,
    val close_delay: Long = 40,
    val on_scroll: List<IDropActionInstanceData>?
)
