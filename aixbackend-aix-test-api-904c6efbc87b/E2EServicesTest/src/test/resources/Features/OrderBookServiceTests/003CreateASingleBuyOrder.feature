@regression3 @orderbook

Feature: Create a single BUY Order and request for a Quote when there is an existing bid
  As a trader
  I want to ask for a quote if available
  So I can have a quote for the quantity I want to buy

  @cancel
  Scenario: Single quote when a BUY order exists in the system
    Given I have received the spot price for a "specific symbol" from the Market Data Service
#    Create a Single Order - "Buy"
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    Then I should be able to get the details of the new order
#    Get a Quote
    When I then submit a quote request via the Quote Service
    Then I should get the quote which should contain a bid and an empty offer
