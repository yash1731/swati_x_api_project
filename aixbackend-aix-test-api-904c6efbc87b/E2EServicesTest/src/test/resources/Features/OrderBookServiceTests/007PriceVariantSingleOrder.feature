@regression7 @orderbook

Feature: Create a SINGLE SELL/BUY order that doesn't match an existing BUY/SELL order - equal quantity, different price
  As a trader
  I want to create a single order that does not match an existing order in price
  So I can have a quote for the quantity I want to sell/buy

  Background:
    Given I have received the spot price for a "specific symbol" from the Market Data Service

  @cancel
  Scenario: Create a SELL order (5620 LTCUSDT @58) when a BUY order (5620 LTCUSDT @57) exists - Equal Quantity but higher SELL price
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    And I create a matching SELL order as trader B - CreateSellOrderNoPriceMatchA
    And I submit a quote request via the Quote Service for trader A
    Then I should get the quote which should contain a bid and an offer value
    When I submit a quote request via the Quote Service for trader B
    Then I should get the quote which should contain a bid and an offer value

  @cancel
  Scenario: Create a SELL order (5620 LTCUSDT @55)when a BUY order (5620 LTCUSDT @57) exists - Equal Quantity but lower SELL price
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    And I create a matching SELL order as trader B - CreateSellOrderNoPriceMatchB
    And I search for the executed SELL order - SearchCreateSellOrderNoPriceMatchB
    Then I should be able to see the BUY order with the order status as "Executed"
    When I submit a quote request via the Quote Service for trader A
    Then I should get the quote which should contain an empty bid and an empty offer value with a status of "Open"
    When I submit a quote request via the Quote Service for trader B
    Then I should get the quote which should contain an empty bid and an empty offer value with a status of "Open"

  @cancel
  Scenario: Create a BUY order (5520 LTCUSD @56)when a SELL order (5520 LTCUSD @58) exists - Equal Quantity but lower BUY price
    When I create a Single SELL order as market maker A - createSingleOrderSell
    And I create a matching BUY order as trader B - CreateBuyOrderNoPriceMatchA
    And I submit a quote request via the Quote Service for trader A
    Then I should get the quote which should contain a bid and an offer value
    When I submit a quote request via the Quote Service for trader B
    Then I should get the quote which should contain a bid and an offer value

  @cancel
  Scenario: Create a BUY order (5520 LTCUSD @58.5)when a SELL order (5520 LTCUSD @58) exists - Equal Quantity but higher BUY price
    When I create a Single SELL order as market maker A - createSingleOrderSell
    And I create a matching BUY order as trader B - CreateBuyOrderNoPriceMatchB
    And I search for the executed BUY order - SearchCreateBuyOrderNoPriceMatchB
    Then I should be able to see the BUY order with the order status as "Open"
    When I submit a quote request via the Quote Service for trader A
    Then I should get the quote which should contain an empty bid and an empty offer value with a status of "Open"
    When I submit a quote request via the Quote Service for trader B
    Then I should get the quote which should contain an empty bid and an empty offer value with a status of "Open"

