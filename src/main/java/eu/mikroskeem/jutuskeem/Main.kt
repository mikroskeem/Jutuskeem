package eu.mikroskeem.jutuskeem

import eu.mikroskeem.jutuskeem.commands.MainCommand
import eu.mikroskeem.jutuskeem.commands.NicknameCommand
import eu.mikroskeem.jutuskeem.commands.PrivateMessageCommand
import eu.mikroskeem.jutuskeem.commands.SocialspyCommand
import eu.mikroskeem.jutuskeem.configuration.ConfigurationWrapper
import eu.mikroskeem.jutuskeem.configuration.MessagesLoader
import eu.mikroskeem.jutuskeem.hooks.VaultHook
import eu.mikroskeem.jutuskeem.listeners.ChatListener
import eu.mikroskeem.jutuskeem.listeners.PlayerListener
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Mark Vainomaa
 */
class Main : JavaPlugin() {
    val lastRepliedTo = HashMap<Player, Player>()
    val socialSpyEnabled = HashSet<Player>()
    lateinit var messages : MessagesLoader
    lateinit var nicknameManager : NicknameManager
    lateinit var config: ConfigurationWrapper
    lateinit var vaultHook: VaultHook.VaultWrapper

    override fun onEnable() {
        /* Load and copy messages */
        config = ConfigurationWrapper(this)
        saveDefaultConfig()
        getConfig().options().copyDefaults(true)
        saveConfig()

        /* Set up Vault Hook */
        vaultHook = VaultHook().vaultHook

        /* Set up nickname manager and messages */
        nicknameManager = NicknameManager(this)
        messages = MessagesLoader(this)

        /* Register commands and listeners */
        getCommand("jutuskeem").executor = MainCommand(this)
        getCommand("nickname").executor = NicknameCommand(this)
        getCommand("socialspy").executor = SocialspyCommand(this)
        getCommand("tell").executor = PrivateMessageCommand(this)
        server.pluginManager.registerEvents(ChatListener(this), this)
        server.pluginManager.registerEvents(PlayerListener(this), this)
    }
}
