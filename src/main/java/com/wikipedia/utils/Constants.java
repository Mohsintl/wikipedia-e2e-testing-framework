package com.wikipedia.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralizes configuration constants and values read from config.properties.
 * 
 * Purpose:
 * - Provides consistent timeout values across the framework.
 * - Reads from classpath config.properties (test resources).
 * - Offers sensible defaults if values are missing.
 * 
 * Usage:
 *   int timeout = Constants.getExplicitWaitSeconds();     // Returns 10 (default) or custom value
 *   int pageLoadTimeout = Constants.getPageLoadTimeoutSeconds();  // Returns 30 (default) or custom
 */
public class Constants {
    private static final Properties props = new Properties();

    static {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException ignored) {
        }
    }

    public static int getExplicitWaitSeconds() {
        String v = props.getProperty("explicitWait");
        if (v != null) {
            try {
                return Integer.parseInt(v);
            } catch (NumberFormatException ignored) {
            }
        }
        // fallback to implicitWait if present
        v = props.getProperty("implicitWait");
        if (v != null) {
            try {
                return Integer.parseInt(v);
            } catch (NumberFormatException ignored) {
            }
        }
        return 10; // default
    }

    public static int getPageLoadTimeoutSeconds() {
        String v = props.getProperty("pageLoadTimeout");
        if (v != null) {
            try {
                return Integer.parseInt(v);
            } catch (NumberFormatException ignored) {
            }
        }
        return 30;
    }
}
