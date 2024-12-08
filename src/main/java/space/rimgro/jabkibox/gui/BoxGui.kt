package space.rimgro.jabkibox.gui

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import space.rimgro.jabkibox.Box
import space.rimgro.jabkibox.Drop
import space.rimgro.jabkibox.JabkiBoxPlugin
import space.rimgro.jabkibox.actions.core.IDropActionInstanceData
import space.rimgro.jabkibox.utils.Easings
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window
import kotlin.math.ceil


fun applyAction(plugin: JabkiBoxPlugin, player: Player,
                dropActionInstanceData: IDropActionInstanceData,
                boxName: String, box: Box, drop: Drop?) {
    val a = plugin.dropActionManager.getDropActionFromId(dropActionInstanceData.action)
    if (a == null) {
        plugin.logger.severe("Unable to give prize to ${player.name}! Unknown action: ${dropActionInstanceData.action}")
        return
    }
    a.apply(player, dropActionInstanceData, box, boxName, drop)
}

class BoxGui(
    private val plugin: JabkiBoxPlugin,
    val box: Box,
    val boxName: String,
    drops: List<Drop>,
    private val prize: Drop,
    val player: Player
) {
    internal val allRollContent: MutableList<Item> = drops.map {
        SimpleItem(ItemBuilder(it.display_item))
    }.toMutableList()

    internal val gui: ScrollGui<Item> = ScrollGui.items()
        .setStructure(
            "# # # # c # # # #",
            "x x x x x x x x x",
            "# # # # c # # # #"
        )
        .addIngredient('x', Markers.CONTENT_LIST_SLOT_VERTICAL)
        .addIngredient('#', SimpleItem(ItemBuilder(box.gui.placeholder_item)))
        .addIngredient('c', SimpleItem(ItemBuilder(box.gui.center_placeholder_item)))
        .setContent(allRollContent.slice(IntRange(0, 8)))
        .build()

    private val window = Window.single()
        .setViewer(player)
        .setTitle(box.gui.title)
        .setGui(gui)
        .setCloseable(false)
        .build()

    fun open() {
        window.open()
        BoxAnimationTask(this, box.gui.scroll_time, plugin, box, boxName).runTaskTimer(plugin, 0, 1)
    }

    internal fun onAnimationEnded() {
        window.isCloseable = true
        allRollContent.clear()
        prize.on_drop.forEach { applyAction(plugin, player, it, boxName, box, prize) }
        box.on_any_drop.forEach { applyAction(plugin, player, it, boxName, box, prize) }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, window::close, box.gui.close_delay)
    }
}

class BoxAnimationTask(
    private val gui: BoxGui,
    private val scrollTimer: Int,
    private val plugin: JabkiBoxPlugin,
    private val box: Box,
    private val boxName: String,
) : BukkitRunnable() {
    private var rollIndex: Int = 0
    private var timer: Double = scrollTimer.toDouble() - 10

    override fun run() {
        if (timer <= 0) {
            gui.onAnimationEnded()
            this.cancel()
            return
        }

        val newRollIndex = ceil(
            Easings.easeOutCirc(((scrollTimer - 10 - timer) / (scrollTimer - 10).toDouble())) * (scrollTimer - 10)
        ).toInt()

        if (newRollIndex != rollIndex) {
            gui.box.gui.on_scroll?.forEach { applyAction(plugin, gui.player, it, boxName, box, null) }
        }

        rollIndex = newRollIndex

        gui.gui.setContent(gui.allRollContent.slice(IntRange(rollIndex, rollIndex + 9)))
        timer--
    }
}