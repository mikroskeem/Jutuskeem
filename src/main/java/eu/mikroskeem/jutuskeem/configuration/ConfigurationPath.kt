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