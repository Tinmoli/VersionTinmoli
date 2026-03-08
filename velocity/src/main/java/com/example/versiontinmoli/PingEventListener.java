package com.example.versiontinmoli;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import org.slf4j.Logger;

/**
 * PingEventListener intercepts ProxyPingEvent to modify the server version information
 * displayed in the server list. It replaces the default version with a custom version name
 * from the configuration while preserving all other ping response data.
 */
public class PingEventListener {
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
    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {
        // Get the current ServerPing object from the event
        ServerPing originalPing = event.getPing();
        
        // Handle null ServerPing case - skip processing
        if (originalPing == null) {
            logger.warn("Received ProxyPingEvent with null ServerPing, skipping");
            return;
        }
        
        try {
            // Get the configured version name from ConfigLoader
            String customVersionName = configLoader.getVersionName();
            
            // Get the original version to preserve the protocol number
            ServerPing.Version originalVersion = originalPing.getVersion();
            int protocolVersion = originalVersion != null ? originalVersion.getProtocol() : -1;
            
            // Create a new Version object with the custom version name
            ServerPing.Version newVersion = new ServerPing.Version(protocolVersion, customVersionName);
            
            // Build a new ServerPing using the builder, preserving all original fields
            ServerPing.Builder builder = originalPing.asBuilder();
            builder.version(newVersion);
            
            // Set the modified ServerPing back to the event
            event.setPing(builder.build());
            
        } catch (Exception e) {
            // Error handling: catch Builder exceptions and use original ServerPing
            logger.error("Failed to build modified ServerPing: {}. Using original ServerPing.", e.getMessage());
            // No need to set anything - the original ping remains in the event
        }
    }
}
