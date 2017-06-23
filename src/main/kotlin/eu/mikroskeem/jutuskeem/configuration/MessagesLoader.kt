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