package com.example.versiontinmoli;

import net.md_5.bungee.api.plugin.Plugin;

import java.nio.file.Path;

/**
 * VersionTinmoli is a lightweight BungeeCord plugin that customizes the version name
 * displayed in the server list. It intercepts ServerPing events to replace the default version
 * information with a custom version name from the configuration.
 */
public class VersionTinmoli extends Plugin {
    private ConfigLoader configLoader;

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
     * Called when the plugin is enabled.
     * This method initializes the ConfigLoader, loads the configuration, and registers
     * the PingEventListener.
     */
    @Override
    public void onEnable() {
        // Get the plugin's data directory
        Path dataDirectory = getDataFolder().toPath();

        // Initialize ConfigLoader instance
        configLoader = new ConfigLoader(dataDirectory, getLogger());

        // Load configuration from config.toml
        configLoader.load();

        // Create and register PingEventListener
        PingEventListener pingEventListener = new PingEventListener(configLoader, getLogger());
        getProxy().getPluginManager().registerListener(this, pingEventListener);

        // Log startup message
        getLogger().info("VersionTinmoli plugin has been enabled");
    }

    /**
     * Called when the plugin is disabled.
     * This method logs a shutdown message.
     */
    @Override
    public void onDisable() {
        // Log shutdown message
        getLogger().info("VersionTinmoli plugin has been disabled");
    }
}
