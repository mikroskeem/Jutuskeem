package eu.mikroskeem.jutuskeem.commands

import com.google.inject.Inject
import eu.mikroskeem.jutuskeem.Main
import eu.mikroskeem.jutuskeem.PermissionNodes
import eu.mikroskeem.jutuskeem.Utils.c
import eu.mikroskeem.jutuskeem.Utils.p
import eu.mikroskeem.providerslib.api.Chat
import eu.mikroskeem.providerslib.api.Permissions
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Mark Vainomaa
 */
class PrivateMessageCommand : CommandExecutor {
    @Inject private lateinit var plugin : Main
    @Inject private lateinit var permissions : Permissions
    @Inject private lateinit var chat : Chat

    val defaultToPlayerFormat = "&eyou &7-> &a%receiver_name%&7: &f%message%"
    val defaultFromPlayerFormat = "&c%sender_name% &7-> &eyou&7: &f%message%"
    val defaultSocialSpyFormat = "&7[&4MSG&7]&f %sender_name% &7-> %receiver_name%&7: &f%message%"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(sender !is Player) {
            sender.sendMessage("Sending message as console is not implemented yet. Use '/minecraft:tell'")
            return true
        }
        if (command.name == "tell") {
            if (label == "r" || label == "reply") {
                if (args.isNotEmpty()) {
                    if(plugin.lastRepliedTo.containsKey(sender)) {
                        plugin.lastRepliedTo.computeIfPresent(sender, { _, receiver ->
                            sendMsg(sender, receiver, args.joinToString(" "))
                            receiver
                        })
                    } else {
                        plugin.messages.sendMessage(sender, "nobody-to-reply-to", null)
                    }
                } else {
                    plugin.messages.sendMessage(sender, "invalid-command-usage", mapOf(Pair("usage", "/$label <message>")))
                }
            } else {
                if(args.isNotEmpty() && args.size >= 2) {
                    val receiver = p(args[0])
                    if(receiver != null) {
                        if(sender == receiver) {
                            plugin.messages.sendMessage(sender, "cannot-message-yourself", null)
                            return true
                        }
                        plugin.lastRepliedTo.compute(sender, { _, _ ->
                            sendMsg(sender, receiver, args.sliceArray(1..(args.size-1)).joinToString(" "))
                            receiver
                        })
                    } else {
                        plugin.messages.sendMessage(sender, "no-such-player", mapOf(Pair("player", args[0])))
                    }
                } else {
                    plugin.messages.sendMessage(sender, "invalid-command-usage",
                            mapOf(Pair("usage", "/$label <receiver> <message>")))
                }
            }
            return true
        }
        return false
    }

    private fun sendMsg(sender: Player, receiver: Player, message: String) {
        // Build placeholders
        val params = mapOf(
                Pair("sender_name", sender.name),
                Pair("receiver_name", receiver.name),
                Pair("sender_displayname", c(sender.name)),
                Pair("receiver_displayname", c(receiver.name)),
                Pair("sender_prefix", c(chat.getPrefix(sender) ?: "")),
                Pair("receiver_prefix", c(chat.getPrefix(receiver) ?: "")),
                Pair("sender_suffix", c(chat.getSuffix(sender) ?: "")),
                Pair("receiver_suffix", c(chat.getSuffix(receiver) ?: "")),
                Pair("message", if (permissions.playerHas(sender, PermissionNodes.COLORS.node)) c(message) else message)
        )
        val senderFormat = plugin.config.getString("format.privatemessage-send-to-player", defaultToPlayerFormat)
        val receiverFormat = plugin.config.getString("format.privatemessage-receive-from-player", defaultFromPlayerFormat)
        val ssFormat = plugin.config.getString("format.socialspy", defaultSocialSpyFormat)
        val ssMessage = c(ssFormat, params)
        plugin.socialSpyEnabled.forEach {
            if(it == sender || it == receiver) return@forEach
            it.sendMessage(ssMessage)
        }
        sender.sendMessage(c(senderFormat, params))
        receiver.sendMessage(c(receiverFormat, params))
    }
}