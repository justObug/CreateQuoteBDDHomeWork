package org.example;

import org.example.runners.QuoteTestRunner;
import org.example.runners.QuotePerformanceTestRunner;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {
        TestNG testng = new TestNG();
        List<Class> classes = new ArrayList<>();
        classes.add(QuoteTestRunner.class);
        classes.add(QuotePerformanceTestRunner.class);
        testng.setTestClasses(classes.toArray(new Class[0]));
        testng.run();
    }
}