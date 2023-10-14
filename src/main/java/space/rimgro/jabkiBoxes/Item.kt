package space.rimgro.jabkiBoxes

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


class Item(
    val material: Material,
    val displayname: String = "",
    val lore: String = "",
    val amount: Int = 1
) {
    fun toItemStack() : ItemStack{
        val itemStack = ItemStack(material, amount)
        val meta = itemStack.itemMeta
        meta.displayName(Locale.process(displayname))
        if (lore != ""){
            val parsedLore = mutableListOf<Component>()
            for (loreLineRaw in lore.split("\r?\n|\r".toRegex()).toTypedArray()) {
                parsedLore.add(Locale.deitalize(Locale.process(loreLineRaw)))
            }
            meta.lore(parsedLore)
        }
        itemStack.itemMeta = meta
        return itemStack
    }
}