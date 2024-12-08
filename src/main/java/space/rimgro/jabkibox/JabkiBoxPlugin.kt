package space.rimgro.jabkibox

import com.fasterxml.jackson.databind.module.SimpleModule
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import space.outbreak.outbreaklib.OutbreakPlugin
import space.outbreak.outbreaklib.itemutils.ItemUtils
import space.rimgro.jabkibox.actions.core.DropActionInstanceDataDeserializer
import space.rimgro.jabkibox.actions.core.DropActionManager
import space.rimgro.jabkibox.actions.core.IDropActionInstanceData
import space.rimgro.jabkibox.gui.BoxGui
import space.rimgro.jabkibox.utils.Locale

class JabkiBoxPlugin : OutbreakPlugin() {
    val dropActionManager = DropActionManager(this)
    lateinit var conf: Config
    internal val locale = Locale()
    internal val itemUtils = ItemUtils(this, locale.serializer)

    val jabkiboxIdKey = NamespacedKey(this, "jabkibox_id")

    override fun onLoad() {
        val module = SimpleModule()
        module.addDeserializer(
            IDropActionInstanceData::class.java,
            DropActionInstanceDataDeserializer(dropActionManager.dropActionInstanceDataTypes)
        )
        mapper.registerModule(module)
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true))
    }

    fun loadConfigs() {
        conf = readConfig("config.yml", Config::class.java)
        locale.load(readConfig("locale.yml", Map::class.java))
    }

    override fun onEnable() {
        itemUtils.registerItemRepresentationSerializer()

        CommandAPI.onEnable()
        loadConfigs()
        server.pluginManager.registerEvents(JabkiListener(this), this)
        Commands(this)
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }

    fun bindBox(item: ItemStack, boxId: String) {
        val itemMeta = item.itemMeta
        itemMeta.persistentDataContainer.set(
            jabkiboxIdKey,
            PersistentDataType.STRING, boxId
        )
        item.itemMeta = itemMeta
    }

    fun unbindBox(item: ItemStack) {
        val itemMeta = item.itemMeta
        itemMeta.persistentDataContainer.remove(jabkiboxIdKey)
        item.itemMeta = itemMeta
    }

    fun openBox(boxId: String, player: Player) {
        if (!player.hasPermission(Perm.OPEN)) {
            locale.NO_PERMISSION_TO_OPEN.send(player)
            return
        }

        val box = conf.boxes[boxId]
        if (box == null) {
            locale.UNKNOWN_BOX.send(player, "box" to boxId)
            return
        }

        if (box.permission != null && !player.hasPermission(box.permission)) {
            locale.NO_PERMISSION_TO_OPEN.send(player)
            return
        }

        val drops = mutableListOf<Drop>()

        for (i in 1..box.gui.scroll_time)
            drops.add(getRandomDrop(box.drops.values.toList()))

        val drop = drops[box.gui.scroll_time - 6]

        BoxGui(this, box, boxId, drops, drop, player).open()
    }

    private fun getRandomDrop(drops: List<Drop>): Drop {
        val totalChance = drops.sumOf { it.chance }
        var random = Math.random() * totalChance
        for (drop in drops) {
            random -= drop.chance
            if (random <= 0) return drop
        }
        throw RuntimeException("Should never be reached")
    }
}