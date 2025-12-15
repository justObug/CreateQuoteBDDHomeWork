Feature: Create New Quote
  As a sales representative
  I want to create a new quote for a customer
  So that I can provide them with pricing and product details quickly

  #AC1: Successfully create a new quote with one item for a customer without discount
  Scenario: Successfully create a new quote with one item for a customer without discount- AC1
    Given a customer with identifier "CUST_001"
    And one item with identifier "ITEM_001"
    When I create a quote for that customer with that item with the quantity 5.0 and the price 100.0
    Then it returns the quote with the correct details, including the total line price calculated as 500.0
    And a confirmation message "Quote created successfully."

  # AC2: Successfully create a new quote with one item with discount for a customer
  Scenario: Successfully create a new quote with one item with discount for a customer - AC2
    Given a customer with identifier "CUST_002"
    And one item with identifier "ITEM_002"
    When I create a quote for that customer with that item with the quantity 3.0, the unitary price 50.0, and a percentage of discount 0.1
    Then it returns the quote with the correct details, including the discount amount 15.0 and the total line price 135.0
    And a confirmation message "Quote created successfully."

  # AC3: Successfully create a new quote with two items for a customer
  Scenario: Successfully create a new quote with two items for a customer - AC3
    Given a customer with identifier "CUST_003"
    And two items "ITEM_3" and "ITEM_4"
    When I create a quote for that customer with item "ITEM_3" with the quantity 2.0 and the price 25.0 and item "ITEM_4" with the quantity 3.0 and the price 15.0
    Then it returns the quote with the correct details, including 2 lines and the quote's total price calculated as 95.0
    And a confirmation message "Quote created successfully."

  # AC4: Successfully create a new quote with three items for a customer with mixed discount percentage
  Scenario: Successfully create a new quote with three items for a customer with mixed discount percentage - AC4
    Given a customer with identifier "CUST_004"
    And three items "ITEM_5", "ITEM_6" and "ITEM_7"
    When I create a quote for that customer with that item:
      | items.item | quantity | unitaryPrice | discountPercentage |
      | ITEM_5     | 1.0      | 100.0        | 0.1                |
      | ITEM_6     | 2.0      | 50.0         | 0.0                |
      | ITEM_7     | 3.0      | 25.0         | 0.2                |
    Then it returns the quote with the correct details, including 3 lines and the quote's total price calculated as 250.0
    And a confirmation message "Quote created successfully."

  # AC5: Successfully create a new quote with 100% discount
  Scenario: Successfully create a new quote with 100% discount - AC5
    Given a customer with identifier "CUST_005"
    And one item with identifier "ITEM_005"
    When I create a quote for that customer with that item with the quantity 2.0, the unitary price 150.0, and a percentage of discount 1.0
    Then it returns the quote with the correct details, including the discount amount 300.0 and the total line price 0.0
    And a confirmation message "Quote created successfully."

  # AC6: Successfully create a new quote with decimal values and zero discount
  Scenario: Successfully create a new quote with decimal values and zero discount - AC6
    Given a customer with identifier "CUST_006"
    And one item with identifier "ITEM_006"
    When I create a quote for that customer with that item with the quantity 2.55, the unitary price 10.5, and a percentage of discount 0.0
    Then it returns the quote with the correct details, including the discount amount 0.0 and the total line price 26.78
    And a confirmation message "Quote created successfully."

  # AC7: Quote version number auto-upgrade (creating revised quotes)
  Scenario: Successfully create a revised quote with auto-incremented version number - AC7
    Given a customer with identifier "CUST_008"
    And one item with identifier "ITEM_008"
    When I create a quote for that customer with that item with the quantity 2.0 and the price 100.0
    Then it returns the quote with the correct details, including the total line price calculated as 200.0
    And the quote version number is "1"
    And the quote status is "active"
    And a confirmation message "Quote created successfully."
    
    When I revise the quote by changing the quantity to 3.0
    Then it returns the quote with the correct details, including the total line price calculated as 300.0
    And the quote version number is "2"
    And the quote status is "active"
    And a confirmation message "Quote revised successfully."

  # AC8: Successfully create a new quote with ten items
  Scenario: Successfully create a new quote with ten items - AC8
    Given a customer with identifier "CUST_007"
    And the following items:
      | item     |
      | ITEM_001 |
      | ITEM_002 |
      | ITEM_003 |
      | ITEM_004 |
      | ITEM_005 |
      | ITEM_006 |
      | ITEM_007 |
      | ITEM_008 |
      | ITEM_009 |
      | ITEM_010 |
    When I create a quote for that customer with that item:
      | items.item | quantity | unitaryPrice | discountPercentage |
      | ITEM_001   | 1.0      | 10.00        | 0.0                |
      | ITEM_002   | 1.0      | 10.00        | 0.0                |
      | ITEM_003   | 1.0      | 10.00        | 0.0                |
      | ITEM_004   | 1.0      | 10.00        | 0.0                |
      | ITEM_005   | 1.0      | 10.00        | 0.0                |
      | ITEM_006   | 1.0      | 10.00        | 0.0                |
      | ITEM_007   | 1.0      | 10.00        | 0.0                |
      | ITEM_008   | 1.0      | 10.00        | 0.0                |
      | ITEM_009   | 1.0      | 10.00        | 0.0                |
      | ITEM_010   | 1.0      | 10.00        | 0.0                |
    Then it returns the quote with the correct details, including 10 lines and the quote's total price calculated as 100.00
    And a confirmation message "Quote created successfully."

  # AC9: Successfully create a new quote with extreme decimal precision (precision validation)
  Scenario: Successfully create a new quote with extreme decimal precision - AC9
    Given a customer with identifier "CUST_009"
    And an item with identifier "ITEM_001"
    When I create a quote for that customer with that item with the quantity 0.123456789, the unitary price 9.876543210123456, and a percentage of discount 0
    Then it returns the quote with the correct details, including the total line price calculated as 1.219323603509262
    And a confirmation message "Quote created successfully."

#  AC10: Successfully create a new quote with minimum quantity value
  Scenario: Create quote with minimum quantity value
    Given a customer with identifier "CUST_010"
    And an item with identifier "ITEM_010"
    When I create a quote for that customer with that item with the quantity 0.001 and the price 100.0
    Then the response status code is 200
    And the quote total amount is 0.1

#  AC11: Successfully create a new quote with maximum quantity value
  Scenario: Create quote with maximum quantity value
    Given a customer with identifier "CUST_011"
    And an item with identifier "ITEM_011"
    When I create a quote for that customer with that item with the quantity 999999.999 and the price 100.0
    Then the response status code is 200
    And the quote total amount is 99999999.9

#  AC12: Successfully create a new quote with invalid discount (>100%)
  Scenario: Create quote with invalid discount (>100%)
    Given a customer with identifier "CUST_012"
    And an item with identifier "ITEM_012"
    When I create a quote for that customer with that item with the quantity 10.0, the unitary price 50.0, and a percentage of discount 150.0
    Then the response status code is 400
    And the error message is "Discount percentage must be between 0 and 100"

#   AC13: Successfully create a new quote with zero price
  Scenario: Create quote with zero price
    Given a customer with identifier "CUST_013"
    And an item with identifier "ITEM_013"
    When I create a quote for that customer with that item with the quantity 5.0 and the price 0.0
    Then the response status code is 200
    And the quote total amount is 0.0

#    AC14: Successfully create a new quote with very high price
  Scenario: Create quote with very high price
    Given a customer with identifier "CUST_014"
    And an item with identifier "ITEM_014"
    When I create a quote for that customer with that item with the quantity 1.0 and the price 999999999.99
    Then the response status code is 200
    And the quote total amount is 999999999.99

    # AC15: Successfully create a new quote with invalid customer identifier
  Scenario: Attempt to create quote with invalid customer identifier
    Given a customer with identifier ""
    And an item with identifier "ITEM_006"
    When I create a quote for that customer with that item with the quantity 5.0 and the price 100.0
    Then the response status code is 400
    And the error message is "Customer or Items cannot be null or empty"

# AC16: Successfully create a new quote with invalid item identifier
  Scenario: Attempt to create quote with invalid item identifier
    Given a customer with identifier "CUST_008"
    And an item with identifier ""
    When I create a quote for that customer with that item with the quantity 5.0 and the price 100.0
    Then the response status code is 400
    And the error message is "Customer or Items cannot be null or empty"

    # AC17: Successfully create a new quote with negative quantity
  Scenario: Attempt to create quote with negative quantity
    Given a customer with identifier "CUST_017"
    And an item with identifier "ITEM_017"
    When I create a quote for that customer with that item with the quantity -5.0 and the price 100.0
    Then the response status code is 400
    And the error message is "Quantity must be greater than zero"

     # AC18: Successfully create a new quote with negative price
  Scenario: Attempt to create quote with negative price
    Given a customer with identifier "CUST_010"
    And an item with identifier "ITEM_010"
    When I create a quote for that customer with that item with the quantity 5.0 and the price -100.0
    Then the response status code is 400
    And the error message is "Price must be greater than zero"

    # AC19: Successfully create a new quote with zero quantity
  Scenario: Attempt to create quote with zero quantity
    Given a customer with identifier "CUST_019"
    And an item with identifier "ITEM_019"
    When I create a quote for that customer with that item with the quantity 0.0 and the price 100.0
    Then the response status code is 400
    And the error message is "Quantity must be greater than zero"

     # AC20: Successfully create a new quote without authorization
  Scenario: Attempt to create quote without proper authorization
    Given an unauthorized user (no token)
    And a customer with identifier "CUST_007"
    And an item with identifier "ITEM_007"
    When I create a quote for that customer with that item with the quantity 5.0 and the price 100.0
    Then the response status code is 401
    And the error message is "Unauthorized access"









    