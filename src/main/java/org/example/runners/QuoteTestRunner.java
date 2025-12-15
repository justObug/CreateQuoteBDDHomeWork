package org.example.runners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = {
                "src/test/resources/features/CreateQuote.feature",
                "src/test/resources/features/CreateQuotePerformanceTest.feature"
        },
        glue = "org.example.stepdefs",
        plugin = {
                "pretty",
                "html:target/cucumber-html-report",
                "testng:target/cucumber-testng-report.xml"
        },
        monochrome = true
)
public class QuoteTestRunner extends AbstractTestNGCucumberTests {
    @DataProvider(parallel = true)
    @Override
    public Object[][] scenarios() {
        return super.scenarios();
    }
}