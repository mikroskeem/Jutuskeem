package eu.mikroskeem.jutuskeem

/**
 * @author Mark Vainomaa
 */
enum class PermissionNodes(val node: String) {
    RELOAD("jutuskeem.use"),
    COLORS("jutuskeem.chatcolors"),
    NICKNAME("jutuskeem.nickname"),
    CLEARNICKNAME("jutuskeem.nickname.clearothers"),
    TELL("jutuskeem.tell"),
    SOCIALSPY("jutuskeem.socialspy");
}