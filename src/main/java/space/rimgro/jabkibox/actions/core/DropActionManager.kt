package space.rimgro.jabkibox.actions.core

import space.rimgro.jabkibox.JabkiBoxPlugin
import space.rimgro.jabkibox.actions.CommandDropAction
import space.rimgro.jabkibox.actions.GiveDropAction
import space.rimgro.jabkibox.actions.MessageDropAction
import space.rimgro.jabkibox.actions.PlaySoundDropAction

class DropActionManager(
    plugin: JabkiBoxPlugin
) {
    private val registeredDropActions: MutableMap<String, DropAction> = mutableMapOf()
    val dropActionInstanceDataTypes: MutableMap<String, Class<out IDropActionInstanceData>> = mutableMapOf()

    init {
        // Тут мы регистрируем DropAction'ы
        registerDropAction(GiveDropAction(plugin))
        registerDropAction(PlaySoundDropAction(plugin))
        registerDropAction(CommandDropAction(plugin))
        registerDropAction(MessageDropAction(plugin))
    }

    fun registerDropAction(action: DropAction) {
        registeredDropActions[action.id.uppercase()] = action
        dropActionInstanceDataTypes[action.id.uppercase()] = action.instanceDataType
    }

    fun getDropActionFromId(id: String): DropAction? {
        return registeredDropActions[id.uppercase()]
    }
}
