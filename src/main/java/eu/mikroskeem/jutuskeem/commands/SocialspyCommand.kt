package eu.mikroskeem.jutuskeem.commands

import eu.mikroskeem.jutuskeem.Main
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Mark Vainomaa
 */
class SocialspyCommand(private val plugin: Main) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(sender !is Player) {
            sender.sendMessage("This is in-game command.")
            return true
        }
        if(command.name == "socialspy") {
            if(plugin.socialSpyEnabled.contains(sender)) {
                plugin.socialSpyEnabled.remove(sender)
                plugin.messages.sendMessage(sender, "socialspy-disabled", mapOf(Pair("player", sender.name)))
            } else {
                plugin.socialSpyEnabled.add(sender)
                plugin.messages.sendMessage(sender, "socialspy-enabled", mapOf(Pair("player", sender.name)))
            }
            return true
        }
        return false
    }
}