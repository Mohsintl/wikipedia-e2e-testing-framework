package com.wikipedia;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.get(new ConfigReader().getUrl());
    }

    // @AfterMethod
    // public void tearDown() {
    //     DriverManager.quitDriver();
    // }
}
