package com.wikipedia.tests;

import com.wikipedia.BaseTest;
import com.wikipedia.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LanguageSelectionTest extends BaseTest {

    @Test
    public void testChangeLanguageToHindi() {
        HomePage home = new HomePage(driver);
        home.open();

        // Click Hindi language link on the main page
        home.selectLanguage("hi");

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after selecting Hindi: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("hi.wikipedia.org") || currentUrl.contains("/hi"), "Expected Hindi site to open, but URL was: " + currentUrl);
    }
}
