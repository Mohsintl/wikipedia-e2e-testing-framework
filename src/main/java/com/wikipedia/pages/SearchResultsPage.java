package com.wikipedia.pages;

import org.openqa.selenium.WebDriver;

/**
 * Page Object for Wikipedia search results or post-navigation page.
 * 
 * Encapsulates:
 * - Current page URL for URL assertion testing.
 * 
 * Methods:
 * - getCurrentUrl(): Returns the current page URL.
 * 
 * Usage:
 *   SearchResultsPage results = new SearchResultsPage(driver);
 *   String url = results.getCurrentUrl();
 *   Assert.assertTrue(url.contains("OpenAI"));
 */
public class SearchResultsPage {
    private WebDriver driver;

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
