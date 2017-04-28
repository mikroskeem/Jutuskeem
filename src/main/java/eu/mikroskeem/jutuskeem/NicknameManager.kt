package eu.mikroskeem.jutuskeem

import com.google.inject.Inject
import org.bukkit.entity.Player
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * I should go hell for this...
 *
 * @author Mark Vainomaa
 */
class NicknameManager @Inject constructor(plugin: Main) {
    private val nicknamesPath : Path = Paths.get(plugin.dataFolder.absolutePath, "nicknames")

    fun getNickname(p: Player) : String? {
        val nicknamePath = Paths.get(nicknamesPath.toAbsolutePath().toString(), p.uniqueId.toString())
        if(Files.exists(nicknamePath)) {
            return Files.newBufferedReader(nicknamePath).use { it.readText() }
        }
        return null
    }

    fun setNickname(p: Player, n: String?) {
        val nicknamePath = Paths.get(nicknamesPath.toAbsolutePath().toString(), p.uniqueId.toString())
        Files.createDirectories(nicknamePath.parent)
        if(n != null) {
            Files.newOutputStream(nicknamePath).bufferedWriter().use { it.write("$n§r") }
        } else {
            if(Files.exists(nicknamePath))
                Files.delete(nicknamePath)
        }
        p.displayName = if(n == null) n else "$n§r"
    }
}