package com.wikipedia.tests;

import com.wikipedia.BaseTest;
import com.wikipedia.pages.HomePage;
import com.wikipedia.pages.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OpenAIUrlTest extends BaseTest {

    @Test
    public void testOpenAiSearchUrlContainsOpenAI() {
        HomePage home = new HomePage(driver);
        home.open();

        // perform search and press enter to go to results/article
        home.enterSearchTerm("OpenAI");
        // press enter via HomePage flow
        com.wikipedia.pages.ArticlePage article = home.search("OpenAI");

        // use SearchResultsPage to inspect URL
        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        String currentUrl = resultsPage.getCurrentUrl();
        System.out.println("Current URL after searching OpenAI: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("OpenAI"), "URL does not contain expected segment '/OpenAI' - was: " + currentUrl);
    }
}
