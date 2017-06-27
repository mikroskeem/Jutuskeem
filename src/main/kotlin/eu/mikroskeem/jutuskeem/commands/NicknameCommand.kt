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

package eu.mikroskeem.jutuskeem.commands

import eu.mikroskeem.jutuskeem.Main
import eu.mikroskeem.jutuskeem.PermissionNodes
import eu.mikroskeem.jutuskeem.Utils.c
import eu.mikroskeem.jutuskeem.Utils.p
import eu.mikroskeem.jutuskeem.configuration.ConfigurationPath
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Mark Vainomaa
 */
class NicknameCommand(private val plugin: Main) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(command.name == "nickname") {
            if(label == "clearnickname" || label == "clearnick") {
                if(args.isEmpty()) {
                    if(sender is Player) {
                        plugin.nicknameManager.setNickname(sender, null)
                        plugin.messages.sendMessage(sender, "nickname-cleared", mapOf(Pair("player", sender.name)))
                    } else {
                        plugin.messages.sendMessage(sender, "invalid-command-usage",
                                mapOf(Pair("usage", "/$label <player>")))
                    }
                } else if(args.size == 1) {
                    if(sender is Player && !sender.hasPermission(PermissionNodes.CLEARNICKNAME.node)) {
                        plugin.messages.sendMessage(sender, "no-permission", null)
                        return true
                    }
                    val player = p(args[0])

                    // Bail out if player is not online
                    if(player == null) {
                        plugin.messages.sendMessage(sender, "no-such-player", mapOf(Pair("player", args[0])))
                        return true
                    }

                    // Set player's nickname
                    plugin.nicknameManager.setNickname(player, null)
                    plugin.messages.sendMessage(sender, "nickname-cleared", mapOf(Pair("player", player.name)))
                } else {
                    plugin.messages.sendMessage(sender, "invalid-command-usage",
                            mapOf(Pair("usage", "/$label ${if(sender is Player) "[player]" else "<player>"}")))
                }
            } else {
                if(args.size == 1) {
                    val nickname = c(args[0])

                    // Bail out if sender is not player
                    if(sender !is Player) {
                        plugin.messages.sendMessage(sender, "invalid-command-usage", mapOf(Pair("usage", "/$label <player> <nickname>")))
                        return true
                    }

                    // Check if nickname is too long
                    if(nickname.isOverLimit()) {
                        plugin.messages.sendMessage(sender, "nickname-too-long",
                                mapOf(Pair("max", "${plugin.config.getInt(ConfigurationPath.N_MAX_LENGTH)}")))
                    }

                    // Set player's nickname
                    plugin.nicknameManager.setNickname(sender, nickname)
                    plugin.messages.sendMessage(sender, "your-nickname-set-to", mapOf(Pair("nickname", nickname)))
                } else if(args.size == 2) {
                    if(sender is Player && !sender.hasPermission(PermissionNodes.NICKNAME_OTHERS.node)) {
                        plugin.messages.sendMessage(sender, "no-permission", null)
                        return true
                    }

                    val player = p(args[0])
                    val nickname = c(args[1])

                    // Check if nickname is too long
                    if(nickname.isOverLimit()) {
                        plugin.messages.sendMessage(sender, "nickname-too-long",
                                mapOf(Pair("max", "${plugin.config.getInt(ConfigurationPath.N_MAX_LENGTH)}")))
                        return true
                    }

                    // Bail out if player is not online
                    if(player == null) {
                        plugin.messages.sendMessage(sender, "no-such-player", mapOf(Pair("player", args[0])))
                        return true
                    }

                    // Set nickname
                    plugin.nicknameManager.setNickname(player, nickname)
                    plugin.messages.sendMessage(sender, "player-nickname-set-to", mapOf(
                            Pair("nickname", nickname),
                            Pair("player", player.name)
                    ))
                } else {
                    plugin.messages.sendMessage(sender, "invalid-command-usage", mapOf(Pair("usage", "/$label ${
                        if(sender is Player)
                            "[player] <nickname>"
                        else
                            "<player> <nickname>"
                    }")))
                }
            }
            return true
        }
        return false
    }

    private fun String.isOverLimit(): Boolean {
        val length = if(plugin.config.getBool(ConfigurationPath.N_COUNT_COLORS_AS_WELL)) {
            this
        } else {
            ChatColor.stripColor(this)
        }.length
        return length > plugin.config.getInt(ConfigurationPath.N_MAX_LENGTH)
    }
}