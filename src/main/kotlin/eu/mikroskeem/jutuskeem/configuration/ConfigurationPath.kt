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

/**
 * @author Mark Vainomaa
 */
enum class ConfigurationPath(val path: String, val default: Any) {
    // Chat formatting
    F_CHAT("format.chat", "<%player_name%> %message%"),
    F_PM_SEND_TO_PLAYER("format.privatemessage-send-to-player", "&eyou &7-> &a%receiver_name%&7: &f%message%"),
    F_PM_RECEIVE_FROM_PLAYER("format.privatemessage-receive-from-player", "&c%sender_name% &7-> &eyou&7: &f%message%"),
    F_SOCIALSPY("format.socialspy", "&7[&4MSG&7]&f %sender_name% &7-> %receiver_name%&7: &f%message%"),

    // Private messaging
    PM_REPLY_TO_FIRST("private-messages.reply-to-first-sender-not-last", false),

    // Nicknames
    N_MAX_LENGTH("nickname.length-limit", 16),
    N_COUNT_COLORS_AS_WELL("nickname.count-colors-in-limit", false),

    // Worlds
    W_PER_WORLD_CHAT("worlds.per-world-enabled", false),
    //W_GROUPS("worlds.world-groups", listOf<Map<String, String>>())
}