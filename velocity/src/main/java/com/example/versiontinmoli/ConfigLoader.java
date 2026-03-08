package com.example.versiontinmoli;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

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
    private final Yaml yaml;

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
        this.versionName = "Velocity 1.8.x-1.21.11"; // Default value
        this.language = "en_US"; // Default language
        this.checkUpdates = true; // Default: check for updates
        
        // Configure YAML output format
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        this.yaml = new Yaml(options);
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
            try (InputStream input = Files.newInputStream(configPath)) {
                Map<String, Object> config = yaml.load(input);
                
                // Parse language field
                if (config != null && config.containsKey("language")) {
                    Object langValue = config.get("language");
                    if (langValue instanceof String) {
                        language = (String) langValue;
                    }
                }
                
                // Parse check_updates field
                if (config != null && config.containsKey("check_updates")) {
                    Object checkUpdatesValue = config.get("check_updates");
                    if (checkUpdatesValue instanceof Boolean) {
                        checkUpdates = (Boolean) checkUpdatesValue;
                    }
                }
                
                // Parse version_name field with type checking
                if (config == null || !config.containsKey("version_name")) {
                    logger.warn(languageLoader.getMessage("config.field_not_found"));
                    versionName = "Velocity 1.8.x-1.21.11";
                } else {
                    Object rawValue = config.get("version_name");
                    if (rawValue instanceof String) {
                        versionName = (String) rawValue;
                        logger.info(languageLoader.getMessage("config.loaded", versionName));
                    } else {
                        logger.warn(languageLoader.getMessage("config.invalid_type", rawValue.getClass().getSimpleName()));
                        versionName = "Velocity 1.8.x-1.21.11";
                    }
                }
            }
        } catch (Exception e) {
            logger.error(languageLoader.getMessage("config.parse_failed", e.getMessage()));
            versionName = "Velocity 1.8.x-1.21.11";
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
                "version_name: \"Velocity 1.8.x-1.21.11\"\n" +
                "\n" +
                "# ===== Color Support =====\n" +
                "# \n" +
                "# Velocity platform supports MiniMessage format:\n" +
                "#   <red>text</red>, <#FF5555>text</#FF5555>\n" +
                "#   <bold>text</bold>, <italic>text</italic>\n" +
                "#   <gradient:red:blue>text</gradient>\n" +
                "# \n" +
                "# Examples:\n" +
                "#   version_name: \"<red><bold>Premium</bold></red> <gray>|</gray> <yellow>1.8.x</yellow>\"\n" +
                "#   version_name: \"<gradient:gold:yellow>Minecraft</gradient> <gray>1.8.x-1.21.11</gray>\"\n";
            
            // Write the default configuration to file
            Files.writeString(configPath, defaultConfig);
            logger.info("Created default configuration file: {}", configPath);
            
            // Set the default version name
            versionName = "Velocity 1.8.x-1.21.11";
        } catch (IOException e) {
            logger.error("Failed to create default config file: {}. Using default version name.", e.getMessage());
            versionName = "Velocity 1.8.x-1.21.11";
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
            content.append("version_name: ");
            
            // Handle special characters, add quotes if necessary
            if (needsQuoting(versionName)) {
                content.append("\"").append(escapeYaml(versionName)).append("\"");
            } else {
                content.append("\"").append(versionName).append("\"");
            }
            content.append("\n\n");
            
            // Add color format help
            content.append("# ===== Color Support =====\n");
            content.append("# \n");
            content.append("# Velocity platform supports MiniMessage format:\n");
            content.append("#   <red>text</red>, <#FF5555>text</#FF5555>\n");
            content.append("#   <bold>text</bold>, <italic>text</italic>\n");
            content.append("#   <gradient:red:blue>text</gradient>\n");
            content.append("# \n");
            content.append("# Examples:\n");
            content.append("#   version_name: \"<red><bold>Premium</bold></red> <gray>|</gray> <yellow>1.8.x</yellow>\"\n");
            content.append("#   version_name: \"<gradient:gold:yellow>Minecraft</gradient> <gray>1.8.x-1.21.11</gray>\"\n");
            
            Files.writeString(configPath, content.toString());
            logger.info(languageLoader.getMessage("config.saved_success", configPath.toString()));
        } catch (IOException e) {
            logger.error(languageLoader.getMessage("config.save_failed", e.getMessage()));
        }
    }

    /**
     * Checks if a value needs quoting in YAML.
     */
    private boolean needsQuoting(String value) {
        // YAML special characters need quotes
        return value.contains(":") || value.contains("#") || 
               value.contains("[") || value.contains("]") ||
               value.contains("{") || value.contains("}") ||
               value.startsWith(" ") || value.endsWith(" ");
    }

    /**
     * Escapes special characters for YAML.
     */
    private String escapeYaml(String value) {
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
