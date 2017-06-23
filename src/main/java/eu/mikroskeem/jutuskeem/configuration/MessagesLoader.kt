package eu.mikroskeem.jutuskeem.configuration

import eu.mikroskeem.jutuskeem.Main
import eu.mikroskeem.jutuskeem.Utils.c
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Mark Vainomaa
 */
class MessagesLoader(private var plugin: Main) {
    private val messagesPath : Path = Paths.get(plugin.dataFolder.absolutePath, "messages.yml")
    private val yamlConfiguration = YamlConfiguration()

    init {
        reloadMessages()
    }

    fun reloadMessages() {
        Files.createDirectories(messagesPath.parent)

        // Set up default configuration
        if(!Files.exists(messagesPath)) Files.copy(plugin.getResource("messages.yml"), messagesPath)
        yamlConfiguration.load(Files.newBufferedReader(messagesPath))
        yamlConfiguration.defaults = YamlConfiguration.loadConfiguration(plugin.getResource("messages.yml").bufferedReader())
        yamlConfiguration.options().copyDefaults(true)
        yamlConfiguration.save(messagesPath.toFile())
    }

    fun sendMessage(receiver: CommandSender, message: String, placeholders: Map<String, String>?) {
        receiver.sendMessage(c(yamlConfiguration.getString(message, ""), placeholders ?: mapOf()))
    }
}