@regression3261 @Quote

Feature: Create an inbound Quote
  As a market maker
  I want to create an inbound Quote
  So I can respond to an Outbound Quote Request

  Background:
    Given I have received the spot price for a "specific symbol" from the Market Data Service

  @cancel
  Scenario: Create an inbound Quote in response to a Quote Request
    And I already submitted a Quote Request as Trader A
    And I can see an Outbound quote request stream payload with the following
      | symbol | quantity | channel  | direction | trader               | status |
      | LTCUSDT | 5500     | Symphony | Outbound  | 210509737            | Open   |
      | LTCUSDT | 5500     | Symphony | Outbound  | 1581079305           | Open   |
      | LTCUSDT | 5500     | Symphony | Outbound  | 1236361883           | Open   |
      | LTCUSDT | 5500     | Rest     | Outbound  | 1596026150           | Open   |
      | LTCUSDT | 5500     | Symphony | Outbound  | 613065783            | Open   |
      | LTCUSDT | 5500     | Symphony | Outbound  | tester6@aixtrade.com | Open   |
      | LTCUSDT | 5500     | Fix      | Outbound  | 256135470            | Open   |
    And I can get the outbound Quote Request ID for the market maker - "1581079305"
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | bid price           |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderC id          |
    Then I should be able to see the inbound quote created with a status of "PendingOpen"
    And I can get the quote Id for the created Quote
    And I should see an "inbound" "createdQuoteEvent"
    And I should see "improvedQuoteEvent"
    And an inbound quote stream payload with the following
      | symbol | requestedQuantity | channel  | direction | trader     | status |
      | LTCUSDT | 5500              | Symphony | Inbound   | 1581079305 | Open   |
    And an Outbound quote stream payload with the following
      | symbol | requestedQuantity | channel  | direction | trader                | status |
      | LTCUSDT | 5500              | Telegram | Outbound  | beatrice@aixtrade.com | Open   |
      | LTCUSDT | 5500              | Telegram | Outbound  | beatrice@aixtrade.com | Open   |
