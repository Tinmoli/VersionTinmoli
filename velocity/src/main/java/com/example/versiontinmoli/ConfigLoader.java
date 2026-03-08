package com.example.versiontinmoli;

import com.electronwill.nightconfig.core.file.FileConfig;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * ConfigLoader is responsible for loading and parsing the plugin's TOML configuration file.
 * It handles configuration file creation, parsing, and error recovery with sensible defaults.
 */
public class ConfigLoader {
    private final Path configPath;
    private final Logger logger;
    private String versionName;

    /**
     * Constructs a new ConfigLoader.
     *
     * @param dataDirectory the plugin's data directory where config.toml is located
     * @param logger the logger instance for logging configuration operations
     */
    public ConfigLoader(Path dataDirectory, Logger logger) {
        this.configPath = dataDirectory.resolve("config.toml");
        this.logger = logger;
        this.versionName = "Velocity 3.5"; // Default value
    }

    /**
     * Loads the configuration from config.toml.
     * If the file doesn't exist, creates a default configuration.
     * If parsing fails, falls back to the default version name "Velocity 3.5".
     */
    public void load() {
        try {
            // Check if config file exists
            if (!Files.exists(configPath)) {
                logger.info("Configuration file not found, creating default configuration");
                createDefaultConfig();
                return;
            }

            // Load and parse the TOML file
            try (FileConfig config = FileConfig.of(configPath)) {
                config.load();
                
                // Parse version_name field with type checking
                Object rawValue = config.get("version_name");
                if (rawValue == null) {
                    logger.warn("version_name field not found in config.toml. Using default.");
                    versionName = "Velocity 3.5";
                } else if (rawValue instanceof String) {
                    versionName = (String) rawValue;
                    logger.info("Loaded version name from config: {}", versionName);
                } else {
                    logger.warn("version_name field has invalid type (expected String, got {}). Using default.", 
                               rawValue.getClass().getSimpleName());
                    versionName = "Velocity 3.5";
                }
            }
        } catch (Exception e) {
            logger.error("Failed to parse config.toml: {}. Using default version name.", e.getMessage());
            versionName = "Velocity 3.5";
        }
    }

    /**
     * Creates a default config.toml file with the default version name.
     */
    private void createDefaultConfig() {
        try {
            // Ensure the data directory exists
            Files.createDirectories(configPath.getParent());
            
            // Create default configuration content
            String defaultConfig = """
                # VersionTinmoli 配置文件
                # 自定义服务器列表中显示的版本名称
                
                # 示例：
                # version_name = "Velocity 1.8.x-1.21.11"
                version_name = "Velocity 1.8.x-1.21.11"
                """;
            
            // Write the default configuration to file
            Files.writeString(configPath, defaultConfig);
            logger.info("Created default configuration file at {}", configPath);
            
            // Set the default version name
            versionName = "Velocity 3.5";
        } catch (IOException e) {
            logger.error("Failed to create default config file: {}. Using default version name.", e.getMessage());
            versionName = "Velocity 3.5";
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
}
