package org.eurofins.service;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.eurofins.constants.TestConstants;
import org.eurofins.model.QuoteRequest;
import org.eurofins.utils.ConfigurationUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class to handle Quote API interactions
 */
public class QuoteApiService {
    
    private static final Logger logger = LoggerFactory.getLogger(QuoteApiService.class);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    private final String baseUrl;
    private boolean isAuthorized;
    private long lastResponseTime;
    
    public QuoteApiService() {
        this.baseUrl = ConfigurationUtils.loadBaseUrl();
        this.isAuthorized = true;
        this.lastResponseTime = 0;
    }
    
    public QuoteApiService(boolean isAuthorized) {
        this.baseUrl = ConfigurationUtils.loadBaseUrl();
        this.isAuthorized = isAuthorized;
        this.lastResponseTime = 0;
    }
    
    /**
     * Initialize a request with default settings
     * @return RequestSpecification object
     */
    public RequestSpecification initRequest() {
        logger.debug("Initializing request with base URL: {}", baseUrl);
        
        RequestSpecification request = RestAssured.given()
                .baseUri(baseUrl)
                .contentType(TestConstants.APPLICATION_JSON);
        
        // Add authorization header if authorized
        if (isAuthorized) {
            request.header(TestConstants.AUTHORIZATION_HEADER, "Bearer " + TestConstants.VALID_TOKEN);
            logger.debug("Added authorization header to request");
        }
        
        return request;
    }
    
    /**
     * Create a quote with the given request
     * @param request Quote request object
     * @return Response from the API
     */
    public Response createQuote(QuoteRequest request) {
        try {
            String requestBody = gson.toJson(request);
            logger.info("Creating quote for customer: {}", request.getCustomer());
            logger.debug("Request body: {}", requestBody);
            
            RequestSpecification req = initRequest();
            
            long startTime = System.currentTimeMillis();
            Response response = req.body(requestBody)
                    .post(baseUrl + TestConstants.CREATE_QUOTE_ENDPOINT);
            long endTime = System.currentTimeMillis();
            
            lastResponseTime = endTime - startTime;
            logger.info("Create quote response status: {}, response time: {}ms", 
                       response.getStatusCode(), lastResponseTime);
            logger.debug("Response body: {}", response.asString());
            
            return response;
        } catch (Exception e) {
            logger.error("Error creating quote: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create quote", e);
        }
    }
    
    /**
     * Revise an existing quote
     * @param quoteId ID of the quote to revise
     * @param request Quote request object with revised data
     * @return Response from the API
     */
    public Response reviseQuote(String quoteId, QuoteRequest request) {
        try {
            String requestBody = gson.toJson(request);
            logger.info("Revising quote ID: {} for customer: {}", quoteId, request.getCustomer());
            logger.debug("Request body: {}", requestBody);
            
            RequestSpecification req = initRequest();
            
            long startTime = System.currentTimeMillis();
            Response response = req.body(requestBody)
                    .post(baseUrl + TestConstants.REVISE_QUOTE_ENDPOINT);
            long endTime = System.currentTimeMillis();
            
            lastResponseTime = endTime - startTime;
            logger.info("Revise quote response status: {}, response time: {}ms", 
                       response.getStatusCode(), lastResponseTime);
            logger.debug("Response body: {}", response.asString());
            
            return response;
        } catch (Exception e) {
            logger.error("Error revising quote: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to revise quote", e);
        }
    }
    
    /**
     * Check if the service is alive
     * @return Response from the health check endpoint
     */
    public Response checkHealth() {
        try {
            logger.debug("Checking service health at: {}", baseUrl + TestConstants.HEALTH_CHECK_ENDPOINT);
            
            long startTime = System.currentTimeMillis();
            Response response = RestAssured.get(baseUrl + TestConstants.HEALTH_CHECK_ENDPOINT);
            long endTime = System.currentTimeMillis();
            
            lastResponseTime = endTime - startTime;
            logger.info("Health check response status: {}, response time: {}ms", 
                       response.getStatusCode(), lastResponseTime);
            return response;
        } catch (Exception e) {
            logger.error("Error checking service health: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to check service health", e);
        }
    }
    
    /**
     * Get the response time of the last API call
     * @return Response time in milliseconds
     */
    public long getLastResponseTime() {
        return lastResponseTime;
    }
    
    /**
     * Set authorization status
     * @param authorized true if authorized, false otherwise
     */
    public void setAuthorized(boolean authorized) {
        this.isAuthorized = authorized;
        logger.debug("Authorization status set to: {}", authorized);
    }
}