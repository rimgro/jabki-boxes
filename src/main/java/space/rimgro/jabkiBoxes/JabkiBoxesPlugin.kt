package space.rimgro.jabkiBoxes

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import space.rimgro.jabkiBoxes.actions.core.DropActionManager
import space.rimgro.jabkiBoxes.gui.BoxGui
import java.io.File

public class JabkiBoxesPlugin : JavaPlugin(){
    lateinit var commands: Commands
    lateinit var boxManager: BoxManager
    lateinit var dropActionManager: DropActionManager

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true))
    }

    fun reload(){
        saveDefaultConfig()
        val mapper: ObjectMapper = YAMLMapper()
            .registerModule(
                KotlinModule.Builder()
                    .withReflectionCacheSize(512)
                    .configure(KotlinFeature.NullToEmptyCollection, false)
                    .configure(KotlinFeature.NullToEmptyMap, false)
                    .configure(KotlinFeature.NullIsSameAsDefault, false)
                    .configure(KotlinFeature.SingletonSupport, false)
                    .configure(KotlinFeature.StrictNullChecks, false)
                    .build()
            )
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        Locale.load(mapper.readValue(getConfigFile("locale.yml"), Map::class.java))
        dropActionManager = DropActionManager(this)
        boxManager = BoxManager(this)
        commands = Commands(this)
        server.pluginManager.registerEvents(JabkiListener(this), this)
    }

    override fun onEnable() {
        CommandAPI.onEnable()
        reload()
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }

    fun openBox(boxId: String, player: Player) {
        if (boxId !in boxManager.config.boxes.keys) return
        val box = boxManager.config.boxes[boxId]

        if (box != null) {
            val scroll_drops = mutableListOf<Drop>()

            for (i in 1..box.gui.scroll_time) {
                scroll_drops.add(getRandomDrop(box.drops.values.toList()))
            }

            val drop = scroll_drops[box.gui.scroll_time-6]

            box.gui.let { BoxGui(this, it).open(player, scroll_drops, drop) }
        }
    }

    fun getRandomDrop(drops: List<Drop>): Drop {
        val totalChance = drops.sumOf { it.chance }
        var random = Math.random() * totalChance
        for (drop in drops) {
            random -= drop.chance
            if (random <= 0) return drop
        }
        throw RuntimeException("Should never be reached")
    }

    private fun getConfigFile(filename: String): File {
        val file = File(dataFolder, filename)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            saveResource(filename, false)
        }
        return file
    }

}