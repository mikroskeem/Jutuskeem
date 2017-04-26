package eu.mikroskeem.jutuskeem.commands

import eu.mikroskeem.jutuskeem.Main
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * @author Mark Vainomaa
 */
class MainCommand(private val plugin : Main) : CommandExecutor {
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if(command.name == "jutuskeem") {
            plugin.reloadConfig()
            commandSender.sendMessage("Configuration reloaded")
        }
        return false
    }
}