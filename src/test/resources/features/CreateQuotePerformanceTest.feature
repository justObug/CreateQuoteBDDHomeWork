Feature: Quote Service Performance Testing

  Scenario: Single quote creation response time should be under 1 seconds
    Given a customer with identifier "PERF_CUST_001"
    And an item with identifier "PERFITEM_002"
    When I create a quote for that customer with that item with the quantity 5.0 and the price 100.0
    Then the response status code is 200
    And the response time is ≤ 1000ms

  Scenario: Average response time for 10 quote creation requests should be under 1.5 seconds
    Given a customer with identifier "PERF_CUST_002"
    And an item with identifier "PERF_ITEM_002"
    When I create a quote for that customer with that item with the quantity 1.0 and the price 50.0
    Then the response status code is 200
    And the average response time over 10 requests is ≤ 1500ms

  Scenario: Concurrent quote creation with 5 threads for 10 seconds should have average response time under 2 seconds
    Given a customer with identifier "PERF_CUST_003"
    And an item with identifier "PERF_ITEM_003"
    When I create a quote for that customer with that item with the quantity 2.0 and the price 75.0
    Then the response status code is 200
    And the concurrent performance test with 5 threads for 10 seconds shows average response time ≤ 2000ms

  Scenario: Quote creation with 5 items should have response time under 3 seconds
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