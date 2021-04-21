@regression3270 @Quote
Feature: Create an inbound Quote
  As a market maker
  I want to create an inbound Quote
  So I can have my Quote executed

  Background:
    Given I create a Single BUY and SELL order via the createTwoWayOrder endpoint as Trader A

  @cancel
  Scenario: Trade happens - Inbound Quote (Offer) matches an existing order (Bid)
    When I submit a quote request via the Quote Service for trader B
    And I can get the outbound Quote Request ID for the market maker - "1596026150"
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | matchBid1 price     |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | matchOffer price    |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderE id          |
    Then I should see an inbound "TradedQuoteEvent"

  @cancel
  Scenario: Trade happens - Inbound Quote (Bid) matches an existing order (Offer)
    When I submit a quote request via the Quote Service for trader B
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
    Then I should see an inbound "TradedQuoteEvent"
