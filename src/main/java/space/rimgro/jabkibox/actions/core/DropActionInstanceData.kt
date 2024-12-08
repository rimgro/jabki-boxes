package space.rimgro.jabkibox.actions.core

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode

//@JsonDeserialize(using = DropActionInstanceDataDeserializer::class)
interface IDropActionInstanceData {
    @get:JsonProperty("type")
    val action: String
}

class DropActionInstanceDataDeserializer() : JsonDeserializer<IDropActionInstanceData>() {
    private lateinit var actionMap: Map<String, Class<out IDropActionInstanceData>>

    constructor(actionMap: Map<String, Class<out IDropActionInstanceData>>) : this() {
        this.actionMap = actionMap
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): IDropActionInstanceData {
        val node: JsonNode = p.codec.readTree(p)
        val type = node.get("type").asText().uppercase()
        val actionClass = actionMap[type] ?: throw IllegalArgumentException("Unknown type: $type")
        return p.codec.treeToValue(node, actionClass)
    }
}