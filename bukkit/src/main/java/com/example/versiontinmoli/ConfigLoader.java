package com.example.versiontinmoli;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * ConfigLoader is responsible for loading and parsing the plugin's YAML configuration file.
 * It handles configuration file creation, parsing, and error recovery with sensible defaults.
 */
public class ConfigLoader {
    private final Path configPath;
    private final Logger logger;
    private final LanguageLoader languageLoader;
    private String versionName;
    private String language;
    private boolean checkUpdates;

    /**
     * Constructs a new ConfigLoader.
     *
     * @param dataDirectory the plugin's data directory where config.yml is located
     * @param logger the logger instance for logging configuration operations
     * @param languageLoader the language loader for localized messages
     */
    public ConfigLoader(Path dataDirectory, Logger logger, LanguageLoader languageLoader) {
        this.configPath = dataDirectory.resolve("config.yml");
        this.logger = logger;
        this.languageLoader = languageLoader;
        this.versionName = "Minecraft 1.20.1-1.21.11"; // Default value
        this.language = "en_US"; // Default language
        this.checkUpdates = true; // Default: check for updates
    }

    /**
     * Loads the configuration from config.yml.
     * If the file doesn't exist, creates a default configuration.
     * If parsing fails, falls back to the default version name.
     */
    public void load() {
        try {
            // Check if config file exists
            if (!Files.exists(configPath)) {
                createDefaultConfig();
                return;
            }

            // Load and parse the YAML file
            FileConfiguration config = YamlConfiguration.loadConfiguration(configPath.toFile());
            
            // Parse language field
            if (config.contains("language")) {
                language = config.getString("language", "en_US");
            }
            
            // Parse check_updates field
            if (config.contains("check_updates")) {
                checkUpdates = config.getBoolean("check_updates", true);
            }
            
            // Parse version_name field with type checking
            if (!config.contains("version_name")) {
                logger.warning(languageLoader.getMessage("config.field_not_found"));
                versionName = "Minecraft 1.20.1-1.21.11";
            } else {
                versionName = config.getString("version_name", "Minecraft 1.20.1-1.21.11");
                logger.info(languageLoader.getMessage("config.loaded", versionName));
            }
        } catch (Exception e) {
            logger.severe(languageLoader.getMessage("config.parse_failed", e.getMessage()));
            versionName = "Minecraft 1.20.1-1.21.11";
        }
    }

    /**
     * Creates a default config.yml file with the default version name.
     */
    private void createDefaultConfig() {
        try {
            // Ensure the data directory exists
            Files.createDirectories(configPath.getParent());
            
            // Create default configuration content (comments in English)
            String defaultConfig = "# VersionTinmoli Configuration File\n" +
                "# Customize the version name displayed in the server list\n" +
                "\n" +
                "# Language setting (en_US or zh_CN)\n" +
                "language: \"en_US\"\n" +
                "\n" +
                "# Check for updates on startup (true or false)\n" +
                "check_updates: true\n" +
                "\n" +
                "# Version name configuration\n" +
                "version_name: \"Minecraft 1.20.1-1.21.11\"\n" +
                "\n" +
                "# ===== Color Support =====\n" +
                "# \n" +
                "# Bukkit platform supports legacy color codes:\n" +
                "#   &c red, &6 gold, &e yellow, &a green, &b aqua, &9 blue\n" +
                "#   &l bold, &o italic, &n underline, &m strikethrough, &r reset\n" +
                "#\n" +
                "# Examples:\n" +
                "#   version_name: \"&c&lPremium &f&l| &e1.20.1\"\n" +
                "#   version_name: \"&6Minecraft &71.20.1-1.21.11\"\n";
            
            // Write the default configuration to file
            Files.writeString(configPath, defaultConfig);
            logger.info("Created default configuration file: " + configPath);
            
            // Set the default version name
            versionName = "Minecraft 1.20.1-1.21.11";
        } catch (IOException e) {
            logger.severe("Failed to create default config file: " + e.getMessage() + ". Using default version name.");
            versionName = "Minecraft 1.20.1-1.21.11";
        }
    }

    /**
     * Returns the configured version name.
     * This method should be called after load() to get the actual configured value.
     *
     * @return the version name string to display in the server list
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * Sets the version name.
     *
     * @param versionName the new version name
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * Returns the configured language.
     *
     * @return the language code (e.g., "en_US", "zh_CN")
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Returns whether to check for updates.
     *
     * @return true if update checking is enabled, false otherwise
     */
    public boolean isCheckUpdates() {
        return checkUpdates;
    }

    /**
     * Saves the current configuration to config.yml file.
     */
    public void save() {
        try {
            // Ensure the data directory exists
            Files.createDirectories(configPath.getParent());
            
            // Build configuration content (comments in English)
            StringBuilder content = new StringBuilder();
            content.append("# VersionTinmoli Configuration File\n");
            content.append("# Customize the version name displayed in the server list\n\n");
            content.append("# Language setting (en_US or zh_CN)\n");
            content.append("language: \"").append(language).append("\"\n\n");
            content.append("# Check for updates on startup (true or false)\n");
            content.append("check_updates: ").append(checkUpdates).append("\n\n");
            content.append("# Version name configuration\n");
            content.append("version_name: \"").append(versionName).append("\"\n\n");
            
            // Add color format help
            content.append("# ===== Color Support =====\n");
            content.append("# \n");
            content.append("# Bukkit platform supports legacy color codes:\n");
            content.append("#   &c red, &6 gold, &e yellow, &a green, &b aqua, &9 blue\n");
            content.append("#   &l bold, &o italic, &n underline, &m strikethrough, &r reset\n");
            content.append("#\n");
            content.append("# Examples:\n");
            content.append("#   version_name: \"&c&lPremium &f&l| &e1.20.1\"\n");
            content.append("#   version_name: \"&6Minecraft &71.20.1-1.21.11\"\n");
            
            Files.writeString(configPath, content.toString());
            logger.info(languageLoader.getMessage("config.saved_success", configPath.toString()));
        } catch (IOException e) {
            logger.severe(languageLoader.getMessage("config.save_failed", e.getMessage()));
        }
    }
}
