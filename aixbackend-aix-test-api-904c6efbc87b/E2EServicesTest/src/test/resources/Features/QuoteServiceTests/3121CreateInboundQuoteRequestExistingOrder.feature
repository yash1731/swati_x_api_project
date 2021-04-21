@regression3121 @Quote

Feature: Create an Inbound Quote Request when a Sell and Buy Order exists in the OrderBook
  As a trader
  I want to request for a Quote when an order exists
  So I get a quote for my requested quantity

  Background:
    Given I have received the spot price for a "specific symbol" from the Market Data Service

  @cancel
  Scenario: Get A Quote for existing Orders
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    And I create a Single SELL order as market maker A - createSingleOrderSell
    When I then submit a quote request via the Quote Service within the existing order limit as Trader A
    Then I should get the quote which should contain a bid and an offer value
    And I should be able to see "createdQuoteRequestEvent"
    And I should be able to see an Inbound quote request stream payload with the following
      | symbol | quantity | channel  | direction | trader                | status |
      | LTCUSDT | 5500     | Telegram | Inbound   | beatrice@aixtrade.com | Open   |
