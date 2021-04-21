@regression3263 @Quote

Feature: Reject an inbound Quote - Overlap
  As a market maker
  I want to have a Quote I created to be rejected
  So I can get an Overlapping error

  @cancel
  Scenario: Reject an inbound Quote - Overlapping Error - Resting Order @57/58 and Inbound Quote @57/58
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    When I create a Single SELL order as market maker A - createSingleOrderSell
    And I already submitted a Quote Request as Trader A
    And I can get the outbound Quote Request ID for the market maker - "1236361883"
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | bid price           |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderB id          |
    Then I should be able to see the correct status code returned
    And I should be able to see a status of "Rejected" returned
#    Then I should be able to see a "Bad Request" error thrown with "400" error code
#    And I should be able to get an overlapping error with "Two-way order exception" error thrown
#    And I should be able to get an overlapping error for the bid as displayed "bid: Overlapping order found with orderId"
#    And I should be able to get an overlapping error for the Offer as displayed "offer: Overlapping order found with orderId"

  @cancel
  Scenario: Reject a Quote Response - Overlapping Error for BID - Resting Order @57/58 and Inbound Quote Response @57
    When I create a Single BUY and SELL order via the createTwoWayOrder endpoint as Trader A
    When I then submit a quote request via the Quote Service
    Then I should get the quote which should contain a bid and an offer value
    Then I can get the Quote id for the bid
    When I attempt to create a Quote Response for a bid as below:
      | Field               | Value            |
      | ExpirationInSeconds | expIn sec        |
      | bidPrice            | bid price        |
      | bidQuantity         | bid quantity     |
      | Symbol              | symbol requested |
      | UserId              | user id          |
#    Then I should be able to see a "Bad Request" error thrown with "400" error code
#    Then I should be able to get an overlapping error for the bid as displayed "Overlapping order found with orderId"
    Then I should be able to see the correct status code returned
    And I should be able to see a status of "Rejected" returned for the Quote Response

  @cancel @regression3263A
  Scenario: Reject an inbound Quote - Overlapping Error - Resting Order @57/58, Inbound Quote @57.5/58.5 and replace with @57/58
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    When I create a Single SELL order as market maker A - createSingleOrderSell
    And I already submitted a Quote Request as Trader A
    And I can get the outbound Quote Request ID for the market maker - "1236361883"
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | replacedBid price   |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | replacedOffer price |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderB id          |
    Then I should be able to see the inbound quote created with a status of "PendingOpen"
    And I can get the quote Id for the created Quote
    And I replace the inbound quote with a two way quote and the values below:
      | Field              | Value               |
      | replacedBidPrice   | bid price           |
      | bidQuantity        | quoteBid quantity   |
      | replacedOfferPrice | offer price         |
      | offerQuantity      | quoteOffer quantity |
      | Symbol             | symbol requested    |
      | trader             | traderB id          |
#    Then I should be able to see a "Bad Request" error thrown with "400" error code
#    And I should be able to get an overlapping error with "Two-way order exception" error thrown
#    And I should be able to get an overlapping error for the bid as displayed "bid: Overlapping order found with orderId"
#    And I should be able to get an overlapping error for the Offer as displayed "offer: Overlapping order found with orderId"
    Then I should be able to see the correct status code returned
    And I should be able to see a status of "Rejected" returned

