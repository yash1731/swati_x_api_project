@regression12 @orderbook

Feature: Create a two Way Order via the twoWayOrder Endpoint
  As a trader
  I want to ask for a quote when there exist resting order
  So I can have a quote for the quantity I want to buy

  @cancel
  Scenario: Create A Two Way Order - 5000 LTCUSDT @57/58
    Given I have received the spot price for a "specific symbol" from the Market Data Service
#    Create a Two Way Order
    When I create a Single BUY and SELL order via the createTwoWayOrder endpoint as Trader A
    Then I should be able to successfully create the two way order with a status of "PendingOpen" for both the BUY and the SELL side
#    Get a Quote
    When I then submit a quote request via the Quote Service
    Then I should get the quote which should contain a bid and an offer value
#    Cancel a Two Way Order
    When I cancel the two way order via cancelTwoWayOrder endpoint
    Then I should be able to see the two way order cancelled