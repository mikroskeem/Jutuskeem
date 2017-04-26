package eu.mikroskeem.jutuskeem

import org.bukkit.ChatColor

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
}