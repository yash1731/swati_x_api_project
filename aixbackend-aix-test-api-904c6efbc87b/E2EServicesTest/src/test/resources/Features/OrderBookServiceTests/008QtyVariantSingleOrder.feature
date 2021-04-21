@regression8 @orderbook

Feature: Create a SINGLE SELL/BUY order that doesn't match an existing BUY/SELL order - different quantity, equal price
  As a trader
  I want to create a single order that does not match an existing order in quantity
  So I can have a quote for the quantity I want to sell/buy

  Background:
    Given I have received the spot price for a "specific symbol" from the Market Data Service

  @cancel
  Scenario: Create a SELL order (5700 LTCUSDT @57) when a BUY order (5620 LTCUSDT @57) exists - Equal Price but higher SELL Quantity
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    And I create a matching SELL order as trader B - CreateSellOrderQtyMatchA
    When I submit a quote request via the Quote Service for trader B
    Then I should get the quote which should contain an offer and an empty bid
    And the status of the offer should be "PartiallyExecuted"

  @cancel
  Scenario: Create a SELL order (5550 LTCUSDT @57)when a BUY order (5620 LTCUSDT @57) exists - Equal Price but lower SELL Quantity
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    And I create a matching SELL order as trader B - CreateSellOrderQtyMatchB
    When I submit a quote request via the Quote Service for trader B
    Then I should get the quote which should contain a bid and an empty offer
    And the status of the bid should be "PartiallyExecuted"

  @cancel
  Scenario: Create a BUY order (5500 LTCUSD @58)when a SELL order (5520 LTCUSD @58) exists - Equal Price but lower BUY Quantity
    When I create a Single SELL order as market maker A - createSingleOrderSell
    And I create a matching BUY order as trader B - CreateBuyOrderQtyMatchA
    When I submit a quote request via the Quote Service for trader B
    Then I should get the quote which should contain an offer and an empty bid
    And the status of the offer should be "PartiallyExecuted"

  @cancel
  Scenario: Create a BUY order (5620 LTCUSD @58)when a SELL order (5520 LTCUSD @58) exists - Equal Price but higher BUY Quantity
    When I create a Single SELL order as market maker A - createSingleOrderSell
    And I create a matching BUY order as trader B - CreateBuyOrderQtyMatchB
    When I submit a quote request via the Quote Service for trader B
    Then I should get the quote which should contain a bid and an empty offer
    And the status of the bid should be "PartiallyExecuted"
