package com.example.versiontinmoli;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.Logger;

/**
 * PingEventListener intercepts ProxyPingEvent to modify the server version information
 * displayed in the server list. It replaces the default version with a custom version name
 * from the configuration while preserving all other ping response data.
 * 
 * Supports MiniMessage format for colors and formatting:
 * - Named colors: <red>, <gold>, <yellow>, <green>, <aqua>, <blue>, etc.
 * - RGB colors: <#FF5555>, <color:#55FF55>
 * - Gradients: <gradient:red:blue>text</gradient>
 * - Styles: <bold>, <italic>, <underlined>, <strikethrough>
 * - Legacy codes: §c, §6, etc.
 */
public class PingEventListener {
    private final ConfigLoader configLoader;
    private final Logger logger;
    private final MiniMessage miniMessage;
    private final LegacyComponentSerializer legacySerializer;

    /**
     * Constructs a new PingEventListener.
     *
     * @param configLoader the configuration loader to retrieve the custom version name
     * @param logger the logger instance for logging event processing
     */
    public PingEventListener(ConfigLoader configLoader, Logger logger) {
        this.configLoader = configLoader;
        this.logger = logger;
        this.miniMessage = MiniMessage.miniMessage();
        this.legacySerializer = LegacyComponentSerializer.legacySection();
    }

    /**
     * Handles ProxyPingEvent to modify the server version information.
     * This method is called when a client pings the proxy server.
     * Supports MiniMessage and legacy color codes.
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
            
            // Parse the version name to support colors and convert to plain text
            // Note: Velocity's ServerPing.Version only accepts String, not Component
            // The string can contain § color codes which will be displayed by the client
            String coloredVersionName = parseVersionNameToLegacy(customVersionName);
            
            // Get the original version to preserve the protocol number
            ServerPing.Version originalVersion = originalPing.getVersion();
            int protocolVersion = originalVersion != null ? originalVersion.getProtocol() : -1;
            
            // Create a new Version object with the custom version name
            ServerPing.Version newVersion = new ServerPing.Version(protocolVersion, coloredVersionName);
            
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

    /**
     * Parses the version name string to support MiniMessage and legacy color codes.
     * Converts to legacy § format for compatibility with Velocity's ServerPing.Version.
     *
     * @param versionName the version name string from config
     * @return a String with § color codes
     */
    private String parseVersionNameToLegacy(String versionName) {
        try {
            // Try to parse as MiniMessage format first
            if (versionName.contains("<") && versionName.contains(">")) {
                Component component = miniMessage.deserialize(versionName);
                return legacySerializer.serialize(component);
            }
            
            // Already in legacy format or plain text - return as is
            return versionName;
            
        } catch (Exception e) {
            // If parsing fails, return plain text
            logger.warn("Failed to parse version name '{}': {}. Using plain text.", versionName, e.getMessage());
            return versionName;
        }
    }
}
