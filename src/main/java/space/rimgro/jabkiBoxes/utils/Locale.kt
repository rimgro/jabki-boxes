import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.apache.commons.text.StringSubstitutor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration

enum class Locale {
    COMMANDS__RELOAD,
    COMMANDS__BIND,
    COMMANDS__UNBIND
    ;

    private val key: String = toString().replace("__", ".").replace("_", "-").lowercase()

    fun raw(): String {
        return data.getOrDefault(key, key)
    }

    fun comp(vararg replacing: Pair<String, Any>): Component {
        return process(data.getOrDefault(key, key), *replacing)
    }


    fun send(audience: Audience) {
        audience.sendMessage(comp())
    }

    fun send(audience: Audience, vararg replacing: Pair<String, Any>) {
        audience.sendMessage(comp(*replacing))
    }

    fun sendActionBar(audience: Audience, vararg replacing: Pair<String, Any>) {
        audience.sendActionBar(comp(*replacing))
    }


    companion object {
        private val mm: MiniMessage = MiniMessage.miniMessage()
        private val data = mutableMapOf<String, String>()
        private val placeholders = mutableMapOf<String, String>()

        private fun valueByPath(config: Map<*, *>, path: String): Any? {
            return valueByPath(config, path.split("."))
        }

        private fun valueByPath(current: Map<*, *>, path: List<String>): Any? {
            if (path.size > 1) {
                val subStep = current[path[0]]
                if (subStep !is Map<*, *>) {
                    return null
                }
                return valueByPath(subStep, path.subList(1, path.size))
            }
            return current[path[0]]
        }

        /**
         * Оборачивает компонент в компонент с явно отключенным курсивом.
         * Может быть полезно, чтобы убирать курсив из описаний и названий предметов.
         * */
        fun deitalize(comp: Component): Component {
            val dn = Component.empty().decoration(TextDecoration.ITALIC, false)
            return dn.children(mutableListOf(comp))
        }

        fun load(config: Map<*, *>) {
            data.clear()
            for (key in Locale.entries)
                data[key.key] = valueByPath(config, key.key).toString()

            for (ph in (config.getOrDefault("placeholders", mapOf<String, String>()) as Map<*, *>).entries) {
                val k = ph.key
                if (k is String)
                    placeholders[k] = ph.value.toString()
            }
        }

        private fun replaceAll(str: String, map: Map<String, Any>): String {
            val substitutor = StringSubstitutor(map, "%", "%", '\\')
            return substitutor.replace(str)
        }

        /**
         * Парсит строку формата MiniMessage в компонент.
         *
         * @param text строка для перевода в компонент
         * @param replacing плейсхолдеры для замены, где ключ - имя плейсхолдера
         *  без %. Значение - Component либо любой другой объект. Компоненты будут
         *  вставлены, используя TextReplacementConfig (медленно),
         *  объекты любого другого типа - просто переведены в строку и
         *  заменены (оптимизированно)
         * */
        fun process(text: String, vararg replacing: Pair<String, Any>): Component {
            val mapComps = mutableMapOf<String, Component>()
            val mapStrings = HashMap<String, Any>(placeholders)

            for (pair in replacing) {
                if (pair.second is Component)
                    mapComps[pair.first] = pair.second as Component
                else
                    mapStrings[pair.first] = pair.second
            }

            var comp = mm.deserialize(replaceAll(text, mapStrings))

            for (entry in mapComps.iterator()) {
                comp = comp.replaceText(
                    TextReplacementConfig.builder()
                        .matchLiteral("%${entry.key}%")
                        .replacement(entry.value)
                        .build()
                )
            }
            return comp
        }

        fun deitalizeAndProcess(text: String, vararg replacing: Pair<String, Any>): Component {
            return deitalize(process(text, *replacing))
        }

        fun deitalizeAndProcess(text: String): Component {
            return deitalize(process(text))
        }

        fun process(text: String): Component {
            return mm.deserialize(text)
        }
    }
}