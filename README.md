# Wikipedia E2E Testing Framework

A robust, production-ready end-to-end testing framework for Wikipedia built with **Selenium**, **TestNG**, **Maven**, and **Allure Reporting**.

## 📋 Overview

This framework automates browser-based testing for Wikipedia's core functionalities, including:
- **Search functionality** — verify searching and result navigation
- **URL redirection** — validate navigation to correct Wikipedia articles
- **Language selection** — test multi-language support (e.g., Hindi site)
- **Page interactions** — robust click, type, and wait operations with stale-element handling

## 🏗️ Architecture

### Base Infrastructure

#### 1. **DriverManager** (`src/test/java/com/wikipedia/DriverManager.java`)
   - Singleton pattern for WebDriver lifecycle management.
   - Supports Chrome and Firefox browsers (configurable via `config.properties`).
   - Auto-maximizes window and sets implicit/page-load timeouts.
   - Single entry point: `getDriver()` and `quitDriver()`.

#### 2. **ConfigReader** (`src/test/java/com/wikipedia/ConfigReader.java`)
   - Reads runtime configuration from `src/test/resources/config.properties`.
   - Provides methods for browser, URL, implicit wait, and page load timeout.

#### 3. **Constants** (`src/main/java/com/wikipedia/utils/Constants.java`)
   - Centralizes timeouts and configuration values read from classpath `config.properties`.
   - Methods: `getExplicitWaitSeconds()`, `getPageLoadTimeoutSeconds()`.
   - Provides sensible defaults if config values are missing.

#### 4. **BaseTest** (`src/test/java/com/wikipedia/BaseTest.java`)
   - TestNG base class with `@BeforeMethod` and `@AfterMethod` hooks.
   - Sets up the WebDriver and loads base URL before each test.
   - Quits the driver cleanly after each test.

### Utility Classes

#### 5. **WebActions** (`src/main/java/com/wikipedia/utils/WebActions.java`)
   - Reusable helper for common Selenium interactions:
     - `click(By)` — clicks element with explicit wait and fallback to JS click if blocked.
     - `type(By, String)` — types text into an element with clear + sendKeys.
     - `getText(By)` — gets visible text with explicit wait.
     - `findElements(By)` — finds elements with presence wait.
     - `pressEnter(By)` — sends ENTER key to an element.
   - All methods use **configurable explicit waits** from `Constants`.
   - Includes fallback mechanisms (scroll + JS click) for hard-to-click elements.

#### 6. **RetryUtils** (`src/main/java/com/wikipedia/utils/RetryUtils.java`)
   - Handles **stale element references** gracefully.
   - Generic retry logic for Supplier and Runnable actions.
   - Default: 3 attempts with 200ms delay between retries.
   - Used in page objects to make interactions resilient to DOM changes.

### Page Object Model (POM)

#### 7. **HomePage** (`src/main/java/com/wikipedia/pages/HomePage.java`)
   - Represents the Wikipedia main page and search interface.
   - Methods:
     - `open()` — navigates to Wikipedia.org.
     - `enterSearchTerm(String)` — types a search query.
     - `selectFirstSuggestion()` — clicks first autocomplete suggestion or presses Enter.
     - `search(String)` — combines enterSearchTerm + selectFirstSuggestion.
     - `selectLanguage(String langCode)` — selects a language link (e.g., "hi" for Hindi).

#### 8. **ArticlePage** (`src/main/java/com/wikipedia/pages/ArticlePage.java`)
   - Represents a Wikipedia article page.
   - Methods:
     - `getArticleTitle()` — retrieves the article heading with stale-element retry.

#### 9. **SearchResultsPage** (`src/main/java/com/wikipedia/pages/SearchResultsPage.java`)
   - Represents search results or post-redirect page state.
   - Methods:
     - `getCurrentUrl()` — returns the current page URL.

---

## 🧪 Test Cases

All tests extend **BaseTest** and are configured in `testng.xml`.

### 1. **WikipediaSearchTest** (`testSearchWithSuggestions`)
   - **Purpose:** Smoke test — verify basic search functionality.
   - **Steps:**
     1. Open Wikipedia.
     2. Search for "India".
     3. Verify article title contains "India".
   - **Expected:** Article page loads with title containing the search term.

### 2. **OpenAIUrlTest** (`testOpenAiSearchUrlContainsOpenAI`)
   - **Purpose:** Verify URL redirection after search.
   - **Steps:**
     1. Open Wikipedia.
     2. Search for "OpenAI".
     3. Verify current URL contains "OpenAI".
   - **Expected:** URL reflects the searched article (e.g., `/wiki/OpenAI`).

### 3. **LanguageSelectionTest** (`testChangeLanguageToHindi`)
   - **Purpose:** Test multi-language support.
   - **Steps:**
     1. Open Wikipedia.
     2. Click language link for Hindi ("hi").
     3. Verify URL indicates Hindi site (`hi.wikipedia.org` or `/hi`).
   - **Expected:** Hindi Wikipedia site opens.

### 4. **SampleTest**
   - Basic infrastructure test to verify setup is working.

---

## 🔧 Configuration

### `src/test/resources/config.properties`

```properties
# Browser can be chrome or firefox
browser=chrome

# Wikipedia URL
url=https://www.wikipedia.org

# Implicit wait in seconds
implicitWait=10

# Page load timeout in seconds
pageLoadTimeout=30

# Explicit wait for element interactions (optional, defaults to implicitWait)
explicitWait=10
```

All values are read by:
- `ConfigReader` — for test setup (driver creation, base URL).
- `Constants` — for explicit waits in `WebActions`.

---

## 📊 Logging

### Log4j2 Configuration (`src/test/resources/log4j2.xml`)

- **Console Appender:** Real-time test output with timestamp, thread, level, and message.
- **File Appender:** Test logs written to `target/test-logs/test.log`.
- **Root Level:** INFO (captures test and framework logs).
- **Selenium Logger:** WARN (reduces verbose CDP warnings).

**Output Example:**
```
15:27:37.764 [main] INFO  io.github.bonigarcia.wdm.WebDriverManager - Using chromedriver 142.0.7444.175
```

All SLF4J logs (from Selenium, WebDriverManager, etc.) are automatically routed to Log4j2.

---

## 🚀 Running Tests

### Prerequisites
- Java 8+ (tested with Java 17)
- Maven 3.6+
- Chrome or Firefox browser installed

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn -Dtest=com.wikipedia.tests.WikipediaSearchTest test
```

### Run with Clean Build
```bash
mvn clean test
```

### Generate Allure Report (after running tests)
```bash
mvn allure:report
```
Report opens at `target/site/allure-report/index.html`.

---

## 📁 Project Structure

```
wikipedia-e2e-testing-framework/
├── src/
│   ├── main/java/com/wikipedia/
│   │   ├── App.java                          # Application entry point
│   │   ├── pages/
│   │   │   ├── HomePage.java                 # Search page object
│   │   │   ├── ArticlePage.java              # Article page object
│   │   │   └── SearchResultsPage.java        # Search results page object
│   │   └── utils/
│   │       ├── WebActions.java               # Reusable Selenium actions
│   │       ├── RetryUtils.java               # Stale element retry logic
│   │       └── Constants.java                # Centralized configuration
│   ├── test/java/com/wikipedia/
│   │   ├── BaseTest.java                     # Test base class with setup/teardown
│   │   ├── DriverManager.java                # WebDriver lifecycle
│   │   ├── ConfigReader.java                 # Config file reader
│   │   └── tests/
│   │       ├── SampleTest.java               # Basic infrastructure test
│   │       ├── WikipediaSearchTest.java      # Search functionality test
│   │       ├── OpenAIUrlTest.java            # URL redirection test
│   │       └── LanguageSelectionTest.java    # Language selection test
│   └── resources/
│       ├── config.properties                 # Test configuration
│       └── log4j2.xml                        # Logging configuration
├── testng.xml                                # TestNG suite configuration
├── pom.xml                                   # Maven dependencies and build config
├── allure-results/                           # Allure report data (generated)
└── target/
    ├── test-logs/test.log                    # Test execution logs
    └── surefire-reports/                     # TestNG XML reports
```

---

## 🛠️ Key Features & Design Patterns

### 1. **Page Object Model (POM)**
   - Encapsulates page elements and interactions in dedicated classes.
   - Decouples test logic from page structure.
   - Easy to maintain and refactor when page structure changes.

### 2. **Singleton Pattern (DriverManager)**
   - Single WebDriver instance throughout the test session.
   - Centralized lifecycle management (creation, quit).

### 3. **Retry Logic (RetryUtils)**
   - Handles **stale element references** — common in dynamic web applications.
   - Automatic retry with configurable attempts and delays.

### 4. **Explicit Waits with Fallbacks (WebActions)**
   - Primary: `WebDriverWait` with `ExpectedConditions`.
   - Fallback 1: Scroll into view + JavaScript click.
   - Fallback 2: Native Selenium click.
   - Ensures robustness across different element rendering states.

### 5. **Configuration-Driven Testing**
   - Browser, URL, and timeouts configurable via `config.properties`.
   - Easy to swap configurations for local vs. CI environments.
   - `Constants` provides runtime access to config values.

### 6. **Centralized Logging**
   - All framework and test logs route through Log4j2.
   - Console and file output for easy debugging.

---

## 🧬 Dependencies

### Core Testing
- **Selenium**: 4.25.0 — browser automation
- **TestNG**: 7.10.2 — test framework and assertions
- **WebDriverManager**: 5.8.0 — automatic driver management

### Reporting & Logging
- **Allure TestNG**: 2.25.0 — rich HTML test reports
- **Log4j2 Core**: 2.23.1 — logging framework
- **Log4j2 SLF4J Impl**: 2.23.1 — bridges SLF4J to Log4j2

### Build
- **Maven Compiler**: JDK 1.8 target
- **Maven Surefire**: 3.2.5 — test runner

---

## 🔄 Workflow Example

```java
// Test extends BaseTest (driver setup/teardown handled automatically)
public class WikipediaSearchTest extends BaseTest {
    @Test
    public void testSearchWithSuggestions() {
        // 1. Create page object with driver
        HomePage home = new HomePage(driver);
        
        // 2. Perform interaction via page object
        home.open();
        ArticlePage article = home.search("India");
        
        // 3. Verify result
        String title = article.getArticleTitle();
        Assert.assertTrue(title.contains("India"), "Title should contain 'India'");
    }
}
```

**Behind the scenes:**
- `home.open()` — uses native Selenium `driver.get()`.
- `home.search("India")` — uses `WebActions.type()` + `WebActions.findElements()`.
- `article.getArticleTitle()` — uses `WebActions.getText()` wrapped in `RetryUtils` for stale-element resilience.
- All explicit waits use timeouts from `Constants.getExplicitWaitSeconds()`.
- Logs are written to console and `target/test-logs/test.log` via Log4j2.

---

## 🐛 Troubleshooting

### Issue: SLF4J NOP Logger Warning
**Solution:** Already fixed! `log4j-slf4j-impl` is in `pom.xml` and `log4j2.xml` is configured.

### Issue: Stale Element Reference
**Solution:** Page objects use `RetryUtils.retryOnStale()` to auto-retry on stale elements.

### Issue: Element Not Clickable
**Solution:** `WebActions.click()` includes fallback logic (scroll + JS click).

### Issue: Tests Timeout
**Solution:** Adjust `implicitWait` or `explicitWait` in `config.properties` and rebuild.

---

## 📈 Next Steps / Roadmap

- [ ] **CI/CD Integration** — GitHub Actions workflow to run tests on every push.
- [ ] **Allure Report Upload** — Archive `allure-results/` as CI artifact.
- [ ] **Parallel Test Execution** — Configure TestNG for concurrent test runs.
- [ ] **Cross-Browser Testing** — Add Firefox and Safari support.
- [ ] **Extended Test Coverage** — Add tests for editing, user interactions, etc.
- [ ] **Performance Testing** — Add load/stress test scenarios.

---

## 👤 Author & License

**Created by:** Mohsin

**Repository:** [wikipedia-e2e-testing-framework](https://github.com/Mohsintl/wikipedia-e2e-testing-framework)

**Branch:** sprint-1

---

## 📞 Support

For issues, questions, or contributions, please refer to the repository's issue tracker or contact the maintainer.

---

**Last Updated:** December 3, 2025

## How to Run Tests
