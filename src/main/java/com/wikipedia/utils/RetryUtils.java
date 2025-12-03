package com.wikipedia.utils;

import org.openqa.selenium.StaleElementReferenceException;

import java.util.function.Supplier;

/**
 * Handles stale element references during test execution.
 * 
 * Stale elements occur when the DOM is updated after element reference.
 * This utility automatically retries actions that fail with StaleElementReferenceException.
 * 
 * Usage:
 *   String title = RetryUtils.retryOnStale(() -> driver.findElement(By.id("title")).getText());
 *   RetryUtils.retryOnStale(() -> driver.findElement(By.id("btn")).click());
 * 
 * Defaults: 3 attempts, 200ms delay.
 */
public class RetryUtils {

    /**
     * Retries a supplier action (returns value) on stale element exception.
     * @param action The supplier to retry.
     * @param attempts Number of retry attempts.
     * @param delayMs Delay in milliseconds between retries.
     * @return Result of the action on success.
     * @throws StaleElementReferenceException if all attempts fail.
     */
    public static <T> T retryOnStale(Supplier<T> action, int attempts, long delayMs) {
        for (int i = 0; i < attempts - 1; i++) {
            try {
                return action.get();
            } catch (StaleElementReferenceException e) {
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        // last attempt, let exception propagate if it fails
        return action.get();
    }

    public static void retryOnStale(Runnable action, int attempts, long delayMs) {
        retryOnStale(() -> {
            action.run();
            return null;
        }, attempts, delayMs);
    }

    public static <T> T retryOnStale(Supplier<T> action) {
        return retryOnStale(action, 3, 200);
    }

    public static void retryOnStale(Runnable action) {
        retryOnStale(action, 3, 200);
    }
}
