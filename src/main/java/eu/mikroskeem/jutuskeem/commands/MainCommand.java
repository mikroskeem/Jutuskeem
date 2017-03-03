package eu.mikroskeem.jutuskeem.commands;

import com.google.inject.Inject;
import eu.mikroskeem.jutuskeem.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
    @Inject private Main plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equals("jutuskeem")){
            plugin.reloadConfig();
            commandSender.sendMessage("Configuration reloaded");
        }
        return false;
    }
}
