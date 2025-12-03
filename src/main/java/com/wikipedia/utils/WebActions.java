package com.wikipedia.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Reusable helper for common Selenium interactions.
 * 
 * Provides safe wrappers around Selenium operations with:
 * - Explicit waits using WebDriverWait (configurable timeout).
 * - Fallback mechanisms (JS click, scroll) for hard-to-interact elements.
 * - Consistent error handling and logging.
 * 
 * Methods:
 * - click(By): Click element with wait + JS fallback.
 * - type(By, String): Type text with clear + sendKeys.
 * - getText(By): Get visible text with wait.
 * - findElements(By): Find elements with presence wait.
 * - pressEnter(By): Send ENTER key.
 * 
 * Usage:
 *   WebActions actions = new WebActions(driver);
 *   actions.click(By.id("submit"));
 *   actions.type(By.id("search"), "India");
 */
public class WebActions {
    private WebDriver driver;
    private WebDriverWait wait;

    public WebActions(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public WebActions(WebDriver driver) {
        this(driver, Constants.getExplicitWaitSeconds());
    }

    public void click(By locator) {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            el.click();
        } catch (org.openqa.selenium.TimeoutException e) {
            // fallback: try to find the element, scroll into view and click via JS
            WebElement el = driver.findElement(locator);
            try {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            } catch (Exception jsEx) {
                // final fallback: attempt native click
                el.click();
            }
        }
    }

    public void type(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
    }

    public String getText(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return el.getText();
    }

    public List<WebElement> findElements(By locator) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return driver.findElements(locator);
    }

    public void pressEnter(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.sendKeys(org.openqa.selenium.Keys.ENTER);
    }
}
