package eu.mikroskeem.jutuskeem;

import org.bukkit.ChatColor;

import java.util.Map;

public class Utils {
    public static String replaceParams(Map<String, String> map, String template) {
        return map.entrySet().stream().reduce(
            template,
            (s, e) -> s.replace("%" + e.getKey() + "%", e.getValue()),
            (s, s2) -> s
        );
    }

    public static String c(String c){
        return ChatColor.translateAlternateColorCodes('&', ""+c);
    }
}
