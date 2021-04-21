@regression4 @orderbook

Feature: Create a single SELL Order and request for a Quote when there is an existing offer
  As a trader
  I want to ask for a quote if available
  So I can have a quote for the quantity I want to sell

  @cancel
  Scenario: Single quote when a SELL order exists in the system
    Given I have received the spot price for a "specific symbol" from the Market Data Service
#    Create a Single Order - "Buy"
    When I create a Single SELL order as market maker A - createSingleOrderSell
    Then I should be able to get the details of the new order - Sell
#    Get a Quote
    When I then submit a quote request via the Quote Service
    Then I should get the quote which should contain an offer and an empty bid
