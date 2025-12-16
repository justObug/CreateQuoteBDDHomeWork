package org.eurofins.stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.eurofins.constants.TestConstants;
import org.eurofins.factory.QuoteServiceFactory;
import org.eurofins.model.Item;
import org.eurofins.model.ItemBuilder;
import org.eurofins.model.QuoteRequest;
import org.eurofins.service.QuoteApiService;
import org.eurofins.utils.PerformanceTester;
import org.eurofins.utils.QuoteResponseValidator;
import org.eurofins.utils.ResponseValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuoteStepsDefs {
    private static final Logger logger = LoggerFactory.getLogger(QuoteStepsDefs.class);
    
    private QuoteApiService quoteService;
    private Response response;
    private String customerId;
    private List<Item> items = new ArrayList<>();
    private long responseTime;

    public QuoteStepsDefs() {
        this.quoteService = QuoteServiceFactory.createService();
        // By default, users are authorized
        this.quoteService.setAuthorized(true);
        logger.info("QuoteStepsDefs initialized with default authorized service");
    }
    
    // Customer identification steps
    @Given("a customer with identifier {string}")
    public void aCustomerWithIdentifier(String custId) {
        this.customerId = custId;
        logger.debug("Set customer ID to: {}", custId);
    }

    @Given("an unauthorized user (no token)")
    public void anUnauthorizedUserNoToken() {
        // Set the existing service instance to unauthorized status
        this.quoteService.setAuthorized(false);
        logger.info("Set user as unauthorized");
    }

    // Item identification steps
    @Given("an item with identifier {string}")
    public void anItemWithIdentifier(String itemId) {
        Item item = ItemBuilder.builder().item(itemId).build();
        items.add(item);
        logger.debug("Added item with ID: {}", itemId);
    }

    @Given("one item with identifier {string}")
    public void oneItemWithIdentifier(String itemId) {
        anItemWithIdentifier(itemId);
    }
    
    @Given("two items {string} and {string}")
    public void twoItems(String firstItemId, String secondItemId) {
        anItemWithIdentifier(firstItemId);
        anItemWithIdentifier(secondItemId);
        logger.debug("Added two items: {} and {}", firstItemId, secondItemId);
    }
    
    @Given("three items {string}, {string} and {string}")
    public void threeItems(String firstItemId, String secondItemId, String thirdItemId) {
        anItemWithIdentifier(firstItemId);
        anItemWithIdentifier(secondItemId);
        anItemWithIdentifier(thirdItemId);
        logger.debug("Added three items: {}, {} and {}", firstItemId, secondItemId, thirdItemId);
    }
    
    @Given("the following items:")
    public void theFollowingItems(List<Map<String, String>> itemsData) {
        items.clear();
        for (Map<String, String> itemData : itemsData) {
            if (itemData.containsKey(TestConstants.ITEM_ID_FIELD)) {
                Item item = ItemBuilder.builder().item(itemData.get(TestConstants.ITEM_ID_FIELD)).build();
                items.add(item);
            }
        }
        logger.debug("Added {} items from data table", items.size());
    }

    // Core quote creation steps
    /**
     * Helper method to update the first item with quantity and price
     */
    private void updateFirstItemWithQuantityAndPrice(Double quantity, Double price) {
        if (!items.isEmpty()) {
            Item firstItem = items.get(0);
            firstItem.setQuantity(new BigDecimal(String.valueOf(quantity)));
            firstItem.setUnitaryPrice(new BigDecimal(String.valueOf(price)));
            logger.debug("Updated first item with quantity: {} and price: {}", quantity, price);
        }
    }
    
    /**
     * Helper method to update the first item with quantity, price, and discount
     */
    private void updateFirstItemWithQuantityPriceAndDiscount(Double quantity, Double price, Double discount) {
        if (!items.isEmpty()) {
            Item firstItem = items.get(0);
            firstItem.setQuantity(new BigDecimal(String.valueOf(quantity)));
            firstItem.setUnitaryPrice(new BigDecimal(String.valueOf(price)));
            firstItem.setDiscountPercentage(new BigDecimal(String.valueOf(discount)));
            logger.debug("Updated first item with quantity: {}, price: {}, discount: {}", quantity, price, discount);
        }
    }
    
    /**
     * Helper method to create and send a quote request
     */
    private void createAndSendQuoteRequest() {
        QuoteRequest request = new QuoteRequest();
        request.setCustomer(customerId);
        request.setItems(items);
        
        response = quoteService.createQuote(request);
        responseTime = quoteService.getLastResponseTime();
        
        logger.info("Quote creation completed with status: {}, response time: {}ms", 
                   response.getStatusCode(), responseTime);
    }

    @When("I create a quote for that customer with that item with the quantity {double} and the price {double}")
    public void createQuoteWithQuantityAndPrice(Double quantity, Double price) {
        logger.info("Creating quote for customer {} with item, quantity: {}, price: {}", customerId, quantity, price);
        
        updateFirstItemWithQuantityAndPrice(quantity, price);
        createAndSendQuoteRequest();
    }
    
    @When("I create a quote for that customer with that item with the quantity {double}, the price {double} and the discount {double}")
    public void createQuoteWithQuantityPriceAndDiscountVariant(Double quantity, Double price, Double discount) {
        logger.info("Creating quote for customer {} with item, quantity: {}, price: {}, discount: {}", 
                   customerId, quantity, price, discount);
        
        updateFirstItemWithQuantityPriceAndDiscount(quantity, price, discount);
        createAndSendQuoteRequest();
    }
    
    @When("I create a quote for that customer with that item with the quantity {double}, the unitary price {double}, and a percentage of discount {double}")
    public void createQuoteWithQuantityPriceAndDiscount(Double quantity, Double price, Double discount) {
        logger.info("Creating quote for customer {} with item, quantity: {}, price: {}, discount: {}", 
                   customerId, quantity, price, discount);
        
        updateFirstItemWithQuantityPriceAndDiscount(quantity, price, discount);
        createAndSendQuoteRequest();
    }

    @When("I create a quote for that customer with item {string} with the quantity {double} and the price {double} and item {string} with the quantity {double} and the price {double}")
    public void createQuoteWithTwoItems(String firstItem, Double firstQuantity, Double firstPrice, 
                                       String secondItem, Double secondQuantity, Double secondPrice) {
        logger.info("Creating quote for customer {} with two items", customerId);
        
        items.clear();
        
        // Create first item
        Item first = ItemBuilder.builder()
                .item(firstItem)
                .quantity(new java.math.BigDecimal(firstQuantity.toString()))
                .unitaryPrice(new java.math.BigDecimal(firstPrice.toString()))
                .build();
        items.add(first);
        
        // Create second item
        Item second = ItemBuilder.builder()
                .item(secondItem)
                .quantity(new java.math.BigDecimal(secondQuantity.toString()))
                .unitaryPrice(new java.math.BigDecimal(secondPrice.toString()))
                .build();
        items.add(second);
        
        logger.debug("Created two items: {} (qty: {}, price: {}) and {} (qty: {}, price: {})",
                    firstItem, firstQuantity, firstPrice, secondItem, secondQuantity, secondPrice);
        
        QuoteRequest request = new QuoteRequest();
        request.setCustomer(customerId);
        request.setItems(items);
        
        response = quoteService.createQuote(request);
        responseTime = quoteService.getLastResponseTime();
        
        logger.info("Quote creation completed with status: {}, response time: {}ms", 
                   response.getStatusCode(), responseTime);
    }

    @When("I create a quote for that customer with that item:")
    public void createQuoteWithItemData(List<Map<String, String>> itemsData) {
        logger.info("Creating quote for customer {} with item data from table", customerId);
        
        // Update items with the provided data
        for (int i = 0; i < Math.min(items.size(), itemsData.size()); i++) {
            Map<String, String> itemData = itemsData.get(i);
            Item item = items.get(i);
            
            if (itemData.containsKey(TestConstants.QUANTITY_FIELD)) {
                BigDecimal quantity = new BigDecimal(itemData.get(TestConstants.QUANTITY_FIELD));
                item.setQuantity(quantity);
                logger.debug("Updated item {} quantity to: {}", i, quantity);
            }
            
            if (itemData.containsKey(TestConstants.UNITARY_PRICE_FIELD)) {
                BigDecimal price = new BigDecimal(itemData.get(TestConstants.UNITARY_PRICE_FIELD));
                item.setUnitaryPrice(price);
                logger.debug("Updated item {} unitary price to: {}", i, price);
            }
            
            if (itemData.containsKey(TestConstants.DISCOUNT_PERCENTAGE_FIELD)) {
                BigDecimal discount = new BigDecimal(itemData.get(TestConstants.DISCOUNT_PERCENTAGE_FIELD));
                item.setDiscountPercentage(discount);
                logger.debug("Updated item {} discount percentage to: {}", i, discount);
            }
        }
        
        QuoteRequest request = new QuoteRequest();
        request.setCustomer(customerId);
        request.setItems(items);
        
        response = quoteService.createQuote(request);
        responseTime = quoteService.getLastResponseTime();
        
        logger.info("Quote creation completed with status: {}, response time: {}ms", 
                   response.getStatusCode(), responseTime);
    }

    // Quote revision step
    @When("I revise the quote by changing the quantity to {double}")
    public void reviseQuoteWithQuantity(Double newQuantity) {
        logger.info("Revising quote for customer {} with new quantity: {}", customerId, newQuantity);
        
        if (!items.isEmpty()) {
            Item firstItem = items.get(0);
            firstItem.setQuantity(new BigDecimal(String.valueOf(newQuantity)));
            logger.debug("Updated first item quantity to: {}", newQuantity);
        }
        
        QuoteRequest request = new QuoteRequest();
        request.setCustomer(customerId);
        request.setItems(items);
        
        response = quoteService.reviseQuote("QUOTE_ID", request); // TODO: Implement proper quote ID handling
        responseTime = quoteService.getLastResponseTime();
        
        logger.info("Quote revision completed with status: {}, response time: {}ms", 
                   response.getStatusCode(), responseTime);
    }

    // Assertion steps
    @Then("it returns the quote with the correct details:")
    public void validateQuoteDetails(List<Map<String, String>> expectedData) {
        logger.info("Validating quote details");
        
        if (!expectedData.isEmpty()) {
            Map<String, String> rowData = expectedData.get(0);
            String expectedCustomer = rowData.get(TestConstants.CUSTOMER_FIELD);
            
            ResponseValidator.validateCustomer(response, expectedCustomer);
            
            String expectedItem = rowData.get("items.item");
            if (expectedItem != null && !expectedItem.isEmpty()) {
                String expectedQuantityStr = rowData.get("items.quantity");
                Double expectedQuantity = expectedQuantityStr != null && !expectedQuantityStr.isEmpty() ? 
                    Double.parseDouble(expectedQuantityStr) : null;
                
                String expectedUnitaryPriceStr = rowData.get("items.unitaryPrice");
                Double expectedUnitaryPrice = expectedUnitaryPriceStr != null && !expectedUnitaryPriceStr.isEmpty() ? 
                    Double.parseDouble(expectedUnitaryPriceStr) : null;
                
                ResponseValidator.validateItemDetails(response, expectedItem, expectedQuantity, expectedUnitaryPrice);
            }
        }
        logger.info("Quote details validation completed successfully");
    }
    
    @Then("it returns the quote with the correct details, including the total line price calculated as {string}")
    public void validateQuoteWithTotalLinePricePrecise(String expectedTotal) {
        logger.info("Validating quote with precise total line price: {}", expectedTotal);
        ResponseValidator.validateCustomer(response, customerId);
        QuoteResponseValidator.validateQuoteTotalPricePrecise(response, new BigDecimal(expectedTotal));
        logger.info("Quote validation with precise total line price completed");
    }
    
    @Then("it returns the quote with the correct details, including the total line price calculated as {double}")
    public void validateQuoteWithTotalLinePrice(Double expectedTotal) {
        logger.info("Validating quote with total line price: {}", expectedTotal);
        ResponseValidator.validateCustomer(response, customerId);
        QuoteResponseValidator.validateQuoteTotalPrice(response, expectedTotal);
        logger.info("Quote validation with total line price completed");
    }
    
    @Then("it returns the quote with the correct details, including the discount amount {double} and the total line price {double}")
    public void validateQuoteWithDiscountAndTotal(Double expectedDiscount, Double expectedTotal) {
        logger.info("Validating quote with discount: {} and total: {}", expectedDiscount, expectedTotal);
        ResponseValidator.validateCustomer(response, customerId);
        QuoteResponseValidator.validateQuoteDiscountAmount(response, expectedDiscount);
        QuoteResponseValidator.validateQuoteTotalPrice(response, expectedTotal);
        logger.info("Quote validation with discount and total completed");
    }
    
    @Then("it returns the quote with the correct details, including {int} lines and the quote's total price calculated as {double}")
    public void validateQuoteWithMultipleLinesAndTotal(Integer expectedLineCount, Double expectedTotal) {
        logger.info("Validating quote with {} lines and total price: {}", expectedLineCount, expectedTotal);
        ResponseValidator.validateCustomer(response, customerId);
        QuoteResponseValidator.validateQuoteLineCount(response, expectedLineCount);
        QuoteResponseValidator.validateQuoteTotalPrice(response, expectedTotal);
        logger.info("Quote validation with multiple lines and total completed");
    }
    
    @And("a confirmation message {string}")
    public void validateConfirmationMessage(String expectedMsg) {
        logger.info("Validating confirmation message: {}", expectedMsg);
        ResponseValidator.validateConfirmationMessage(response, expectedMsg);
        logger.info("Confirmation message validation completed");
    }

    @Then("the response status code is {int}")
    public void validateStatusCode(int statusCode) {
        logger.info("Validating response status code: {}", statusCode);
        ResponseValidator.validateStatusCode(response, statusCode);
        logger.info("Status code validation completed");
    }

    @And("the response customer is {string}")
    public void validateCustomer(String expectedCustId) {
        logger.info("Validating response customer: {}", expectedCustId);
        ResponseValidator.validateCustomer(response, expectedCustId);
        logger.info("Customer validation completed");
    }

    @And("the quote total amount is {string}")
    public void validateQuoteTotalAmountPrecise(String expectedTotal) {
        logger.info("Validating quote total amount: {}", expectedTotal);
        QuoteResponseValidator.validateQuoteTotalPricePrecise(response, new BigDecimal(expectedTotal));
        logger.info("Quote total amount validation completed");
    }

    @And("the quote total amount is {double}")
    public void validateQuoteTotalAmount(double expectedTotal) {
        logger.info("Validating quote total amount: {}", expectedTotal);
        QuoteResponseValidator.validateQuoteTotalPrice(response, expectedTotal);
        logger.info("Quote total amount validation completed");
    }

    @And("the quote version number is {string}")
    public void validateQuoteVersionNumber(String expectedVersion) {
        logger.info("Validating quote version number: {}", expectedVersion);
        QuoteResponseValidator.validateQuoteStringField(response, "revision", expectedVersion);
        logger.info("Quote version number validation completed");
    }
    
    @And("the quote status is {string}")
    public void validateQuoteStatus(String expectedStatus) {
        logger.info("Validating quote status: {}", expectedStatus);
        QuoteResponseValidator.validateQuoteStringField(response, "status", expectedStatus);
        logger.info("Quote status validation completed");
    }

    @And("the error message is {string}")
    public void validateErrorMessage(String expectedMsg) {
        logger.info("Validating error message: {}", expectedMsg);
        ResponseValidator.validateErrorMessage(response, expectedMsg);
        logger.info("Error message validation completed");
    }

    @And("the response time is ≤ {int}ms")
    public void validateResponseTime(int maxTime) {
        logger.info("Validating response time: {}ms (max: {}ms)", responseTime, maxTime);
        Assert.assertTrue(responseTime <= maxTime, 
            "Response time exceeds " + maxTime + "ms. Actual time: " + responseTime + "ms");
        logger.info("Response time validation completed");
    }
    
    @And("the average response time over {int} requests is ≤ {int}ms")
    public void validateAverageResponseTime(int requestCount, int maxAvgTime) {
        logger.info("Performing performance test with {} requests, max average time: {}ms", requestCount, maxAvgTime);
        
        long totalTime = 0;
        int successCount = 0;
        
        for (int i = 0; i < requestCount; i++) {
            try {
                // Create a new quote request for each test
                QuoteRequest request = new QuoteRequest();
                request.setCustomer(customerId != null && !customerId.isEmpty() ? customerId : "PERF_TEST_CUST_" + i);
                
                // Add a sample item
                Item item = ItemBuilder.builder()
                    .item("PERF_ITEM_" + i)
                    .quantity(new java.math.BigDecimal("1.0"))
                    .unitaryPrice(new java.math.BigDecimal("100.0"))
                    .build();
                
                List<Item> itemList = new ArrayList<>();
                itemList.add(item);
                request.setItems(itemList);
                
                // Execute the request
                response = quoteService.createQuote(request);
                long currentResponseTime = quoteService.getLastResponseTime();
                totalTime += currentResponseTime;
                successCount++;
                
                logger.debug("Performance test request {}: {}ms", i + 1, currentResponseTime);
            } catch (Exception e) {
                logger.warn("Performance test request {} failed: {}", i + 1, e.getMessage());
            }
        }
        
        if (successCount > 0) {
            double avgResponseTime = (double) totalTime / successCount;
            logger.info("Performance test completed. Average response time: {}ms over {} successful requests", 
                       avgResponseTime, successCount);
            
            Assert.assertTrue(avgResponseTime <= maxAvgTime,
                "Average response time exceeds " + maxAvgTime + "ms. Actual average: " + String.format("%.2f", avgResponseTime) + "ms");
        } else {
            Assert.fail("All performance test requests failed");
        }
    }
    
    @And("the concurrent performance test with {int} threads for {int} seconds shows average response time ≤ {int}ms")
    public void validateConcurrentPerformance(int threadCount, int durationSeconds, int maxAvgTime) {
        logger.info("Performing concurrent performance test with {} threads for {} seconds, max average time: {}ms", 
                   threadCount, durationSeconds, maxAvgTime);
        
        try {
            // Create a supplier for quote requests
            java.util.function.Supplier<QuoteRequest> requestSupplier = () -> {
                QuoteRequest request = new QuoteRequest();
                request.setCustomer("CONCURRENT_TEST_CUST");
                
                Item item = ItemBuilder.builder()
                    .item("CONCURRENT_TEST_ITEM")
                    .quantity(new java.math.BigDecimal("1.0"))
                    .unitaryPrice(new java.math.BigDecimal("100.0"))
                    .build();
                
                List<Item> itemList = new ArrayList<>();
                itemList.add(item);
                request.setItems(itemList);
                
                return request;
            };
            
            // Execute concurrent test
            PerformanceTester.PerformanceResult result = PerformanceTester.executeConcurrentTest(
                quoteService, 
                requestSupplier, 
                threadCount, 
                durationSeconds * 1000L
            );
            
            logger.info("Concurrent performance test results:\n{}", result);
            
            // Validate average response time
            Assert.assertTrue(result.getAvgResponseTime() <= maxAvgTime,
                "Average response time exceeds " + maxAvgTime + "ms. Actual average: " + String.format("%.2f", result.getAvgResponseTime()) + "ms");
            
            // Also validate that we had successful requests
            Assert.assertTrue(result.getSuccessCount() > 0,
                "No successful requests in concurrent performance test");
        } catch (Exception e) {
            logger.error("Error during concurrent performance test: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to execute concurrent performance test: " + e.getMessage(), e);
        }
    }
    
    /**
     * Helper method to update item properties from data map
     * @param itemData Data map containing item properties
     * @param propertyName Name of the property to update
     * @param updateFunction Function to apply the update
     * @param logFunction Function to log the update
     */
    private void updateItemProperty(Map<String, String> itemData, String propertyName, 
                                  java.util.function.Consumer<BigDecimal> updateFunction,
                                  java.util.function.BiConsumer<String, BigDecimal> logFunction) {
        if (itemData.containsKey(propertyName)) {
            try {
                BigDecimal value = new BigDecimal(itemData.get(propertyName));
                updateFunction.accept(value);
                logFunction.accept(propertyName, value);
            } catch (NumberFormatException e) {
                logger.warn("Invalid numeric format for property {}: {}", propertyName, itemData.get(propertyName));
            }
        }
    }
    
    /**
     * Helper method to update an item from data map
     * @param itemData Data map containing item properties
     * @param item Item to update
     * @param index Index of the item (for logging)
     */
    private void updateItemFromData(Map<String, String> itemData, Item item, int index) {
        updateItemProperty(itemData, TestConstants.QUANTITY_FIELD, 
            value -> item.setQuantity(value), 
            (prop, val) -> logger.debug("Updated item {} quantity to: {}", index, val));
            
        updateItemProperty(itemData, TestConstants.UNITARY_PRICE_FIELD, 
            value -> item.setUnitaryPrice(value), 
            (prop, val) -> logger.debug("Updated item {} unitary price to: {}", index, val));
            
        updateItemProperty(itemData, TestConstants.DISCOUNT_PERCENTAGE_FIELD, 
            value -> item.setDiscountPercentage(value), 
            (prop, val) -> logger.debug("Updated item {} discount percentage to: {}", index, val));
    }

}