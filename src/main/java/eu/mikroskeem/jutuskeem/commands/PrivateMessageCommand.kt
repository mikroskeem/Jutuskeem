package eu.mikroskeem.jutuskeem.commands

import eu.mikroskeem.jutuskeem.Main
import eu.mikroskeem.jutuskeem.PermissionNodes
import eu.mikroskeem.jutuskeem.Utils.c
import eu.mikroskeem.jutuskeem.Utils.p
import eu.mikroskeem.jutuskeem.configuration.ConfigurationPath
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Mark Vainomaa
 */
class PrivateMessageCommand(private val plugin : Main) : CommandExecutor {
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
                            if(!receiver.isOnline) {
                                plugin.messages.sendMessage(sender, "nobody-to-reply-to", null)
                                return@computeIfPresent null
                            }
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
                            if(plugin.config.getBool(ConfigurationPath.PM_REPLY_TO_FIRST)) {
                                plugin.lastRepliedTo.computeIfAbsent(receiver, { _ -> sender })
                            } else {
                                plugin.lastRepliedTo.compute(receiver, { _, _ -> sender })
                            }
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
                Pair("sender_prefix", sender.getPrefix()),
                Pair("receiver_prefix", receiver.getPrefix()),
                Pair("sender_suffix", sender.getSuffix()),
                Pair("receiver_suffix", receiver.getSuffix()),
                Pair("message", if(sender.hasPermission(PermissionNodes.COLORS.node)) c(message) else message)
        )
        val senderFormat = plugin.config.getString(ConfigurationPath.F_PM_SEND_TO_PLAYER)
        val receiverFormat = plugin.config.getString(ConfigurationPath.F_PM_RECEIVE_FROM_PLAYER)
        val ssFormat = plugin.config.getString(ConfigurationPath.F_SOCIALSPY)
        val ssMessage = c(ssFormat, params)
        plugin.socialSpyEnabled.forEach {
            if(it == sender || it == receiver) return@forEach
            it.sendMessage(ssMessage)
        }
        sender.sendMessage(c(senderFormat, params))
        receiver.sendMessage(c(receiverFormat, params))
    }

    private fun Player.getPrefix(): String = c(plugin.vaultHook.getPrefix(player)?: "")
    private fun Player.getSuffix(): String = c(plugin.vaultHook.getSuffix(player)?: "")
}