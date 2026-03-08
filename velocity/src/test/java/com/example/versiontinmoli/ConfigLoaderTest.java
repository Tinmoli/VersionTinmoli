package com.example.versiontinmoli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConfigLoader.
 */
class ConfigLoaderTest {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoaderTest.class);

    @Test
    void testLoadCreatesDefaultConfigWhenFileDoesNotExist(@TempDir Path tempDir) {
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify default version name is used
        assertEquals("Velocity 3.5", loader.getVersionName());
        
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
        assertEquals("Velocity 3.5", loader.getVersionName());
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
        assertEquals("Velocity 3.5", loader.getVersionName());
    }

    @Test
    void testLoadHandlesEmptyConfig(@TempDir Path tempDir) throws Exception {
        // Create an empty config file
        Path configPath = tempDir.resolve("config.toml");
        Files.writeString(configPath, "");
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify default version name is used
        assertEquals("Velocity 3.5", loader.getVersionName());
    }

    @Test
    void testLoadHandlesConfigWithComments(@TempDir Path tempDir) throws Exception {
        // Create a config file with only comments
        Path configPath = tempDir.resolve("config.toml");
        String configContent = """
            # This is a comment
            # version_name = "Commented Out"
            """;
        Files.writeString(configPath, configContent);
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify default version name is used
        assertEquals("Velocity 3.5", loader.getVersionName());
    }

    @Test
    void testLoadHandlesMiniMessageFormat(@TempDir Path tempDir) throws Exception {
        // Create a config with MiniMessage formatting
        Path configPath = tempDir.resolve("config.toml");
        String configContent = "version_name = \"<red>Custom Server</red>\"";
        Files.writeString(configPath, configContent);
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify the MiniMessage format is preserved
        assertEquals("<red>Custom Server</red>", loader.getVersionName());
    }

    @Test
    void testLoadHandlesVersionNameAsNumber(@TempDir Path tempDir) throws Exception {
        // Create a config with version_name as a number (invalid type)
        Path configPath = tempDir.resolve("config.toml");
        String configContent = "version_name = 123";
        Files.writeString(configPath, configContent);
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify default version name is used when type is wrong
        assertEquals("Velocity 3.5", loader.getVersionName());
    }

    @Test
    void testLoadHandlesVersionNameAsBoolean(@TempDir Path tempDir) throws Exception {
        // Create a config with version_name as a boolean (invalid type)
        Path configPath = tempDir.resolve("config.toml");
        String configContent = "version_name = true";
        Files.writeString(configPath, configContent);
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify default version name is used when type is wrong
        assertEquals("Velocity 3.5", loader.getVersionName());
    }

    @Test
    void testLoadHandlesVersionNameAsArray(@TempDir Path tempDir) throws Exception {
        // Create a config with version_name as an array (invalid type)
        Path configPath = tempDir.resolve("config.toml");
        String configContent = "version_name = [\"value1\", \"value2\"]";
        Files.writeString(configPath, configContent);
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify default version name is used when type is wrong
        assertEquals("Velocity 3.5", loader.getVersionName());
    }

    @Test
    void testLoadIgnoresOtherFields(@TempDir Path tempDir) throws Exception {
        // Create a config with version_name and additional fields
        Path configPath = tempDir.resolve("config.toml");
        String configContent = """
            version_name = "My Server"
            other_field = "should be ignored"
            another_number = 42
            some_array = ["a", "b", "c"]
            [some_table]
            nested = "value"
            """;
        Files.writeString(configPath, configContent);
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify only version_name is read, other fields don't affect the result
        assertEquals("My Server", loader.getVersionName());
    }

    @Test
    void testLoadHandlesVersionNameAsTable(@TempDir Path tempDir) throws Exception {
        // Create a config with version_name as a table (invalid type)
        Path configPath = tempDir.resolve("config.toml");
        String configContent = """
            [version_name]
            nested = "value"
            """;
        Files.writeString(configPath, configContent);
        
        ConfigLoader loader = new ConfigLoader(tempDir, logger);
        loader.load();
        
        // Verify default version name is used when type is wrong
        assertEquals("Velocity 3.5", loader.getVersionName());
    }
}
