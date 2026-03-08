package com.example.versiontinmoli;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * VersionTinmoli is a lightweight Velocity plugin that customizes the version name
 * displayed in the server list. It intercepts ProxyPingEvent to replace the default version
 * information with a custom version name from the configuration.
 */
@Plugin(
    id = "versiontinmoli",
    name = "VersionTinmoli",
    version = "1.0.1",
    authors = {"Author"}
)
public class VersionTinmoli {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private ConfigLoader configLoader;

    /**
     * Constructs the main plugin class with dependency injection.
     * This constructor is called by Velocity's dependency injection system.
     *
     * @param server the ProxyServer instance
     * @param logger the Logger instance for this plugin
     * @param dataDirectory the plugin's data directory path
     */
    @Inject
    public VersionTinmoli(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

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
     * Handles the proxy initialization event.
     * This method is called when the proxy server is initialized.
     * It initializes the ConfigLoader, loads the configuration, creates and registers
     * the PingEventListener, and logs the startup message.
     *
     * @param event the ProxyInitializeEvent
     */
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Initialize ConfigLoader instance
        configLoader = new ConfigLoader(dataDirectory, logger);

        // Load configuration from config.toml
        configLoader.load();

        // Create PingEventListener instance
        PingEventListener pingEventListener = new PingEventListener(configLoader, logger);

        // Register the event listener with the EventManager
        server.getEventManager().register(this, pingEventListener);

        // Log startup message
        logger.info("VersionTinmoli plugin has been enabled");
    }

    /**
     * Handles the proxy shutdown event.
     * This method is called when the proxy server is shutting down.
     * It logs a shutdown message.
     *
     * @param event the ProxyShutdownEvent
     */
    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        // Log shutdown message
        logger.info("VersionTinmoli plugin has been disabled");
    }

}
