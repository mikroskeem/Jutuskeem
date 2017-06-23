/*
 * This file is part of project Jutuskeem, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
