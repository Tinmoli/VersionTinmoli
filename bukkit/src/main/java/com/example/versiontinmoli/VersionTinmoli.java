package com.example.versiontinmoli;

import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

/**
 * VersionTinmoli is a lightweight Bukkit plugin that customizes the version name
 * displayed in the server list. It intercepts ServerListPing events to replace the default version
 * information with a custom version name from the configuration.
 */
public class VersionTinmoli extends JavaPlugin {
    private ConfigLoader configLoader;
    private LanguageLoader languageLoader;

    /**
     * Returns the ConfigLoader instance.
     * This method provides access to the configuration loader for other components.
     *
     * @return the ConfigLoader instance, or null if not yet initialized
     */
    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    /**
     * Returns the LanguageLoader instance.
     * This method provides access to the language loader for other components.
     *
     * @return the LanguageLoader instance, or null if not yet initialized
     */
    public LanguageLoader getLanguageLoader() {
        return languageLoader;
    }

    /**
     * Called when the plugin is enabled.
     * This method initializes the ConfigLoader, loads the configuration, and registers
     * the PingEventListener.
     */
    @Override
    public void onEnable() {
        // Get the plugin's data directory
        Path dataDirectory = getDataFolder().toPath();

        // Initialize LanguageLoader instance first (with default language)
        languageLoader = new LanguageLoader(dataDirectory, getLogger());
        languageLoader.load("en_US"); // Load default language first
        
        // Initialize ConfigLoader instance
        configLoader = new ConfigLoader(dataDirectory, getLogger(), languageLoader);

        // Load configuration from config.yml
        configLoader.load();

        // Reload language file based on config
        languageLoader.load(configLoader.getLanguage());

        // Create and register PingEventListener
        PingEventListener pingEventListener = new PingEventListener(configLoader, getLogger());
        getServer().getPluginManager().registerEvents(pingEventListener, this);

        // Register command
        CommandHandler commandHandler = new CommandHandler(this);
        getCommand("vt").setExecutor(commandHandler);
        getCommand("vt").setTabCompleter(commandHandler);

        // Log startup message
        getLogger().info(languageLoader.getMessage("plugin.enabled"));
        
        // Check for updates asynchronously (if enabled)
        if (configLoader.isCheckUpdates()) {
            UpdateChecker updateChecker = new UpdateChecker("1.0.2", getLogger());
            updateChecker.checkForUpdates();
        }
    }

    /**
     * Called when the plugin is disabled.
     * This method logs a shutdown message.
     */
    @Override
    public void onDisable() {
        // Log shutdown message
        getLogger().info(languageLoader.getMessage("plugin.disabled"));
    }
}
