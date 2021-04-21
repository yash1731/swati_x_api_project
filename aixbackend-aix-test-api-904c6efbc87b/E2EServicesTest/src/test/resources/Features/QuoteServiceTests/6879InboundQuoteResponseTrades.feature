@regression6879 @Quote

Feature: Create an inbound Quote Response
  As a market maker
  I want to create an inbound Quote Response
  So I can have my Quote Response executed

  @cancel @regression6879a
  Scenario: Trade happens - Inbound Quote Response (Bid) matches an existing order (Offer)
    Given I create a Single BUY and SELL order via the createTwoWayOrder endpoint as Trader A
    When I submit a quote request via the Quote Service for trader B
    Then I should see "createdQuoteEvent"
    And I can get the Quote Id from the "createdQuoteEvent" sent to trader - "145174195"
    When I create a counter by creating a Quote Response for a Bid as Trader A as below:
      | Field               | Value             |
      | ExpirationInSeconds | expIn sec         |
      | bidPrice            | matchBid2 price   |
      | bidQuantity         | quoteBid quantity |
      | Symbol              | symbol requested  |
      | UserId              | userB id          |
    Then I should be able to see the inbound Quote Response created with a status of "PendingOpen"
    Then I should see an inbound "TradedQuoteResponseEvent"


  @cancel
  Scenario: Trade happens - Inbound Quote Response (Offer) matches an existing order (Bid)
    Given I create a Single BUY and SELL order via the createTwoWayOrder endpoint as Trader A
    When I submit a quote request via the Quote Service for trader B
    Then I should see "createdQuoteEvent"
    And I can get the Quote Id from the "createdQuoteEvent" sent to trader - "145174195"
    When I create a counter by creating a Quote Response for a Bid as Trader A as below:
      | Field               | Value               |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | matchOffer price    |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | UserId              | userB id            |
    Then I should be able to see the inbound Quote Response created with a status of "PendingOpen"
    Then I should see an inbound "TradedQuoteResponseEvent"