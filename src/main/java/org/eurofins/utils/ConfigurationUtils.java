package org.eurofins.utils;

import org.eurofins.constants.TestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationUtils {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationUtils.class);
    
    private static final String CONFIG_FILE = TestConstants.CONFIG_FILE;
    private static final String BASE_URL_PROPERTY = TestConstants.BASE_URL_PROPERTY;
    private static final String DEFAULT_BASE_URL = "https://7a40222f-86f7-48b2-a8c3-3dde76ed1077.mock.pstmn.io";
    
    /**
     * Load base URL from configuration file
     * @return base URL string
     */
    public static String loadBaseUrl() {
        try {
            EnvironmentConfig envConfig = new EnvironmentConfig();
            String baseUrl = envConfig.getProperty(BASE_URL_PROPERTY, null);
            
            if (baseUrl != null) {
                logger.info("Using base URL from environment configuration: {}", baseUrl);
                return baseUrl;
            }
            
            // Fallback to default configuration file
            try (InputStream input = ConfigurationUtils.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
                Properties prop = new Properties();
                if (input == null) {
                    logger.warn("Unable to find {}. Using default URL: {}", CONFIG_FILE, DEFAULT_BASE_URL);
                    return DEFAULT_BASE_URL;
                }
                prop.load(input);
                String url = prop.getProperty(BASE_URL_PROPERTY, DEFAULT_BASE_URL);
                logger.info("Loaded base URL from {}: {}", CONFIG_FILE, url);
                return url;
            }
        } catch (IOException ex) {
            logger.error("Error loading configuration. Using default URL: {}", ex.getMessage());
            return DEFAULT_BASE_URL;
        }
    }
}