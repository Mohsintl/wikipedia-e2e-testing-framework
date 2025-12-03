package com.wikipedia.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import com.wikipedia.utils.RetryUtils;

/**
 * Page Object for Wikipedia's main page and search interface.
 * 
 * Encapsulates:
 * - Search input locator (searchInput).
 * - Search suggestions dropdown (suggestionList).
 * - Language selection links (a[lang='xx']).
 * 
 * Methods:
 * - open(): Navigate to Wikipedia.org.
 * - enterSearchTerm(String): Type search query.
 * - selectFirstSuggestion(): Click first autocomplete or press Enter.
 * - search(String): Combined search flow (enter + select).
 * - selectLanguage(String langCode): Click language link (e.g., 'hi' for Hindi).
 * 
 * Usage:
 *   HomePage home = new HomePage(driver);
 *   home.open();
 *   ArticlePage article = home.search("India");
 */
public class HomePage {
    private WebDriver driver;
    private By searchInput = By.id("searchInput");
    private By suggestionList = By.cssSelector(".suggestion-title"); // auto-suggestions
    private By languageLinkTemplate = By.cssSelector("a[lang='%s']");
    private com.wikipedia.utils.WebActions actions;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new com.wikipedia.utils.WebActions(driver);
    }

    public void open() {
        driver.get("https://www.wikipedia.org/");
    }

    public void enterSearchTerm(String term) {
        actions.type(searchInput, term);
    }

    public ArticlePage selectFirstSuggestion() {
        // wait for suggestions to appear (if any) then click first suggestion or press Enter
        RetryUtils.retryOnStale(() -> {
            List<WebElement> suggestions = actions.findElements(suggestionList);
            if (!suggestions.isEmpty()) {
                WebElement first = suggestions.get(0);
                // wait until clickable then click
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                        .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(first));
                first.click();
            } else {
                actions.pressEnter(searchInput);
            }
        });

        return new ArticlePage(driver);
    }

    public ArticlePage search(String term) {
        enterSearchTerm(term);
        return selectFirstSuggestion();
    }

    public void selectLanguage(String langCode) {
        By langLocator = By.cssSelector(String.format("a[lang='%s']", langCode));
        actions.click(langLocator);
    }
}
