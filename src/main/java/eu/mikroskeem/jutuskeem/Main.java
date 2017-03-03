package eu.mikroskeem.jutuskeem;

import com.google.inject.Injector;
import eu.mikroskeem.jutuskeem.commands.MainCommand;
import eu.mikroskeem.jutuskeem.listeners.ChatListener;
import eu.mikroskeem.providerslib.api.Providers;
import org.bukkit.plugin.java.JavaPlugin;

import static com.google.common.base.Preconditions.checkNotNull;

public class Main extends JavaPlugin {
    private Injector injector;

    @Override
    public void onEnable() {
        /* Set plugin up */
        try {
            injector = checkNotNull(
                    getServer().getServicesManager().getRegistration(Providers.class),
                    "Failed to hook into ProvidersLib!"
            ).getProvider().getInjector().createChildInjector(binder->{
                binder.bind(Main.class).toInstance(this);
            });
        } catch (NullPointerException e) {
            getLogger().severe(e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* Load and copy messages */
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        /* Register commands and listeners */
        getCommand("jutuskeem").setExecutor(injector.getInstance(MainCommand.class));
        getServer().getPluginManager().registerEvents(injector.getInstance(ChatListener.class), this);
    }
}
