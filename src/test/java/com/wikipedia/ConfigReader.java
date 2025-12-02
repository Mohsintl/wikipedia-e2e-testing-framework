package com.wikipedia;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;

    public ConfigReader() {
        try {
            FileInputStream fis = new FileInputStream("src\\test\\resources\\config.properties");
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBrowser() {
        return properties.getProperty("browser");
    }

    public String getUrl() {
        return properties.getProperty("url");
    }

    public int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicitWait"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("pageLoadTimeout"));
    }
}
