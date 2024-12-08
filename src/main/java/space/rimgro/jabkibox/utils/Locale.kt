package space.rimgro.jabkibox.utils

import space.outbreak.outbreaklib.LocaleBase


class Locale : LocaleBase() {
    val COMMANDS__RELOAD = L()
    val COMMANDS__BIND = L()
    val COMMANDS__UNBIND = L()
    val UNKNOWN_BOX = L()
    val NO_PERMISSION_TO_OPEN = L()

    init {
        instance = this
    }

    companion object {
        lateinit var instance: Locale
    }
}