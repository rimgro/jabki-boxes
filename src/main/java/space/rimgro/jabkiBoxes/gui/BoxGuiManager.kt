package space.rimgro.jabkiBoxes.gui

import org.bukkit.entity.Player
import space.rimgro.jabkiBoxes.BoxGuiData
import space.rimgro.jabkiBoxes.Item

// DO NOT USE
class BoxGuiManager {
    public val openedGUIs: MutableList<BoxGui> = mutableListOf()

    public fun openGui(data: BoxGuiData, player: Player, drops: List<Item>, prize: Item){

    }
}