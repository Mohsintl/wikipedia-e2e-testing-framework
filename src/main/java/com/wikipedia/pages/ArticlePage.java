package com.wikipedia.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.wikipedia.utils.RetryUtils;

/**
 * Page Object for Wikipedia article pages.
 * 
 * Encapsulates:
 * - Article title locator (firstHeading).
 * 
 * Methods:
 * - getArticleTitle(): Retrieves article heading text with stale-element retry.
 * 
 * Usage:
 *   ArticlePage article = new ArticlePage(driver);
 *   String title = article.getArticleTitle();  // e.g., "India"
 *   Assert.assertTrue(title.contains("India"));
 */
public class ArticlePage {
    private WebDriver driver;
    private By title = By.id("firstHeading");

    public ArticlePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getArticleTitle() {
        com.wikipedia.utils.WebActions actions = new com.wikipedia.utils.WebActions(driver);
        return RetryUtils.retryOnStale(() -> actions.getText(title));
    }
}
