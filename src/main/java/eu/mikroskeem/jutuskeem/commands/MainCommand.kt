package eu.mikroskeem.jutuskeem.commands

import eu.mikroskeem.jutuskeem.Main
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * @author Mark Vainomaa
 */
class MainCommand(private val plugin: Main) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(command.name == "jutuskeem") {
            plugin.reloadConfig()
            plugin.messages.reloadMessages()
            plugin.messages.sendMessage(sender, "configuration-reloaded", null)
        }
        return false
    }
}