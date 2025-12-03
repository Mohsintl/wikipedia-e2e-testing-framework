package com.wikipedia;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for all test cases.
 * 
 * Provides TestNG lifecycle hooks:
 * - @BeforeMethod: Initializes driver and navigates to base URL.
 * - @AfterMethod: Quits driver and cleans up resources.
 * 
 * All tests should extend this class to inherit standard setup/teardown.
 */
public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.get(new ConfigReader().getUrl());
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
