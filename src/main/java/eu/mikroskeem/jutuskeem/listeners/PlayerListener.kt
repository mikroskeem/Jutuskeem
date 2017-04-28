package eu.mikroskeem.jutuskeem.listeners

import com.google.inject.Inject
import eu.mikroskeem.jutuskeem.Main
import eu.mikroskeem.jutuskeem.NicknameManager
import eu.mikroskeem.jutuskeem.PermissionNodes
import eu.mikroskeem.providerslib.api.Permissions
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author Mark Vainomaa
 */
class PlayerListener constructor() : Listener {
    @Inject private lateinit var main : Main
    @Inject private lateinit var permissions : Permissions

    @EventHandler(ignoreCancelled = true)
    fun on(e: PlayerJoinEvent) {
        if(permissions.playerHas(e.player, PermissionNodes.SOCIALSPY.node)) {
            main.socialSpyEnabled.add(e.player)
        }
        if(permissions.playerHas(e.player, PermissionNodes.NICKNAME.node)) {
            val nickname = main.nicknameManager.getNickname(e.player)
            if(nickname?.isNotEmpty()!!) {
                e.player.displayName = nickname
            }
        }
    }

    @EventHandler
    fun on(e: PlayerQuitEvent) {
        main.lastRepliedTo.compute(e.player, {_, _ -> null })
    }
}