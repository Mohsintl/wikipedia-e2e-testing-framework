package com.wikipedia.tests;

import com.wikipedia.BaseTest;
import com.wikipedia.pages.HomePage;
import com.wikipedia.pages.ArticlePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WikipediaSearchTest extends BaseTest {

    @Test
    public void testSearchWithSuggestions() {
        HomePage home = new HomePage(driver);
        home.open();

        ArticlePage article = home.search("India");
        String title = article.getArticleTitle();

        System.out.println("Article title: " + title);
        Assert.assertTrue(title.contains("India"), "Title does not contain search term");
    }
}
