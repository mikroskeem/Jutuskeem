package eu.mikroskeem.jutuskeem

import com.google.inject.Injector
import com.google.inject.Module
import eu.mikroskeem.jutuskeem.commands.MainCommand
import eu.mikroskeem.jutuskeem.commands.NicknameCommand
import eu.mikroskeem.jutuskeem.commands.PrivateMessageCommand
import eu.mikroskeem.jutuskeem.commands.SocialspyCommand
import eu.mikroskeem.jutuskeem.listeners.ChatListener
import eu.mikroskeem.jutuskeem.listeners.PlayerListener
import eu.mikroskeem.providerslib.api.Providers
import org.bukkit.entity.Player
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Mark Vainomaa
 */
class Main : JavaPlugin() {
    val lastRepliedTo = HashMap<Player, Player>()
    val socialSpyEnabled = HashSet<Player>()
    lateinit var messages : MessagesLoader
    lateinit var nicknameManager : NicknameManager

    override fun onEnable() {
        /* Set plugin up */
        val injector : Injector
        try {
            injector = checkNotNull<RegisteredServiceProvider<Providers>>(
                    server.servicesManager.getRegistration(Providers::class.java))
                    .provider
                    .injector
                    .createChildInjector(Module { binder -> binder.bind(Main::class.java).toInstance(this) })
        } catch (e: NullPointerException) {
            logger.severe(e.message)
            isEnabled = false
            return
        }

        /* Load and copy messages */
        saveDefaultConfig()
        config.options().copyDefaults(true)
        saveConfig()

        /* Set up nickname manager and messages */
        nicknameManager = injector.getInstance(NicknameManager::class.java)
        messages = injector.getInstance(MessagesLoader::class.java)

        /* Register commands and listeners */
        getCommand("jutuskeem").executor = injector.getInstance(MainCommand::class.java)
        getCommand("nickname").executor = injector.getInstance(NicknameCommand::class.java)
        getCommand("socialspy").executor = injector.getInstance(SocialspyCommand::class.java)
        getCommand("tell").executor = injector.getInstance(PrivateMessageCommand::class.java)
        server.pluginManager.registerEvents(injector.getInstance(ChatListener::class.java), this)
        server.pluginManager.registerEvents(injector.getInstance(PlayerListener::class.java), this)
    }
}
