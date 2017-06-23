package eu.mikroskeem.jutuskeem.hooks

import eu.mikroskeem.shuriken.reflect.Reflect
import eu.mikroskeem.shuriken.reflect.wrappers.ClassWrapper
import eu.mikroskeem.shuriken.reflect.wrappers.TypeWrapper
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.reflect.Proxy

/**
 * @author Mark Vainomaa
 */
class VaultHook {
    private val CHAT_CLASS = "net.milkbowl.vault.chat.Chat"
    private val INTF_ARRAY = Array(1, { _ -> VaultWrapper::class.java })
    val vaultHook: VaultWrapper

    init {
        val vaultPluginLoader: ClassLoader? = Bukkit.getPluginManager().getPlugin("Vault")?.javaClass?.classLoader
        val chatServiceClass: ClassWrapper<*>? = Reflect.getClass(CHAT_CLASS, vaultPluginLoader).orElse(null)
        val provider: Any? = Bukkit.getServer().servicesManager.getRegistration(chatServiceClass?.wrappedClass)?.provider

        if(provider != null) {
            val providerWrapper = Reflect.wrapInstance(provider)
            vaultHook = Proxy.newProxyInstance(VaultHook::class.java.classLoader, INTF_ARRAY) { _, method, args ->
                when(method.name) {
                    "getPrefix" -> providerWrapper.invokeMethod("getPlayerPrefix", String::class.java, TypeWrapper.of(
                            Player::class.java,
                            args[0]
                    ))
                    "getSuffix" -> providerWrapper.invokeMethod("getPlayerSuffix", String::class.java, TypeWrapper.of(
                            Player::class.java,
                            args[0]
                    ))
                    else -> null
                }
            } as VaultWrapper
        } else {
            println("Vault Chat class or provider is not available! Make sure Vault " +
                    "and supported permissions plugin is installed!")
            vaultHook = Proxy.newProxyInstance(VaultHook::class.java.classLoader, INTF_ARRAY) { _, _, _ ->
                null
            } as VaultWrapper
        }
    }

    interface VaultWrapper {
        fun getPrefix(player: Player): String?
        fun getSuffix(player: Player): String?
    }
}