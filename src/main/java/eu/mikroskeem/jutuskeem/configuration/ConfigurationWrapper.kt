package eu.mikroskeem.jutuskeem.configuration

import eu.mikroskeem.jutuskeem.Main

/**
 * @author Mark Vainomaa
 */
class ConfigurationWrapper(private val main: Main) {
    fun getString(path: ConfigurationPath): String =
            main.getConfig().getString(path.path, path.default as String)

    fun getInt(path: ConfigurationPath): Int =
            main.getConfig().getInt(path.path, path.default as Int)

    fun getBool(path: ConfigurationPath): Boolean =
            main.getConfig().getBoolean(path.path, path.default as Boolean)
}