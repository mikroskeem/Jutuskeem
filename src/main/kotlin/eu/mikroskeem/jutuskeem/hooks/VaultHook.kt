/*
 * This file is part of project Jutuskeem, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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