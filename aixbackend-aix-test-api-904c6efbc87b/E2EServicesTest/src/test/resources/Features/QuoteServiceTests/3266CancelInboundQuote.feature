@regression3266 @Quote

Feature: Cancel an inbound Quote
  As a market maker
  I want to cancel my Quote

  @cancel
  Scenario: Cancel an inbound Quote
    Given I already submitted a Quote Request as Trader A
    And I can see an Outbound quote request stream payload with the following
      | symbol | quantity | channel | direction | trader    | status |
      | LTCUSDT | 5500     | Fix     | Outbound  | 256135470 | Open   |
#      | LTCUSDT | 5500     | Symphony | Outbound  | 1581079305 | Open   |
    And I can get the outbound Quote Request ID for the market maker - "256135470"
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | bid price           |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderG id          |
    Then I should be able to see the inbound quote created with a status of "PendingOpen"
    And I can get the quote Id for the created Quote
    When I cancel the quote
    Then I should be able to see both the bid and offer side of the quote "Cancelled"
