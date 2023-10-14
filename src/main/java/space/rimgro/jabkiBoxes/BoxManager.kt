package space.rimgro.jabkiBoxes

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import space.rimgro.jabkiBoxes.actions.core.DropActionInstanceDataDeserializer
import space.rimgro.jabkiBoxes.actions.core.IDropActionInstanceData

data class Config(
    val boxes: Map<String, Box>
)

public data class Box(
    val drops: Map<String, Drop>,
    val gui: BoxGuiData
)

data class Drop(
    val chance: Int,
    val display_item: Item,
    val on_drop: List<IDropActionInstanceData>
)

data class BoxGuiData(
    val title: String,
    val placeholder_item: Item,
    val center_placeholder_item: Item = placeholder_item,
    val scroll_time: Int,
    val on_scroll: List<IDropActionInstanceData>?
)


class BoxManager(val plugin: JabkiBoxesPlugin) {
    lateinit var config: Config
//    lateinit var boxes: Map<String, Box>

    init {
        parseConfig()
    }

    public fun parseConfig(){
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

        val module = SimpleModule()
        module.addDeserializer(IDropActionInstanceData::class.java, DropActionInstanceDataDeserializer(plugin.dropActionManager.dropActionInstanceDataTypes))
        mapper.registerModule(module)

        config = mapper.readValue(plugin.config.saveToString(), Config::class.java)


    }
}