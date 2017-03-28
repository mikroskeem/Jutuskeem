package eu.mikroskeem.jutuskeem.listeners;

import com.google.inject.Inject;
import eu.mikroskeem.jutuskeem.Main;
import eu.mikroskeem.jutuskeem.Utils;
import eu.mikroskeem.providerslib.api.Chat;
import eu.mikroskeem.providerslib.api.Permissions;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import static eu.mikroskeem.jutuskeem.Utils.c;

public class ChatListener implements Listener {
    @Inject private Main plugin;
    @Inject private Permissions permissions;
    @Inject private Chat chat;

    @EventHandler(ignoreCancelled = true)
    public void on(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        Set<Player> recipients = clearRecipients(event);
        String message = event.getMessage();
        String format = plugin.getConfig().getString("chat-format", "<%player_name%> %message%");
        String finalRawMessage = Utils.replaceParams(new HashMap<String, String>(){{
            put("player_name", player.getName());
            put("player_displayname", c(player.getDisplayName()));
            put("message", permissions.playerHas(player, "jutuskeem.chatcolors")?c(message):message);
            put("player_prefix", c(chat.getPrefix(player)));
            put("player_suffix", c(chat.getSuffix(player)));
        }}, c(format));
        BaseComponent[] components = TextComponent.fromLegacyText(finalRawMessage);
        TextComponent finalMessage = new TextComponent();
        for (BaseComponent component : components) {
            finalMessage.addExtra(component);
        }
        recipients.forEach(p -> p.sendMessage(finalMessage));
    }

    private Set<Player> clearRecipients(AsyncPlayerChatEvent event){
        /* Try to clean, and if it fails, cancel event */
        try {
            Set<Player> recipients = new LinkedHashSet<>(event.getRecipients());
            event.getRecipients().clear();
            return recipients;
        } catch (UnsupportedOperationException e){
            event.setCancelled(true);
            return event.getRecipients();
        }
    }
}
