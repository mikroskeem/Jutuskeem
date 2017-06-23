package eu.mikroskeem.jutuskeem.listeners

import eu.mikroskeem.jutuskeem.Main
import eu.mikroskeem.jutuskeem.PermissionNodes
import eu.mikroskeem.jutuskeem.Utils.c
import eu.mikroskeem.jutuskeem.configuration.ConfigurationPath
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.*

/**
 * @author Mark Vainomaa
 */
class ChatListener(private val plugin: Main) : Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun on(e: AsyncPlayerChatEvent) {
        val format = plugin.config.getString(ConfigurationPath.F_CHAT)
        //getPlaceholders(format)
        val params = mapOf(
            Pair("player_name", e.player.name),
            Pair("player_displayname", c(e.player.displayName)),
            Pair("message", if(e.player.hasPermission(PermissionNodes.COLORS.node)) c(e.message) else e.message),
            Pair("player_prefix", e.player.getPrefix()),
            Pair("player_suffix", e.player.getSuffix())
        )
        val message = c(format, params)
        clearRecipients(e).forEach { it.sendMessage(message) }
    }

    private fun clearRecipients(event: AsyncPlayerChatEvent): Set<Player> {
        /* Try to clean, and if it fails, cancel event */
        try {
            val recipients = LinkedHashSet(event.recipients)
            event.recipients.clear()
            return recipients
        } catch (e: UnsupportedOperationException) {
            event.isCancelled = true
            return event.recipients
        }
    }

    private fun Player.getPrefix(): String = c(plugin.vaultHook.getPrefix(player)?: "")
    private fun Player.getSuffix(): String = c(plugin.vaultHook.getSuffix(player)?: "")
}
