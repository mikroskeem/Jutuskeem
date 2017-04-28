package eu.mikroskeem.jutuskeem.commands

import com.google.inject.Inject
import eu.mikroskeem.jutuskeem.Main
import eu.mikroskeem.providerslib.api.Permissions
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * @author Mark Vainomaa
 */
class MainCommand constructor() : CommandExecutor {
    @Inject private lateinit var plugin : Main

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(command.name == "jutuskeem") {
            plugin.reloadConfig()
            plugin.messages.reloadMessages()
            plugin.messages.sendMessage(sender, "configuration-reloaded", null)
        }
        return false
    }
}