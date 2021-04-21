@regression5 @orderbook

Feature: Create a BUY Order and a SELL Order and request for a Quote whereby both bid and offer exists - No match in Price and Quantity
  As a trader
  I want to ask for a quote if available
  So I can have a quote for the quantity I want to buy or sell

  @cancel
  Scenario: Create a Bid and Offer quote when a BUY and SELL order exists in the system
    Given I have received the spot price for a "specific symbol" from the Market Data Service
#    Create a Single Order - "Buy"
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    Then I should be able to get the details of the new order
#    Create a Single Order - "Sell"
    When I create a Single SELL order as market maker A - createSingleOrderSell
    Then I should be able to get the details of the new order - Sell
#    Get a Quote
    When I then submit a quote request via the Quote Service
    Then I should get the quote which should contain a bid and an offer value
