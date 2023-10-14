package space.rimgro.jabkiBoxes.gui

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.scheduler.BukkitRunnable
import space.rimgro.jabkiBoxes.BoxGuiData
import space.rimgro.jabkiBoxes.Drop
import space.rimgro.jabkiBoxes.JabkiBoxesPlugin
import space.rimgro.jabkiBoxes.utils.Easings
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.item.impl.controlitem.ScrollItem
import xyz.xenondevs.invui.window.Window
import kotlin.math.ceil

class BoxGui(val plugin: JabkiBoxesPlugin, val data: BoxGuiData) {
    lateinit var window: Window
    lateinit var gui: ScrollGui<Item>
    val allRollContent: MutableList<Item> = mutableListOf()
    lateinit var prize: Drop
    lateinit var openingPlayer: Player

    fun open(player: Player, drops: List<Drop>, prize: Drop) {
        drops.forEach {
            allRollContent.add(SimpleItem(ItemBuilder(it.display_item.toItemStack())))
        }


        gui = ScrollGui.items()
            .setStructure(
                "# # # # c # # # #",
                "x x x x x x x x x",
                "# # # # c # # # #")
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_VERTICAL)
            .addIngredient('#', SimpleItem(ItemBuilder(data.placeholder_item.toItemStack())))
            .addIngredient('c', SimpleItem(ItemBuilder(data.center_placeholder_item.toItemStack())))
            .setContent(allRollContent.slice(IntRange(0, 8)))
            .build()

        window = Window.single()
            .setViewer(player)
            .setTitle(data.title)
            .setGui(gui)
            .setCloseable(false)
            .build()

        window.open()
        openingPlayer = player
        this.prize = prize
        BoxAnimationTask(this, data.scroll_time)
            .runTaskTimer(plugin, 0, 1)

    }


    fun onAnimationEnded(){
        window.isCloseable = true
        allRollContent.clear()
        prize.on_drop.forEach{
            plugin.dropActionManager.getDropActionFromId(it.action)?.apply(openingPlayer, it)
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Runnable {
            window.close()
        }, 60)
    }
}

class BoxAnimationTask(val gui: BoxGui, val scroll_timer: Int) : BukkitRunnable(){
    var rollIndex: Int = 0
    var timer : Double = scroll_timer.toDouble() - 10

    override fun run() {
        if (timer <= 0){
            gui.onAnimationEnded()
            this.cancel()

            return
        }
        val newRollIndex = ceil(Easings.easeOutCirc(((scroll_timer - 10 - timer) / (scroll_timer - 10).toDouble())) * (scroll_timer - 10)).toInt()
        if (newRollIndex != rollIndex){
            gui.data.on_scroll?.forEach {
                gui.plugin.dropActionManager.getDropActionFromId(it.action)?.apply(gui.openingPlayer, it)
            }
        }
        rollIndex = newRollIndex

        gui.gui.setContent(gui.allRollContent.slice(IntRange(rollIndex, rollIndex+9)))
        timer--
    }
}