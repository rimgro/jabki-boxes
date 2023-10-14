package space.rimgro.jabkiBoxes

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class JabkiListener(val plugin: JabkiBoxesPlugin) : Listener {
    @EventHandler
    public fun boxClick(e: PlayerInteractEvent){
        if (!e.action.isRightClick) return

        val item = e.item

        if (item?.itemMeta?.persistentDataContainer?.has(NamespacedKey(plugin, "jabkibox_id")) != true) return
        e.isCancelled = true
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val boxId =
                item.itemMeta?.persistentDataContainer?.get(NamespacedKey(plugin, "jabkibox_id"), PersistentDataType.STRING)
                    ?: return@Runnable


            plugin.openBox(boxId, e.player)
            item.amount -= 1
        }, 1)
    }
}