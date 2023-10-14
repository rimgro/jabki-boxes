package space.rimgro.jabkiBoxes.actions.core;

import space.rimgro.jabkiBoxes.JabkiBoxesPlugin
import space.rimgro.jabkiBoxes.actions.GiveDropAction
import space.rimgro.jabkiBoxes.actions.PlaySoundDropAction

class DropActionManager {
    private val registeredDropActions: MutableMap<String, DropAction> = mutableMapOf()
    val dropActionInstanceDataTypes: MutableMap<String, Class<out IDropActionInstanceData>> = mutableMapOf()
    val plugin: JabkiBoxesPlugin

    constructor(plugin: JabkiBoxesPlugin){
        this.plugin = plugin
        // Тут мы регистиуем DropAction'ы
        registerDropAction(GiveDropAction(plugin))
        registerDropAction(PlaySoundDropAction(plugin))
    }

    fun registerDropAction(action: DropAction){
        registeredDropActions[action.id.uppercase()] = action
        dropActionInstanceDataTypes[action.id.uppercase()] = action.instanceDataType
    }

    fun getDropActionFromId(id: String) : DropAction? {
        if (registeredDropActions.containsKey(id)){
            return registeredDropActions[id.uppercase()]
        }
        return null
    }
}
