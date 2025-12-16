package org.eurofins.utils;

import io.restassured.response.Response;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Utility class for validating quote-specific API responses
 */
public class QuoteResponseValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(QuoteResponseValidator.class);
    
    /**
     * Validate quote total price
     * @param response API response
     * @param expectedTotal Expected total price
     */
    public static void validateQuoteTotalPrice(Response response, double expectedTotal) {
        try {
            Object totalPriceObj = response.jsonPath().get("quote.totalPrice");
            
            // Handle case where totalPrice might be missing or null
            if (totalPriceObj == null) {
                throw new AssertionError("Total price not found in response");
            }
            
            String totalPriceStr = totalPriceObj.toString();
            
            // Use String constructor for BigDecimal to maintain precision
            BigDecimal actualTotal = new BigDecimal(totalPriceStr);
            BigDecimal expectedTotalDecimal = new BigDecimal(String.valueOf(expectedTotal));
            
            logger.debug("Validating quote total price. Expected: {}, Actual: {}", expectedTotalDecimal, actualTotal);
            Assert.assertEquals(actualTotal.compareTo(expectedTotalDecimal), 0, "Quote total price mismatch");
        } catch (AssertionError e) {
            logger.error("Total price validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error validating quote total price: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to validate quote total price: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validate quote total price with precise BigDecimal comparison
     * @param response API response
     * @param expectedTotal Expected total price as BigDecimal
     */
    public static void validateQuoteTotalPricePrecise(Response response, BigDecimal expectedTotal) {
        try {
            Object totalPriceObj = response.jsonPath().get("quote.totalPrice");
            
            // Handle case where totalPrice might be missing or null
            if (totalPriceObj == null) {
                throw new AssertionError("Total price not found in response");
            }
            
            String totalPriceStr = totalPriceObj.toString();
            
            // Use String constructor for BigDecimal to maintain precision
            BigDecimal actualTotal = new BigDecimal(totalPriceStr);
            
            logger.debug("Validating quote total price. Expected: {}, Actual: {}", expectedTotal, actualTotal);
            Assert.assertEquals(actualTotal.compareTo(expectedTotal), 0, "Quote total price mismatch");
        } catch (AssertionError e) {
            logger.error("Total price validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error validating quote total price: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to validate quote total price: " + e.getMessage(), e);
        }
    }

    /**
     * Validate quote discount amount
     * @param response API response
     * @param expectedDiscount Expected discount amount
     */
    public static void validateQuoteDiscountAmount(Response response, double expectedDiscount) {
        try {
            // Get the lines array from the response
            List<Map<String, Object>> quoteLines = response.jsonPath().getList("quote.lines");
            
            if (quoteLines == null || quoteLines.isEmpty()) {
                throw new AssertionError("No quote lines found in response");
            }
            
            // Calculate total discount amount from all lines
            BigDecimal totalDiscount = BigDecimal.ZERO;
            for (Map<String, Object> line : quoteLines) {
                Object discountObj = line.get("discountAmount");
                if (discountObj != null) {
                    // Convert to string and then to BigDecimal
                    String discountStr = discountObj.toString();
                    totalDiscount = totalDiscount.add(new BigDecimal(discountStr));
                }
            }
            
            BigDecimal expectedDiscountDecimal = new BigDecimal(String.valueOf(expectedDiscount));
            logger.debug("Validating total discount amount. Expected: {}, Actual: {}", expectedDiscountDecimal, totalDiscount);
            Assert.assertEquals(totalDiscount.compareTo(expectedDiscountDecimal), 0, "Quote discount amount mismatch");
        } catch (AssertionError e) {
            logger.error("Discount amount validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error validating quote discount amount: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to validate quote discount amount: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validate quote string fields
     * @param response API response
     * @param fieldName Name of the field to validate
     * @param expectedValue Expected value of the field
     */
    public static void validateQuoteStringField(Response response, String fieldName, String expectedValue) {
        try {
            Object fieldValueObj = response.jsonPath().get("quote." + fieldName);
            
            // Handle case where field might be missing or null
            if (fieldValueObj == null) {
                throw new AssertionError("Field '" + fieldName + "' not found in response");
            }
            
            // Convert to string representation
            String actualValue = fieldValueObj.toString();
            
            logger.debug("Validating field '{}'. Expected: {}, Actual: {}", fieldName, expectedValue, actualValue);
            Assert.assertEquals(actualValue, expectedValue, "Quote field '" + fieldName + "' mismatch");
        } catch (AssertionError e) {
            logger.error("Field '{}' validation failed: {}", fieldName, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error validating quote field '{}': {}", fieldName, e.getMessage(), e);
            throw new RuntimeException("Failed to validate quote field '" + fieldName + "': " + e.getMessage(), e);
        }
    }
    
    /**
     * Validate quote line count
     * @param response API response
     * @param expectedLineCount Expected number of lines
     */
    public static void validateQuoteLineCount(Response response, int expectedLineCount) {
        try {
            List<?> quoteLines = response.jsonPath().getList("quote.lines");
            
            if (quoteLines == null) {
                throw new AssertionError("Quote lines not found in response");
            }
            
            int actualLineCount = quoteLines.size();
            logger.debug("Validating quote line count. Expected: {}, Actual: {}", expectedLineCount, actualLineCount);
            Assert.assertEquals(actualLineCount, expectedLineCount, "Quote line count mismatch");
        } catch (AssertionError e) {
            logger.error("Line count validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error validating quote line count: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to validate quote line count: " + e.getMessage(), e);
        }
    }
}