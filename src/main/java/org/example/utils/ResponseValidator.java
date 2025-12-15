package org.example.utils;

import io.restassured.response.Response;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Utility class for validating API responses
 */
public class ResponseValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(ResponseValidator.class);
    
    /**
     * Validate response status code
     * @param response API response
     * @param expectedStatusCode Expected status code
     */
    public static void validateStatusCode(Response response, int expectedStatusCode) {
        try {
            int actualStatusCode = response.getStatusCode();
            logger.debug("Validating status code. Expected: {}, Actual: {}", expectedStatusCode, actualStatusCode);
            Assert.assertEquals(actualStatusCode, expectedStatusCode, 
                "Response status code mismatch. Expected: " + expectedStatusCode + 
                ", Actual: " + actualStatusCode);
        } catch (AssertionError e) {
            logger.error("Status code validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error validating status code: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to validate status code: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validate customer in response
     * @param response API response
     * @param expectedCustomer Expected customer ID
     */
    public static void validateCustomer(Response response, String expectedCustomer) {
        try {
            String actualCustomer = response.jsonPath().getString("quote.customer");
            logger.debug("Validating customer. Expected: {}, Actual: {}", expectedCustomer, actualCustomer);
            Assert.assertEquals(actualCustomer, expectedCustomer, "Response customer identifier mismatch");
        } catch (AssertionError e) {
            logger.error("Customer validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error validating customer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to validate customer: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validate item details in response
     * @param response API response
     * @param expectedItem Expected item ID
     * @param expectedQuantity Expected quantity
     * @param expectedUnitaryPrice Expected unitary price
     */
    public static void validateItemDetails(Response response, String expectedItem, 
                                         Double expectedQuantity, Double expectedUnitaryPrice) {
        try {
            List<Map<String, Object>> quoteLines = response.jsonPath().getList("quote.lines");
            
            if (quoteLines == null || quoteLines.isEmpty()) {
                throw new AssertionError("No quote lines found in response");
            }
            
            Map<String, Object> targetLine = quoteLines.stream()
                    .filter(line -> {
                        Object item = line.get("item");
                        return item != null && item.equals(expectedItem);
                    })
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("Item " + expectedItem + " not found in quote lines"));
            
            if (expectedQuantity != null) {
                Number actualQuantityNum = (Number) targetLine.get("quantity");
                if (actualQuantityNum == null) {
                    throw new AssertionError("Quantity not found in response for item " + expectedItem);
                }
                double actualQuantity = actualQuantityNum.doubleValue();
                logger.debug("Validating quantity. Expected: {}, Actual: {}", expectedQuantity, actualQuantity);
                Assert.assertEquals(actualQuantity, expectedQuantity, 0.01, "Item quantity mismatch");
            }
            
            if (expectedUnitaryPrice != null) {
                Number actualUnitaryPriceNum = (Number) targetLine.get("unitaryPrice");
                if (actualUnitaryPriceNum == null) {
                    throw new AssertionError("Unitary price not found in response for item " + expectedItem);
                }
                double actualUnitaryPrice = actualUnitaryPriceNum.doubleValue();
                logger.debug("Validating unitary price. Expected: {}, Actual: {}", expectedUnitaryPrice, actualUnitaryPrice);
                Assert.assertEquals(actualUnitaryPrice, expectedUnitaryPrice, 0.01, "Item unitaryPrice mismatch");
            }
        } catch (AssertionError e) {
            logger.error("Item details validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error validating item details: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to validate item details: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validate error message in response
     * @param response API response
     * @param expectedMessage Expected error message
     */
    public static void validateErrorMessage(Response response, String expectedMessage) {
        try {
            String actualMsg = response.jsonPath().getString("errorMessage");
            logger.debug("Validating error message. Expected: {}, Actual: {}", expectedMessage, actualMsg);
            Assert.assertEquals(actualMsg, expectedMessage, "Error message mismatch");
        } catch (AssertionError e) {
            logger.error("Error message validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error validating error message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to validate error message: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validate confirmation message in response
     * @param response API response
     * @param expectedMessage Expected confirmation message
     */
    public static void validateConfirmationMessage(Response response, String expectedMessage) {
        try {
            String actualMsg = response.jsonPath().getString("confirmation.message");
            logger.debug("Validating confirmation message. Expected: {}, Actual: {}", expectedMessage, actualMsg);
            Assert.assertEquals(actualMsg, expectedMessage, "Confirmation message mismatch");
        } catch (AssertionError e) {
            logger.error("Confirmation message validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error validating confirmation message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to validate confirmation message: " + e.getMessage(), e);
        }
    }
}