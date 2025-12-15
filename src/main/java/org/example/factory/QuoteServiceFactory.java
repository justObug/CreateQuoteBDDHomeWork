package org.example.factory;

import org.example.service.QuoteApiService;

/**
 * Factory class for creating QuoteApiService instances
 */
public class QuoteServiceFactory {
    
    /**
     * Create a default QuoteApiService instance
     * @return QuoteApiService instance
     */
    public static QuoteApiService createService() {
        return new QuoteApiService();
    }
    
    /**
     * Create a QuoteApiService instance with custom authorization
     * @param isAuthorized Authorization status
     * @return QuoteApiService instance
     */
    public static QuoteApiService createService(boolean isAuthorized) {
        return new QuoteApiService(isAuthorized);
    }
}