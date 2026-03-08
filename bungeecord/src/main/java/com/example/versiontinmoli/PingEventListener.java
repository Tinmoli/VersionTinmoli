package com.example.versiontinmoli;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.logging.Logger;

/**
 * PingEventListener intercepts ProxyPingEvent to modify the server version information
 * displayed in the server list. It replaces the default version with a custom version name
 * from the configuration while preserving all other ping response data.
 */
public class PingEventListener implements Listener {
    private final ConfigLoader configLoader;
    private final Logger logger;

    /**
     * Constructs a new PingEventListener.
     *
     * @param configLoader the configuration loader to retrieve the custom version name
     * @param logger the logger instance for logging event processing
     */
    public PingEventListener(ConfigLoader configLoader, Logger logger) {
        this.configLoader = configLoader;
        this.logger = logger;
    }

    /**
     * Handles ProxyPingEvent to modify the server version information.
     * This method is called when a client pings the proxy server.
     *
     * @param event the ProxyPingEvent containing the ping request and response
     */
    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        // Get the current ServerPing object from the event
        ServerPing response = event.getResponse();
        
        // Handle null ServerPing case - skip processing
        if (response == null) {
            logger.warning("Received ProxyPingEvent with null ServerPing, skipping");
            return;
        }
        
        try {
            // Get the configured version name from ConfigLoader
            String customVersionName = configLoader.getVersionName();
            
            // Get the original version to preserve the protocol number
            ServerPing.Protocol originalVersion = response.getVersion();
            int protocolVersion = originalVersion != null ? originalVersion.getProtocol() : -1;
            
            // Create a new Protocol object with the custom version name
            ServerPing.Protocol newVersion = new ServerPing.Protocol(customVersionName, protocolVersion);
            
            // Set the new version to the response
            response.setVersion(newVersion);
            
        } catch (Exception e) {
            // Error handling: log error and use original ServerPing
            logger.severe("Failed to modify ServerPing: " + e.getMessage() + ". Using original ServerPing.");
            // No need to do anything - the original response remains
        }
    }
}
