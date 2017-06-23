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
