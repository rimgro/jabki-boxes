package space.rimgro.jabkibox

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class JabkiListener(private val plugin: JabkiBoxPlugin) : Listener {
    @EventHandler
    fun boxClick(e: PlayerInteractEvent) {
        if (!e.action.isRightClick) return
        val item = e.item ?: return

        val boxId = item.itemMeta?.persistentDataContainer
            ?.get(plugin.jabkiboxIdKey, PersistentDataType.STRING) ?: return

        e.isCancelled = true

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            plugin.openBox(boxId, e.player)
            if (e.player.gameMode != GameMode.CREATIVE)
                item.amount -= 1
        }, 1)
    }
}