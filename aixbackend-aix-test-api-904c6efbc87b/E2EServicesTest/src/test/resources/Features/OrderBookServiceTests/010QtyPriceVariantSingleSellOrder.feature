@regression10 @orderbook

Feature: Create a SINGLE SELL order not matching an existing BUY order - different quantity, different price
  As a trader
  I want to create a single SELL order that does not match an existing BUY order in price and quantity
  So I can have a quote for the quantity I want to sell

  Background:
    Given I have received the spot price for a "specific symbol" from the Market Data Service

  @cancel
  Scenario: Create a SELL order (5520 LTCUSDT @58) when a BUY order (5620 LTCUSDT @57) exists - Higher SELL Price but lower SELL Quantity
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    When I create a matching SELL order as trader B - createSingleOrderSell
    And I submit a quote request via the Quote Service for trader A
    Then I should get the quote which should contain a bid and an offer value
    When I submit a quote request via the Quote Service for trader B
    Then I should get the quote which should contain a bid and an offer value

  @cancel
  Scenario: Create a SELL order (5620 LTCUSDT @55)when a BUY order (5500 LTCUSDT @58) exists - Lower SELL Price but higher SELL Quantity
    When I create a Single BUY order as trader A - CreateBuyOrderQtyMatchA
    And I create a matching SELL order as trader B - CreateSellOrderNoPriceMatchB
    When I submit a quote request via the Quote Service for trader B
    Then I should get the quote which should contain an offer and an empty bid
    And the status of the offer should be "PartiallyExecuted"

  @cancel
  Scenario: Create a SELL order (5550 LTCUSDT @57)when a BUY order (5620 LTCUSDT @58) exists - Lower SELL Price and lower SELL Quantity
    When I create a Single BUY order as trader A - CreateBuyOrderQtyMatchB
    And I create a matching SELL order as trader B - CreateSellOrderQtyMatchB
    When I submit a quote request via the Quote Service for trader B
    Then I should get the quote which should contain a bid and an empty offer
    And the status of the bid should be "PartiallyExecuted"

  @cancel
  Scenario: Create a SELL order (5700 LTCUSDT @57)when a BUY order (5520 LTCUSDT @56) exists - Higher SELL Price and higher SELL Quantity
    When I create a Single BUY order as trader A - CreateBuyOrderNoPriceMatchA
    And I create a matching SELL order as trader B - CreateSellOrderQtyMatchA
    And I submit a quote request via the Quote Service for trader A
    Then I should get the quote which should contain a bid and an offer value
    When I submit a quote request via the Quote Service for trader B
    Then I should get the quote which should contain a bid and an offer value