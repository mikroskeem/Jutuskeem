package eu.mikroskeem.jutuskeem.commands

import eu.mikroskeem.jutuskeem.Main
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * @author Mark Vainomaa
 */
class IgnoreCommand(private val plugin: Main) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(command.name == "ignore") {

            return true
        }
        return false
    }
}