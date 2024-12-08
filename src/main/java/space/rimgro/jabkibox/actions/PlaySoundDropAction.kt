package space.rimgro.jabkibox.actions

import com.fasterxml.jackson.annotation.JsonProperty
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.Sound.Source
import org.bukkit.entity.Player
import space.rimgro.jabkibox.Box
import space.rimgro.jabkibox.Drop
import space.rimgro.jabkibox.JabkiBoxPlugin
import space.rimgro.jabkibox.actions.core.DropAction
import space.rimgro.jabkibox.actions.core.IDropActionInstanceData

class PlaySoundDropAction(plugin: JabkiBoxPlugin) : DropAction(plugin) {
    override val id = "PLAYSOUND"
    override val instanceDataType: Class<out IDropActionInstanceData> = PlaySoundDropActionInstanceData::class.java
    override fun apply(player: Player, data: IDropActionInstanceData, box: Box, boxName: String, drop: Drop?) {
        val soundData = data as? PlaySoundDropActionInstanceData ?: return
        player.playSound(soundData.soundParsed)
    }
}

data class PlaySoundDropActionInstanceData(
    override val action: String,
    @JsonProperty("sound") val soundKey: String,
    val source: Source = Source.MASTER,
    val volume: Float = 1f,
    val pitch: Float = 1f
) : IDropActionInstanceData {
    val soundParsed: Sound by lazy {
        Sound.sound(Key.key(soundKey.lowercase()), source, volume, pitch)
    }
}
