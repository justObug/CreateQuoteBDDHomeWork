package org.eurofins.utils;

import org.eurofins.service.QuoteApiService;
import org.eurofins.model.QuoteRequest;
import org.eurofins.model.Item;
import org.eurofins.model.ItemBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Utility class for performance testing
 */
public class PerformanceTester {
    
    private static final Logger logger = LoggerFactory.getLogger(PerformanceTester.class);
    
    /**
     * Performance test result
     */
    public static class PerformanceResult {
        private final long minResponseTime;
        private final long maxResponseTime;
        private final double avgResponseTime;
        private final int successCount;
        private final int failureCount;
        private final int totalCount;
        
        public PerformanceResult(long minResponseTime, long maxResponseTime, double avgResponseTime, 
                               int successCount, int failureCount, int totalCount) {
            this.minResponseTime = minResponseTime;
            this.maxResponseTime = maxResponseTime;
            this.avgResponseTime = avgResponseTime;
            this.successCount = successCount;
            this.failureCount = failureCount;
            this.totalCount = totalCount;
        }
        
        // Getters
        public long getMinResponseTime() { return minResponseTime; }
        public long getMaxResponseTime() { return maxResponseTime; }
        public double getAvgResponseTime() { return avgResponseTime; }
        public int getSuccessCount() { return successCount; }
        public int getFailureCount() { return failureCount; }
        public int getTotalCount() { return totalCount; }
        
        @Override
        public String toString() {
            return String.format(
                "Performance Results:\n" +
                "  Total Requests: %d\n" +
                "  Successful: %d\n" +
                "  Failed: %d\n" +
                "  Min Response Time: %d ms\n" +
                "  Max Response Time: %d ms\n" +
                "  Avg Response Time: %.2f ms\n" +
                "  Success Rate: %.2f%%",
                totalCount, successCount, failureCount, minResponseTime, maxResponseTime, 
                avgResponseTime, (successCount * 100.0 / totalCount)
            );
        }
    }
    
    /**
     * Execute a single performance test
     * @param service Quote API service
     * @param request Quote request
     * @return Response time in milliseconds
     */
    public static long executeSingleTest(QuoteApiService service, QuoteRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            service.createQuote(request);
            long responseTime = service.getLastResponseTime();
            logger.debug("Single performance test completed in {} ms", responseTime);
            return responseTime;
        } catch (Exception e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.warn("Performance test failed after {} ms: {}", elapsedTime, e.getMessage());
            return elapsedTime;
        }
    }
    
    /**
     * Execute concurrent performance tests
     * @param service Quote API service
     * @param requestSupplier Supplier for creating quote requests
     * @param concurrencyLevel Number of concurrent threads
     * @param testDuration Duration of test in milliseconds
     * @return Performance results
     */
    public static PerformanceResult executeConcurrentTest(
            QuoteApiService service, 
            Supplier<QuoteRequest> requestSupplier, 
            int concurrencyLevel, 
            long testDuration) {
        
        logger.info("Starting concurrent performance test with {} threads for {} seconds", concurrencyLevel, testDuration/1000);
        
        ExecutorService executor = Executors.newFixedThreadPool(concurrencyLevel);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        List<Long> responseTimes = new ArrayList<>();
        // Use AtomicInteger for thread-safe counting
        java.util.concurrent.atomic.AtomicInteger successCount = new java.util.concurrent.atomic.AtomicInteger(0);
        java.util.concurrent.atomic.AtomicInteger failureCount = new java.util.concurrent.atomic.AtomicInteger(0);
        
        long testStartTime = System.currentTimeMillis();
        
        // Submit concurrent tasks
        for (int i = 0; i < concurrencyLevel; i++) {
            final int threadId = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                long threadStartTime = System.currentTimeMillis();
                int threadRequestCount = 0;
                
                logger.debug("Thread {} started", threadId);
                
                while (System.currentTimeMillis() - threadStartTime < testDuration) {
                    try {
                        QuoteRequest request = requestSupplier.get();
                        long responseTime = executeSingleTest(service, request);
                        
                        synchronized (responseTimes) {
                            responseTimes.add(responseTime);
                        }
                        
                        successCount.incrementAndGet();
                        threadRequestCount++;
                        
                        // Log progress every 100 requests
                        if (threadRequestCount % 100 == 0) {
                            logger.debug("Thread {} completed {} requests", threadId, threadRequestCount);
                        }
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        logger.warn("Concurrent test failed in thread {}: {}", threadId, e.getMessage());
                    }
                    
                    // Small delay to prevent overwhelming the service
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.warn("Thread {} interrupted", threadId);
                        break;
                    }
                }
                
                long threadElapsedTime = System.currentTimeMillis() - threadStartTime;
                logger.debug("Thread {} finished after {} ms with {} requests", threadId, threadElapsedTime, threadRequestCount);
            }, executor);
            
            futures.add(future);
        }
        
        logger.info("Submitted {} concurrent tasks, waiting for completion...", futures.size());
        
        // Wait for all tasks to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();
        
        long totalElapsedTime = System.currentTimeMillis() - testStartTime;
        
        // Calculate statistics
        if (responseTimes.isEmpty()) {
            logger.warn("No successful requests completed during performance test");
            return new PerformanceResult(0, 0, 0, successCount.get(), failureCount.get(), successCount.get() + failureCount.get());
        }
        
        long minResponseTime = responseTimes.stream().mapToLong(Long::longValue).min().orElse(0);
        long maxResponseTime = responseTimes.stream().mapToLong(Long::longValue).max().orElse(0);
        double avgResponseTime = responseTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        
        PerformanceResult result = new PerformanceResult(
            minResponseTime, maxResponseTime, avgResponseTime, 
            successCount.get(), failureCount.get(), successCount.get() + failureCount.get()
        );
        
        logger.info("Concurrent performance test completed in {} ms", totalElapsedTime);
        logger.info("Results: {}", result.toString().replace("\n", ", "));
        
        return result;
    }
    
    /**
     * Create a sample quote request for testing
     * @param customerId Customer ID
     * @param itemCount Number of items to include
     * @return QuoteRequest object
     */
    public static QuoteRequest createSampleQuoteRequest(String customerId, int itemCount) {
        QuoteRequest request = new QuoteRequest();
        request.setCustomer(customerId);
        
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            Item item = ItemBuilder.builder()
                .item("ITEM" + i)
                .quantity(new java.math.BigDecimal(String.valueOf(1 + i)))
                .unitaryPrice(new java.math.BigDecimal(String.valueOf(100 + (i * 10))))
                .discountPercentage(new java.math.BigDecimal(String.valueOf(i * 5)))
                .build();
            items.add(item);
        }
        
        request.setItems(items);
        return request;
    }
}