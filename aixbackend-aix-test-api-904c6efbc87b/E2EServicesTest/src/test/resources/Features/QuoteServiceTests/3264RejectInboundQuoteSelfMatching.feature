@regression3264 @Quote

Feature: Reject an inbound Quote - Self-Matching Error
  As a market maker
  I want to have my Quote rejected
  So I can get a Self-Matching error

  @cancel
  Scenario: Reject an inbound Quote - Self-Matching Error for Offer - Resting Order @57/58 and Inbound Quote @56/57
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    When I create a Single SELL order as market maker A - createSingleOrderSell
    And I already submitted a Quote Request as Trader A
    And I can get the outbound Quote Request ID for the market maker - "1236361883"
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | matchBid1 price     |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | matchOffer price    |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderB id          |
#    Then I should be able to see a "Bad Request" error thrown with "400" error code
#    And I should be able to get a self matching error with "Two-way order exception" error thrown
#    And I should be able to get a self matching error for the Offer as displayed "offer: Self matching order found for price:"
    Then I should be able to see the correct status code returned
    And I should be able to see a status of "Rejected" returned

  @cancel @regression3264A
  Scenario: Reject an inbound Quote - Self-Matching Error for Bid - Resting Order @57/58 and Inbound Quote @58/58.5
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    When I create a Single SELL order as market maker A - createSingleOrderSell
    And I already submitted a Quote Request as Trader A
    And I can get the outbound Quote Request ID for the market maker - "1236361883"
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | matchBid2 price     |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | replacedOffer price |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderB id          |
#    Then I should be able to see a "Bad Request" error thrown with "400" error code
#    And I should be able to get a self matching error with "Two-way order exception" error thrown
#    And I should be able to get a self matching error for the Bid as displayed "bid: Self matching order found for price:"
    Then I should be able to see the correct status code returned
    And I should be able to see a status of "Rejected" returned

  @cancel
  Scenario: Reject a Quote Response -  Self-Matching Error for BID - Resting Order @57/58 and Inbound Quote Response Bid @58
    When I create a Single BUY and SELL order via the createTwoWayOrder endpoint as Trader A
    When I then submit a quote request via the Quote Service
    Then I should get the quote which should contain a bid and an offer value
    Then I can get the Quote id for the bid
    When I attempt to create a Quote Response for a bid as below:
      | Field               | Value            |
      | ExpirationInSeconds | expIn sec        |
      | bidPrice            | matchBid2 price  |
      | bidQuantity         | bid quantity     |
      | Symbol              | symbol requested |
      | UserId              | user id          |
#    Then I should be able to see a "Bad Request" error thrown with "400" error code
#    And I should be able to get a self matching error for the Bid as displayed "Self matching order found for price:"
    Then I should be able to see the correct status code returned
    And I should be able to see a status of "Rejected" returned for the Quote Response
