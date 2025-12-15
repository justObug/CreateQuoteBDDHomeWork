Feature: Quote Service Performance Testing

  Scenario: Single request response time should be acceptable
    Given a customer with identifier "PERF_CUST_001"
    And an item with identifier "PERFITEM_002"
    When I create a quote for that customer with that item with the quantity 5.0 and the price 100.0
    Then the response status code is 200
    And the response time is ≤ 2000ms

  Scenario: Average response time over multiple requests
    Given a customer with identifier "PERF_CUST_002"
    And an item with identifier "PERF_ITEM_002"
    When I create a quote for that customer with that item with the quantity 1.0 and the price 50.0
    Then the response status code is 200
    And the average response time over 10 requests is ≤ 1500ms

  Scenario: High concurrency performance test
    Given a customer with identifier "PERF_CUST_003"
    And an item with identifier "PERF_ITEM_003"
    When I create a quote for that customer with that item with the quantity 2.0 and the price 75.0
    Then the response status code is 200
    And the concurrent performance test with 5 threads for 10 seconds shows average response time ≤ 2000ms

  Scenario: Stress test with large payloads
    Given a customer with identifier "PERF_CUST_004"
    And the following items:
      | item       |
      | ITEM_001   |
      | ITEM_002   |
      | ITEM_003   |
      | ITEM_004   |
      | ITEM_005   |
    When I create a quote for that customer with that item:
      | quantity | unitaryPrice | discountPercentage |
      | 10.0     | 100.0        | 5.0                |
      | 5.0      | 200.0        | 10.0               |
      | 15.0     | 50.0         | 2.5                |
      | 8.0      | 150.0        | 7.5                |
      | 12.0     | 75.0         | 3.0                |
    Then the response status code is 200
    And the response time is ≤ 3000ms
