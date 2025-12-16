package org.eurofins;

import org.eurofins.runners.QuoteTestRunner;
import org.eurofins.runners.QuotePerformanceTestRunner;
import org.testng.TestNG;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    
    @Test
    public void runAllTests() {
        TestNG testng = new TestNG();
        List<Class> classes = new ArrayList<>();
        classes.add(QuoteTestRunner.class);
        classes.add(QuotePerformanceTestRunner.class);
        testng.setTestClasses(classes.toArray(new Class[0]));
        testng.run();
    }
}