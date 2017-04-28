package eu.mikroskeem.jutuskeem

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * @author Mark Vainomaa
 */
object Utils {
    fun replaceParams(map: Map<String, String>, template: String): String {
        return map.entries.stream().reduce(
                template,
                { s, e -> s.replace("%" + e.key + "%", e.value) }
        ) { s, _ -> s }
    }

    fun c(c: String): String {
        return ChatColor.translateAlternateColorCodes('&', "" + c)
    }

    fun c(c: String, p: Map<String, String>): BaseComponent {
        val components = TextComponent.fromLegacyText(Utils.replaceParams(p, c(c)))
        val finalMessage = TextComponent()
        components.forEach { finalMessage.addExtra(it) }
        return finalMessage
    }

    fun p(n: String) : Player? {
        return Bukkit.getOnlinePlayers().find { it.name == n }
    }
}