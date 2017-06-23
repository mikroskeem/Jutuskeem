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

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * @author Mark Vainomaa
 */
object Utils {
    fun replaceParams(map: Map<String, String>, template: String): String {
        return map.entries.stream().reduce(
                template,
                { s, e -> s.replace("%" + e.key + "%", e.value) }
        ) { s, _ -> s }
    }

    fun c(c: String?): String {
        return ChatColor.translateAlternateColorCodes('&', c?:"")
    }

    fun c(c: String, p: Map<String, String>): BaseComponent {
        val components = TextComponent.fromLegacyText(Utils.replaceParams(p, c(c)))
        val finalMessage = TextComponent()
        components.forEach { finalMessage.addExtra(it) }
        return finalMessage
    }

    fun p(n: String) : Player? {
        return Bukkit.getOnlinePlayers().find { it.name == n }
    }
}