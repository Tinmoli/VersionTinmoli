package com.example.versiontinmoli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConfigLoader.
 */
class ConfigLoaderTest {
    private static final Logger logger = Logger.getLogger(ConfigLoaderTest.class.getName());

    @Test
    void testLoadCreatesDefaultConfigWhenFileDoesNotExist(@TempDir Path tempDir) {
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify default version name is used
        assertEquals("BungeeCord 1.20", loader.getVersionName());
        
        // Verify config file was created
        Path configPath = tempDir.resolve("config.toml");
        assertTrue(Files.exists(configPath));
    }

    @Test
    void testLoadParsesValidConfig(@TempDir Path tempDir) throws Exception {
        // Create a valid config file
        Path configPath = tempDir.resolve("config.toml");
        String configContent = "version_name = \"Custom Server 1.0\"";
        Files.writeString(configPath, configContent);
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify the configured version name is loaded
        assertEquals("Custom Server 1.0", loader.getVersionName());
    }

    @Test
    void testLoadHandlesMissingVersionNameField(@TempDir Path tempDir) throws Exception {
        // Create a config file without version_name field
        Path configPath = tempDir.resolve("config.toml");
        String configContent = "other_field = \"value\"";
        Files.writeString(configPath, configContent);
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify default version name is used
        assertEquals("BungeeCord 1.20", loader.getVersionName());
    }

    @Test
    void testLoadHandlesInvalidToml(@TempDir Path tempDir) throws Exception {
        // Create an invalid TOML file
        Path configPath = tempDir.resolve("config.toml");
        String configContent = "invalid toml [[[";
        Files.writeString(configPath, configContent);
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify default version name is used
        assertEquals("BungeeCord 1.20", loader.getVersionName());
    }

    @Test
    void testLoadHandlesEmptyConfig(@TempDir Path tempDir) throws Exception {
        // Create an empty config file
        Path configPath = tempDir.resolve("config.toml");
        Files.writeString(configPath, "");
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify default version name is used
        assertEquals("BungeeCord 1.20", loader.getVersionName());
    }

    @Test
    void testLoadIgnoresOtherFields(@TempDir Path tempDir) throws Exception {
        // Create a config with version_name and additional fields
        Path configPath = tempDir.resolve("config.toml");
        String configContent = "version_name = \"My Server\"\n" +
            "other_field = \"should be ignored\"\n" +
            "another_number = 42\n";
        Files.writeString(configPath, configContent);
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify only version_name is read
        assertEquals("My Server", loader.getVersionName());
    }
}
