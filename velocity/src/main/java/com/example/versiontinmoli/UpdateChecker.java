package com.example.versiontinmoli;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * UpdateChecker checks for plugin updates from GitHub releases.
 */
public class UpdateChecker {
    private static final String GITHUB_API_URL = "https://api.github.com/repos/Tinmoli/VersionTinmoli/releases/latest";
    private static final int TIMEOUT = 5000; // 5 seconds timeout
    
    private final String currentVersion;
    private final Logger logger;

    /**
     * Constructs a new UpdateChecker.
     *
     * @param currentVersion the current plugin version
     * @param logger the logger instance
     */
    public UpdateChecker(String currentVersion, Logger logger) {
        this.currentVersion = currentVersion;
        this.logger = logger;
    }

    /**
     * Checks for updates asynchronously.
     */
    public void checkForUpdates() {
        CompletableFuture.runAsync(() -> {
            try {
                String latestVersion = fetchLatestVersion();
                if (latestVersion != null && !latestVersion.equals(currentVersion)) {
                    logger.warn("========================================");
                    logger.warn("A new version of VersionTinmoli is available!");
                    logger.warn("Current version: {}", currentVersion);
                    logger.warn("Latest version: {}", latestVersion);
                    logger.warn("Download: https://github.com/Tinmoli/VersionTinmoli/releases/latest");
                    logger.warn("========================================");
                } else if (latestVersion != null) {
                    logger.info("VersionTinmoli is up to date! ({})", currentVersion);
                }
            } catch (Exception e) {
                logger.debug("Failed to check for updates: {}", e.getMessage());
            }
        });
    }

    /**
     * Fetches the latest version from GitHub API.
     *
     * @return the latest version string, or null if failed
     */
    private String fetchLatestVersion() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(GITHUB_API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setRequestProperty("User-Agent", "VersionTinmoli-UpdateChecker");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse JSON response
                JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();
                String tagName = json.get("tag_name").getAsString();
                
                // Remove 'v' prefix if present (e.g., "v1.0.2" -> "1.0.2")
                return tagName.startsWith("v") ? tagName.substring(1) : tagName;
            }
        } catch (Exception e) {
            logger.debug("Error fetching latest version: {}", e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
