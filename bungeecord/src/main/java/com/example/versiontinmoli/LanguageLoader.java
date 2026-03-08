package com.example.versiontinmoli;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * LanguageLoader is responsible for loading and managing language files.
 * It supports loading YAML language files with nested structure and provides
 * message retrieval with parameter substitution.
 */
public class LanguageLoader {
    private final Path dataDirectory;
    private final Logger logger;
    private final Map<String, String> messages;
    private String currentLanguage;
    private final ConfigurationProvider provider;

    /**
     * Constructs a new LanguageLoader.
     *
     * @param dataDirectory the plugin's data directory
     * @param logger the logger instance
     */
    public LanguageLoader(Path dataDirectory, Logger logger) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
        this.messages = new HashMap<>();
        this.currentLanguage = "en_US";
        this.provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
    }

    /**
     * Loads a language file.
     *
     * @param language the language code (e.g., "en_US", "zh_CN")
     */
    public void load(String language) {
        this.currentLanguage = language;
        messages.clear();

        // Try to load from external file first (in data directory)
        Path externalLangFile = dataDirectory.resolve("lang").resolve(language + ".yml");
        if (Files.exists(externalLangFile)) {
            try (InputStream input = Files.newInputStream(externalLangFile)) {
                loadFromStream(input);
                logger.info("Loaded language file from external: " + language);
                return;
            } catch (Exception e) {
                logger.warning("Failed to load external language file: " + e.getMessage());
            }
        }

        // Try to load from plugin resources (in JAR)
        String resourcePath = "lang/" + language + ".yml";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (input != null) {
                loadFromStream(input);
                logger.info("Loaded language file from resources: " + language);
                
                // Copy to external directory for user customization
                try (InputStream copyInput = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                    if (copyInput != null) {
                        Files.createDirectories(dataDirectory.resolve("lang"));
                        if (!Files.exists(externalLangFile)) {
                            Files.copy(copyInput, externalLangFile);
                            logger.info("Copied language file to: " + externalLangFile);
                        }
                    }
                } catch (Exception e) {
                    // Ignore copy errors
                }
                return;
            }
        } catch (Exception e) {
            logger.warning("Failed to load language file from resources: " + e.getMessage());
        }

        // Fallback to en_US if the requested language fails
        if (!language.equals("en_US")) {
            logger.warning("Language file " + language + " not found, falling back to en_US");
            load("en_US");
        }
    }

    /**
     * Loads language data from an input stream.
     */
    private void loadFromStream(InputStream input) {
        try {
            Configuration config = provider.load(input);
            flattenConfiguration("", config, messages);
        } catch (Exception e) {
            logger.severe("Failed to parse language file: " + e.getMessage());
        }
    }

    /**
     * Flattens a nested configuration structure into dot-separated keys.
     */
    private void flattenConfiguration(String prefix, Configuration config, Map<String, String> result) {
        for (String key : config.getKeys()) {
            String fullKey = prefix.isEmpty() ? key : prefix + "." + key;
            Object value = config.get(key);
            
            if (value instanceof Configuration) {
                flattenConfiguration(fullKey, (Configuration) value, result);
            } else if (value instanceof String) {
                result.put(fullKey, (String) value);
            }
        }
    }

    /**
     * Gets a message by key.
     *
     * @param key the message key (e.g., "plugin.enabled")
     * @return the message text, or the key itself if not found
     */
    public String getMessage(String key) {
        return messages.getOrDefault(key, key);
    }

    /**
     * Gets a message by key with parameter substitution.
     *
     * @param key the message key
     * @param args the parameters to substitute
     * @return the formatted message text
     */
    public String getMessage(String key, Object... args) {
        String message = getMessage(key);
        if (args.length == 0) {
            return message;
        }
        
        try {
            return MessageFormat.format(message, args);
        } catch (Exception e) {
            logger.warning("Failed to format message '" + key + "': " + e.getMessage());
            return message;
        }
    }

    /**
     * Gets the current language code.
     *
     * @return the current language code
     */
    public String getCurrentLanguage() {
        return currentLanguage;
    }
}
