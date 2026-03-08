package com.example.versiontinmoli;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * PingEventListener intercepts ServerListPingEvent to modify the server version information
 * displayed in the server list. It replaces the default version with a custom version name
 * from the configuration while preserving all other ping response data.
 * 
 * Supports color codes:
 * - & codes: &c, &6, &a, etc. (recommended for Bukkit/Spigot)
 * - § codes: §c, §6, §a, etc. (legacy format)
 * - Formatting: &l (bold), &m (strikethrough), &n (underline), &o (italic), &r (reset)
 * 
 * Note: Version customization requires Paper or newer Spigot versions.
 */
public class PingEventListener implements Listener {
    private final ConfigLoader configLoader;
    private final Logger logger;
    private Method setVersionMethod;
    private boolean methodChecked = false;

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
     * Handles ServerListPingEvent to modify the server version information.
     * This method is called when a client pings the server.
     * Uses reflection to support both Spigot and Paper servers.
     * Supports & and § color codes.
     *
     * @param event the ServerListPingEvent containing the ping request and response
     */
    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        try {
            // Get the configured version name from ConfigLoader
            String customVersionName = configLoader.getVersionName();
            
            // Translate color codes (& to §)
            String coloredVersionName = translateColorCodes(customVersionName);
            
            // Try to find and use setVersion method (available in Paper and some Spigot versions)
            if (!methodChecked) {
                try {
                    setVersionMethod = event.getClass().getMethod("setVersion", String.class);
                    logger.info("setVersion method found - version customization with colors is supported");
                } catch (NoSuchMethodException e) {
                    logger.warning("setVersion method not available on this server. Version customization requires Paper or newer Spigot versions.");
                    setVersionMethod = null;
                }
                methodChecked = true;
            }
            
            // If method is available, use it
            if (setVersionMethod != null) {
                setVersionMethod.invoke(event, coloredVersionName);
            }
            
        } catch (Exception e) {
            // Error handling: log error
            logger.severe("Failed to modify server version: " + e.getMessage());
        }
    }

    /**
     * Translates color codes from & format to § format.
     * Also preserves existing § codes.
     *
     * @param text the text with & or § color codes
     * @return the text with § color codes
     */
    private String translateColorCodes(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // Use Bukkit's built-in color code translator
        // This converts & codes to § codes
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
