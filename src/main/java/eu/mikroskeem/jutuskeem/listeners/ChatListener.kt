package eu.mikroskeem.jutuskeem.listeners

import com.google.inject.Inject
import eu.mikroskeem.jutuskeem.Main
import eu.mikroskeem.jutuskeem.PermissionNodes
import eu.mikroskeem.jutuskeem.Utils
import eu.mikroskeem.jutuskeem.Utils.c
import eu.mikroskeem.providerslib.api.Chat
import eu.mikroskeem.providerslib.api.Permissions
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.LinkedHashSet

/**
 * @author Mark Vainomaa
 */
class ChatListener constructor() : Listener {
    @Inject private lateinit var plugin : Main
    @Inject private lateinit var permissions: Permissions
    @Inject private lateinit var chat: Chat

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun on(e: AsyncPlayerChatEvent) {
        val format = plugin.config.getString("format.chat", "<%player_name%> %message%")
        val params = mapOf(
            Pair("player_name", e.player.name),
            Pair("player_displayname", c(e.player.displayName)),
            Pair("message", if(permissions.playerHas(e.player, PermissionNodes.COLORS.node)) c(e.message) else e.message),
            Pair("player_prefix", c(chat.getPrefix(e.player)?:"")),
            Pair("player_suffix", c(chat.getSuffix(e.player)?:""))
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
}
