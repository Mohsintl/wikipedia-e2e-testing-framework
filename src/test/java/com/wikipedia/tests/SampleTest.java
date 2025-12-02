package com.wikipedia.tests;

import com.wikipedia.BaseTest;
import org.testng.annotations.Test;

public class SampleTest extends BaseTest {

    @Test
    public void openWikipedia() {
        System.out.println("Page title is: " + driver.getTitle());
    }
}
