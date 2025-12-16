Feature: Create New Quote
  As a sales representative
  I want to create a new quote for a customer
  So that I can provide them with pricing and product details quickly

  #AC1: Successfully create a new quote with one item for a customer
  Scenario: Successfully create a new quote with one item for a customer
    Given a customer with identifier "CUST_001"
    And one item with identifier "ITEM_001"
    When I create a quote for that customer with that item with the quantity 5.0 and the price 100.0
    Then it returns the quote with the correct details, including the total line price calculated as 500.0
    And a confirmation message "Quote created successfully."

  # AC2: Successfully create a new quote with one item with discount for a customer
  Scenario: Successfully create a new quote with one item with discount for a customer
    Given a customer with identifier "CUST_002"
    And one item with identifier "ITEM_002"
    When I create a quote for that customer with that item with the quantity 3.0, the unitary price 50.0, and a percentage of discount 0.1
    Then it returns the quote with the correct details, including the discount amount 15.0 and the total line price 135.0
    And a confirmation message "Quote created successfully."

  # AC3: Successfully create a new quote with two items for a customer
  Scenario: Successfully create a new quote with two items for a customer
    Given a customer with identifier "CUST_003"
    And two items "ITEM_3" and "ITEM_4"
    When I create a quote for that customer with item "ITEM_3" with the quantity 2.0 and the price 25.0 and item "ITEM_4" with the quantity 3.0 and the price 15.0
    Then it returns the quote with the correct details, including 2 lines and the quote's total price calculated as 95.0
    And a confirmation message "Quote created successfully."

  # AC4: Successfully create a new quote with three items for a customer with mixed discount percentage
  Scenario: Successfully create a new quote with three items for a customer with mixed discount percentage
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
  Scenario: Successfully create a new quote with 100% discount
    Given a customer with identifier "CUST_005"
    And one item with identifier "ITEM_005"
    When I create a quote for that customer with that item with the quantity 2.0, the unitary price 150.0, and a percentage of discount 1.0
    Then it returns the quote with the correct details, including the discount amount 300.0 and the total line price 0.0
    And a confirmation message "Quote created successfully."

  # AC6: Successfully create a new quote with decimal values and zero discount
  Scenario: Successfully create a new quote with decimal values and zero discount
    Given a customer with identifier "CUST_006"
    And one item with identifier "ITEM_006"
    When I create a quote for that customer with that item with the quantity 2.55, the unitary price 10.5, and a percentage of discount 0.0
    Then it returns the quote with the correct details, including the discount amount 0.0 and the total line price 26.775
    And a confirmation message "Quote created successfully."

  # AC7: Quote version number auto-upgrade (creating revised quotes)
  Scenario: Successfully create a revised quote with auto-incremented version number
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
  Scenario: Successfully create a new quote with ten items
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
  Scenario: Successfully create a new quote with extreme decimal precision
    Given a customer with identifier "CUST_009"
    And an item with identifier "ITEM_001"
    When I create a quote for that customer with that item with the quantity 0.123456789, the unitary price 9.876543210123456, and a percentage of discount 0
    Then it returns the quote with the correct details, including the total line price calculated as 1.2193236
    And a confirmation message "Quote created successfully."

#  AC10: Successfully create a new quote with minimum quantity value
  Scenario: Successfully create quote with minimum quantity value
    Given a customer with identifier "CUST_010"
    And an item with identifier "ITEM_010"
    When I create a quote for that customer with that item with the quantity 0.001 and the price 100.0
    Then the response status code is 200
    And the quote total amount is 0.1

#  AC11: Successfully create a new quote with maximum quantity value
  Scenario: Successfully Create quote with maximum quantity value
    Given a customer with identifier "CUST_011"
    And an item with identifier "ITEM_011"
    When I create a quote for that customer with that item with the quantity 999999.999 and the price 100.0
    Then the response status code is 200
    And the quote total amount is "99999999.9"

#  AC12: Fail to create a new quote with invalid discount (>100%)
  Scenario: Fail to create a new quote with invalid discount (>100%)
    Given a customer with identifier "CUST_012"
    And an item with identifier "ITEM_012"
    When I create a quote for that customer with that item with the quantity 1.0, the price 100.0 and the discount 101.0
    Then the response status code is 400
    And the error message is "Discount percentage cannot exceed 100"

#  AC13: Fail to create a new quote with negative discount
  Scenario: Fail to create a new quote with negative discount
    Given a customer with identifier "CUST_013"
    And an item with identifier "ITEM_013"
    When I create a quote for that customer with that item with the quantity 1.0, the price 100.0 and the discount -10.0
    Then the response status code is 400
    And the error message is "Discount percentage cannot be negative"

#  AC14: Fail to create a new quote with negative quantity
  Scenario: Fail to create a new quote with negative quantity
    Given a customer with identifier "CUST_014"
    And an item with identifier "ITEM_014"
    When I create a quote for that customer with that item with the quantity -5.0 and the price 100.0
    Then the response status code is 400
    And the error message is "Quantity must be positive"

#  AC15: Fail to create a new quote with negative price
  Scenario: Fail to create a new quote with negative price
    Given a customer with identifier "CUST_015"
    And an item with identifier "ITEM_015"
    When I create a quote for that customer with that item with the quantity 5.0 and the price -100.0
    Then the response status code is 400
    And the error message is "Price must be positive"

#  AC16: Fail to create a new quote with zero quantity
  Scenario: Fail to create a new quote with zero quantity
    Given a customer with identifier "CUST_016"
    And an item with identifier "ITEM_016"
    When I create a quote for that customer with that item with the quantity 0.0 and the price 100.0
    Then the response status code is 400
    And the error message is "Quantity must be positive"

#  AC17: Fail to create a new quote with zero price
  Scenario: Fail to create a new quote with zero price
    Given a customer with identifier "CUST_017"
    And an item with identifier "ITEM_017"
    When I create a quote for that customer with that item with the quantity 5.0 and the price 0.0
    Then the response status code is 400
    And the error message is "Price must be positive"

#  AC18: Successfully create a new quote with zero discount
  Scenario: Successfully create a new quote with zero discount
    Given a customer with identifier "CUST_018"
    And an item with identifier "ITEM_018"
    When I create a quote for that customer with that item with the quantity 5.0, the price 100.0 and the discount 0.0
    Then the response status code is 200
    And the quote total amount is 500.0

#  AC19: Fail to create a new quote with null customer ID
  Scenario: Fail to create a new quote with null customer ID
    Given a customer with identifier ""
    And an item with identifier "ITEM_019"
    When I create a quote for that customer with that item with the quantity 5.0 and the price 100.0
    Then the response status code is 400
    And the error message is "Customer ID cannot be null or empty"

#  AC20: Fail to create a new quote with null item ID
  Scenario: Fail to create a new quote with null item ID
    Given a customer with identifier "CUST_020"
    And an item with identifier ""
    When I create a quote for that customer with that item with the quantity 5.0 and the price 100.0
    Then the response status code is 400
    And the error message is "Item ID cannot be null or empty"

#  AC21: Faile to create a new quote without authorization
  Scenario: Fail to create a new quote without authorization
#    not authorized to create a quote
#    Given an unauthorized user (no token)
    And a customer with identifier "CUST_007"
    And an item with identifier "ITEM_007"
    When I create a quote for that customer with that item with the quantity 5.0 and the price 100.0
    Then the response status code is 401
    And the error message is "Unauthorized access"









    