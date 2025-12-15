package org.example.runners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features/CreateQuotePerformanceTest.feature",
        glue = "org.example.stepdefs",
        plugin = {
                "pretty",
                "html:target/cucumber-performance-html-report",
                "testng:target/cucumber-performance-testng-report.xml"
        },
        monochrome = true
)
public class QuotePerformanceTestRunner extends AbstractTestNGCucumberTests {
    // 支持并行执行（可选）
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}