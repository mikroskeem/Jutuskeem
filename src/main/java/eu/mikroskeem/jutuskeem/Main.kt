package eu.mikroskeem.jutuskeem

import com.google.inject.Injector
import eu.mikroskeem.jutuskeem.commands.MainCommand
import eu.mikroskeem.jutuskeem.listeners.ChatListener
import eu.mikroskeem.providerslib.api.Providers
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Mark Vainomaa
 */
class Main : JavaPlugin() {

    override fun onEnable() {
        /* Set plugin up */
        val injector : Injector
        try {
            injector = checkNotNull<RegisteredServiceProvider<Providers>>(
                    server.servicesManager.getRegistration(Providers::class.java))
                    .provider
                    .injector
        } catch (e: NullPointerException) {
            logger.severe(e.message)
            isEnabled = false
            return
        }

        /* Load and copy messages */
        saveDefaultConfig()
        config.options().copyDefaults(true)
        saveConfig()

        /* Register commands and listeners */
        getCommand("jutuskeem").executor = MainCommand(this)
        server.pluginManager.registerEvents(injector.getInstance<ChatListener>(ChatListener::class.java), this)
    }
}
