package eu.mikroskeem.jutuskeem.commands

import com.google.inject.Inject
import eu.mikroskeem.jutuskeem.Main
import eu.mikroskeem.jutuskeem.PermissionNodes
import eu.mikroskeem.jutuskeem.Utils.c
import eu.mikroskeem.jutuskeem.Utils.p
import eu.mikroskeem.providerslib.api.Chat
import eu.mikroskeem.providerslib.api.Permissions
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Mark Vainomaa
 */
class NicknameCommand : CommandExecutor {
    @Inject private lateinit var plugin : Main
    @Inject private lateinit var permissions : Permissions

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
                    if(sender is Player && !permissions.playerHas(sender, PermissionNodes.CLEARNICKNAME.node)) {
                        plugin.messages.sendMessage(sender, "no-permission", null)
                        return true
                    }
                    val player = p(args[0])
                    if(player != null) {
                        plugin.nicknameManager.setNickname(player, null)
                        plugin.messages.sendMessage(sender, "nickname-cleared", mapOf(Pair("player", player.name)))
                    } else {
                        plugin.messages.sendMessage(sender, "no-such-player", mapOf(Pair("player", args[0])))
                    }
                } else {
                    plugin.messages.sendMessage(sender, "invalid-command-usage",
                            mapOf(Pair("usage", "/$label ${if(sender is Player) "[player]" else "<player>"}")))
                }
            } else {
                if(args.size == 1) {
                    val nickname = c(args[0])
                    val maxLength = plugin.config.getInt("nickname.length-limit", 16)
                    if(ChatColor.stripColor(nickname).length > maxLength) {
                        plugin.messages.sendMessage(sender, "nickname-too-long", mapOf(Pair("max", "$maxLength")))
                        return true
                    }
                    if(sender is Player) {
                        plugin.nicknameManager.setNickname(sender, nickname)
                        plugin.messages.sendMessage(sender, "your-nickname-set-to", mapOf(Pair("nickname", nickname)))
                    } else {
                        plugin.messages.sendMessage(sender, "invalid-command-usage", mapOf(Pair("usage", "/$label <player> <nickname>")))
                    }
                } else if(args.size == 2) {
                    val player = p(args[0])
                    val nickname = c(args[1])
                    val maxLength = plugin.config.getInt("nickname.length-limit", 16)
                    if(ChatColor.stripColor(nickname).length > maxLength) {
                        plugin.messages.sendMessage(sender, "nickname-too-long", mapOf(Pair("max", "$maxLength")))
                        return true
                    }
                    if(player != null) {
                        plugin.nicknameManager.setNickname(player, nickname)
                        plugin.messages.sendMessage(sender, "player-nickname-set-to", mapOf(
                                Pair("nickname", nickname),
                                Pair("player", player.name)
                        ))
                    } else {
                        plugin.messages.sendMessage(sender, "no-such-player", mapOf(Pair("player", args[0])))
                    }
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
}