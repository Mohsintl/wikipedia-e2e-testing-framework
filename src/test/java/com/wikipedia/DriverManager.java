package com.wikipedia;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

/**
 * Manages WebDriver lifecycle using the Singleton pattern.
 * 
 * Responsibilities:
 * - Creates and initializes WebDriver (Chrome/Firefox).
 * - Manages implicit waits and page load timeouts.
 * - Provides single entry point via getDriver().
 * - Ensures clean driver shutdown via quitDriver().
 * 
 * Usage:
 *   WebDriver driver = DriverManager.getDriver();  // Get or create
 *   DriverManager.quitDriver();                      // Cleanup
 */
public class DriverManager {
    private static WebDriver driver;
    private static ConfigReader config = new ConfigReader();

    public static WebDriver getDriver() {
        if (driver == null) {
            String browser = config.getBrowser().toLowerCase();
            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                default:
                    System.out.println("Browser not supported, defaulting to Chrome");
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
