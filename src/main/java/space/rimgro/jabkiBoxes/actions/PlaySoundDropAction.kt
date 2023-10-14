package space.rimgro.jabkiBoxes.actions

import com.fasterxml.jackson.annotation.JsonProperty
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.Audiences
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.Sound.Source
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import space.rimgro.jabkiBoxes.Item
import space.rimgro.jabkiBoxes.JabkiBoxesPlugin
import space.rimgro.jabkiBoxes.actions.core.DropAction
import space.rimgro.jabkiBoxes.actions.core.IDropActionInstanceData

class PlaySoundDropAction(plugin: JabkiBoxesPlugin) : DropAction(plugin) {
    override val id = "PLAYSOUND"
    override val instanceDataType: Class<out IDropActionInstanceData> = PlaySoundDropActionInstanceData::class.java
    override fun apply(player: Player, data: IDropActionInstanceData) {
        val soundData = data as? PlaySoundDropActionInstanceData ?: return

        player.playSound(soundData.soundParsed)
    }
}

data class PlaySoundDropActionInstanceData(override val action: String, @JsonProperty("sound") val soundKey: String, val source: Source = Source.MASTER, val volume: Float = 1f, val pitch: Float = 1f) : IDropActionInstanceData{
    val soundParsed: Sound by lazy {
        Sound.sound(Key.key(soundKey.lowercase()), source, volume, pitch)
    }
}
