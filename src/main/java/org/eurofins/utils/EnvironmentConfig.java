package org.eurofins.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Environment configuration utility to support different environments
 */
public class EnvironmentConfig {
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentConfig.class);
    
    private static final String ENVIRONMENT_PROPERTY = "environment";
    private static final String DEFAULT_ENVIRONMENT = "test";
    
    private final Properties properties;
    
    public EnvironmentConfig() {
        properties = new Properties();
        loadEnvironmentProperties();
    }
    
    private void loadEnvironmentProperties() {
        String environment = System.getProperty(ENVIRONMENT_PROPERTY, DEFAULT_ENVIRONMENT);
        logger.info("Loading configuration for environment: {}", environment);
        
        try {
            // Load environment-specific properties
            String configFile = "application-" + environment + ".properties";
            var inputStream = EnvironmentConfig.class.getClassLoader().getResourceAsStream(configFile);
            
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Loaded environment-specific properties from: {}", configFile);
            } else {
                logger.warn("Environment-specific configuration file not found: {}, using default configuration", configFile);
            }
        } catch (Exception e) {
            logger.warn("Error loading environment-specific configuration: {}", e.getMessage());
        }
    }
    
    /**
     * Get a property value
     * @param key Property key
     * @param defaultValue Default value if property is not found
     * @return Property value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get the current environment
     * @return Current environment name
     */
    public static String getCurrentEnvironment() {
        return System.getProperty(ENVIRONMENT_PROPERTY, DEFAULT_ENVIRONMENT);
    }
}