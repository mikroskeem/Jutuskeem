package eu.mikroskeem.jutuskeem.listeners

import eu.mikroskeem.jutuskeem.Main
import eu.mikroskeem.jutuskeem.PermissionNodes
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author Mark Vainomaa
 */
class PlayerListener(private val main: Main) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun on(e: PlayerJoinEvent) {
        if(e.player.hasPermission(PermissionNodes.SOCIALSPY.node)) {
            main.socialSpyEnabled.add(e.player)
        }
        if(e.player.hasPermission(PermissionNodes.NICKNAME.node)) {
            val nickname = main.nicknameManager.getNickname(e.player)
            if(nickname != null && nickname.isNotEmpty()) {
                e.player.displayName = nickname
            }
        }
    }

    @EventHandler
    fun on(e: PlayerQuitEvent) {
        main.lastRepliedTo.compute(e.player, {_, _ -> null })
        main.lastRepliedTo.filter { it.value == e.player }.forEach { sender, _ ->
            main.lastRepliedTo[sender]!!.remove()
        }
    }
}