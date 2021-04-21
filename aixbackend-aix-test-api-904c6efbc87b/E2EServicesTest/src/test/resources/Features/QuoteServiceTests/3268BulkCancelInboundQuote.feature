@regression3268 @Quote

Feature: Bulk Cancel an inbound Quote
  As a market maker
  I want to cancel all my Quotes


  Background:
    Given I already submitted a Quote Request as Trader A
    And I can see an Outbound quote request stream payload with the following
      | symbol | quantity | channel  | direction | trader    | status |
      | LTCUSDT | 5500     | Symphony | Outbound  | 613065783 | Open   |
    And I can get the outbound Quote Request ID for the market maker - "613065783"


  @cancel @regression3268a
  Scenario: Bulk Cancel inbound Quote using both Symbol and Trader's Id
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | bid price           |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderF id          |
    Then I should be able to see the inbound quote created with a status of "PendingOpen"
    When I bulk cancel Quote using both "symbol requested" and "traderF id"
    Then I can see Quote cancelled

  @cancel @regression3268b
  Scenario: Bulk Cancel inbound Quote using only Trader's Id
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | bid price           |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderF id          |
    Then I should be able to see the inbound quote created with a status of "PendingOpen"
    When I bulk cancel Quote using only "traderF id"
    Then I can see Quote cancelled

  @cancel @regression3268c
  Scenario: Bulk Cancel inbound Quote using by Side (BUY) and Trader's Id
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | bid price           |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderF id          |
    Then I should be able to see the inbound quote created with a status of "PendingOpen"
    When I bulk cancel Quote using "bid side" and "traderF id"
    Then I can see Quote cancelled

  @cancel @regression3268d
  Scenario: Bulk Cancel inbound Quote using by Side (SELL) and Trader's Id
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | bid price           |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderF id          |
    Then I should be able to see the inbound quote created with a status of "PendingOpen"
    When I bulk cancel Quote using offer side and "traderF id"
    Then I can see Quote cancelled

  @cancel @regression3268e
  Scenario: Bulk Cancel inbound Quote using by Side (SELL), Symbol and Trader's Id
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | bid price           |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderF id          |
    Then I should be able to see the inbound quote created with a status of "PendingOpen"
    When I bulk cancel Quote using offer side, "symbol requested" and "traderF id"
    Then I can see Quote cancelled

  @cancel @regression3268f
  Scenario: Bulk Cancel inbound Quote using by Side (BUY), Symbol and Trader's Id
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | bid price           |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderF id          |
    Then I should be able to see the inbound quote created with a status of "PendingOpen"
    When I bulk cancel Quote using "bid side", "symbol requested" and "traderF id"
    Then I can see Quote cancelled
