package org.eurofins.constants;

/**
 * Constants used throughout the test framework
 */
public class TestConstants {
    // API Endpoints
    public static final String HEALTH_CHECK_ENDPOINT = "/api/Quotes/isalive";
    public static final String CREATE_QUOTE_ENDPOINT = "/api/Quotes/create";
    public static final String REVISE_QUOTE_ENDPOINT = "/api/Quotes/revise";
    
    // Default values
    public static final String DEFAULT_CUSTOMER = "DEFAULT_CUSTOMER";
    public static final String DEFAULT_ITEM = "DEFAULT_ITEM";
    public static final String VALID_TOKEN = "VALID_TOKEN";
    
    // Field names
    public static final String CUSTOMER_FIELD = "customer";
    public static final String ITEMS_FIELD = "items";
    public static final String ITEM_ID_FIELD = "item";
    public static final String QUANTITY_FIELD = "quantity";
    public static final String UNITARY_PRICE_FIELD = "unitaryPrice";
    public static final String DISCOUNT_PERCENTAGE_FIELD = "discountPercentage";
    
    // HTTP Headers
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    
    // Configuration
    public static final String CONFIG_FILE = "application.properties";
    public static final String BASE_URL_PROPERTY = "api.base.url";
}