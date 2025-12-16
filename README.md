# Quote BDD Sample Project

This is a Behavior-Driven Development (BDD) testing framework for a Quote API service, implemented using Cucumber, RestAssured, and TestNG.

## Project Structure

```
src/
├── main/
│   ├── java/org/eurofins/
│   │   ├── constants/          # Application constants
│   │   ├── factory/            # Factory patterns for object creation
│   │   ├── model/              # Data models (POJOs)
│   │   ├── runners/            # Test runners
│   │   ├── service/            # Business logic and API interactions
│   │   ├── stepdefs/           # Cucumber step definitions
│   │   └── utils/              # Utility classes
│   └── resources/              # Configuration files
└── test/
    ├── java/org/eurofins/      # Test runners
    └── resources/features/     # Feature files
```

## Key Components

### 1. Feature Files
- `CreateQuote.feature` - Functional test scenarios
- `CreateQuotePerformanceTest.feature` - Performance test scenarios

### 2. Test Runners
- `QuoteTestRunner` - Runs all functional tests
- `QuotePerformanceTestRunner` - Runs performance tests only

### 3. Core Classes
- `QuoteApiService` - Handles all API interactions
- `QuoteStepsDefs` - Cucumber step definitions
- `PerformanceTester` - Performance testing utilities
- `ResponseValidator` - General response validation utilities
- `QuoteResponseValidator` - Quote-specific validation utilities with BigDecimal precision

### 4. Models
- `Item` - Quote item model with BigDecimal fields for precision
- `ItemBuilder` - Builder pattern for Item creation
- `QuoteRequest` - Quote request model

## Running Tests

### Using Maven
```bash
# Run all tests
mvn test

# Run with specific environment
mvn test -Denvironment=dev

# Run with specific test suite
mvn test -DsuiteXmlFile=testng.xml

# Run specific test class
mvn test -Dtest=TestRunner
```

## Environment Configuration

The framework supports multiple environments:
- `dev` - Development environment
- `test` - Test environment (default)
- `prod` - Production environment

Set the environment using the `environment` system property:
```bash
-Denvironment=dev
```

## BigDecimal Precision Handling

The framework now uses BigDecimal throughout for financial calculations to ensure precision:
- All monetary values (prices, discounts, totals) use BigDecimal
- Quantity values also use BigDecimal for consistency
- Validation methods properly handle scientific notation in API responses
- Precision is maintained in all calculations and comparisons

## Performance Testing

The framework includes comprehensive performance testing capabilities:

### Available Performance Tests
1. **Single Request Timing** - Validates response time for individual requests
2. **Average Response Time** - Measures average response time over multiple requests
3. **Concurrent Load Testing** - Tests performance under concurrent load
4. **Stress Testing** - Tests with large payloads

### Performance Metrics
- Response time (min, max, average)
- Success/failure rates
- Throughput measurements

## Logging

The framework uses SLF4J with Logback for logging with the following configuration:
- Console output for immediate feedback
- File logging configured for detailed analysis (requires `logs/` directory)
- Separate performance logs for performance testing
- Configurable log levels per environment

Log files are configured to be written to the `logs/` directory:
- `application.log` - General application logs
- `performance.log` - Performance test logs

Troubleshooting Logging Issues
1. Ensure the `logs/` directory exists in the project root
2. Verify test execution is reaching code paths with logging statements
3. Check for package mismatches in test runners (ensure `glue` parameter matches step definition package)
4. Validate logback.xml configuration in both source and target directories
5. Check console output for any logging-related

## Dependencies

- **Cucumber** - BDD framework
- **RestAssured** - REST API testing
- **TestNG** - Test execution framework
- **Gson** - JSON processing
- **Logback** - Logging framework

## Best Practices Implemented

1. **Separation of Concerns** - Clear division between test logic, business logic, and utilities
2. **Design Patterns** - Factory, Builder, and Service patterns
3. **Error Handling** - Comprehensive exception handling and logging
4. **Configuration Management** - Environment-specific configuration support
5. **Performance Monitoring** - Built-in performance testing capabilities
6. **Code Reusability** - Modular design for easy maintenance and extension
7. **Financial Precision** - BigDecimal usage for accurate monetary calculations