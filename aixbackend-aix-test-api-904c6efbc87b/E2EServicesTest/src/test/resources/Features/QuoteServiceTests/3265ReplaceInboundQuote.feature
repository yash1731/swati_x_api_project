@regression3265 @Quote

Feature: Replace an inbound Quote
  As a market maker
  I want to replace my Quote
  So that I can have a better chance of having my counter executed

  Background:
    Given I already submitted a Quote Request as Trader A
    And I can see an Outbound quote request stream payload with the following
      | symbol | quantity | channel  | direction | trader               | status |
      | LTCUSDT | 5500     | Symphony | Outbound  | tester6@aixtrade.com | Open   |
    And I can get the outbound Quote Request ID for the market maker - "tester6@aixtrade.com"

  @regression3265a @cancel
  Scenario: Replace a ONE way BID Quote with a Bid - Replace Bid @57 with Bid @58
    When I create a one way Quote for only the bid side with the values below:
      | Field               | Value             |
      | bidPrice            | bid price         |
      | bidQuantity         | quoteBid quantity |
      | ExpirationInSeconds | expIn sec         |
      | Symbol              | symbol requested  |
      | trader              | traderD id        |
    And I replace the inbound quote with a one way bid quote and the values below:
      | Field            | Value             |
      | replacedBidPrice | replacedBid price |
      | bidQuantity      | quoteBid quantity |
      | Symbol           | symbol requested  |
      | trader           | traderD id        |
    Then I should be able to see the quote replaced
    And I can get the new quote Id for created Quote

  @regression3265b @cancel
  Scenario: Replace a ONE way BID Quote with an offer - Replace Bid @57 with Offer @58.5
    When I create a one way Quote for only the bid side with the values below:
      | Field               | Value             |
      | bidPrice            | bid price         |
      | bidQuantity         | quoteBid quantity |
      | ExpirationInSeconds | expIn sec         |
      | Symbol              | symbol requested  |
      | trader              | traderD id        |
    And I replace the inbound quote with a one way offer quote and the values below:
      | Field              | Value               |
      | replacedOfferPrice | replacedOffer price |
      | offerQuantity      | quoteOffer quantity |
      | Symbol             | symbol requested    |
      | trader             | traderD id          |
    Then I should be able to see the quote replaced
    And I can get the new quote Id for created Quote

  @regression3265c @cancel
  Scenario: Replace a ONE way BID Quote with a TWO way Quote - Replace Bid @57 with @57/58
    When I create a one way Quote for only the bid side with the values below:
      | Field               | Value             |
      | bidPrice            | bid price         |
      | bidQuantity         | quoteBid quantity |
      | ExpirationInSeconds | expIn sec         |
      | Symbol              | symbol requested  |
      | trader              | traderD id        |
    And I replace the inbound quote with a two way quote and the values below:
      | Field              | Value               |
      | replacedBidPrice   | replacedBid price   |
      | bidQuantity        | quoteBid quantity   |
      | replacedOfferPrice | replacedOffer price |
      | offerQuantity      | quoteOffer quantity |
      | Symbol             | symbol requested    |
      | trader             | traderD id          |
    Then I should be able to see the quote replaced
    And I can get the new quote Id for created Quote


  @regression3265d @cancel
  Scenario: Replace a ONE way OFFER Quote with a Bid - Replace Offer @58 with Bid @58
    When I create a one way Quote for only the offer side with the values below:
      | Field               | Value               |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | ExpirationInSeconds | expIn sec           |
      | Symbol              | symbol requested    |
      | trader              | traderD id          |
    And I replace the inbound quote with a one way bid quote and the values below:
      | Field            | Value             |
      | replacedBidPrice | replacedBid price |
      | bidQuantity      | quoteBid quantity |
      | Symbol           | symbol requested  |
      | trader           | traderD id        |
    Then I should be able to see the quote replaced
    And I can get the new quote Id for created Quote


  @regression3265e @cancel
  Scenario: Replace a ONE way OFFER Quote with an Offer - Replace Offer @58 with Offer @58.5
    When I create a one way Quote for only the offer side with the values below:
      | Field               | Value               |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | ExpirationInSeconds | expIn sec           |
      | Symbol              | symbol requested    |
      | trader              | traderD id          |
    And I replace the inbound quote with a one way offer quote and the values below:
      | Field              | Value               |
      | replacedOfferPrice | replacedOffer price |
      | offerQuantity      | quoteOffer quantity |
      | Symbol             | symbol requested    |
      | trader             | traderD id          |
    Then I should be able to see the quote replaced
    And I can get the new quote Id for created Quote


  @regression3265f @cancel
  Scenario: Replace a ONE way OFFER Quote with TWO way Quote - Replace Offer @58 with @57.5/58.5
    When I create a one way Quote for only the offer side with the values below:
      | Field               | Value               |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | ExpirationInSeconds | expIn sec           |
      | Symbol              | symbol requested    |
      | trader              | traderD id          |
    And I replace the inbound quote with a two way quote and the values below:
      | Field              | Value               |
      | replacedBidPrice   | replacedBid price   |
      | bidQuantity        | quoteBid quantity   |
      | replacedOfferPrice | replacedOffer price |
      | offerQuantity      | quoteOffer quantity |
      | Symbol             | symbol requested    |
      | trader             | traderD id          |
    Then I should be able to see the quote replaced
    And I can get the new quote Id for created Quote


  @regression3265g @cancel
  Scenario: Replace a TWO way Quote with ONE way bid Quote - Replace Quote @57/58 with bid @57.5
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | bid price           |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderD id          |
    And I replace the inbound quote with a one way bid quote and the values below:
      | Field            | Value             |
      | replacedBidPrice | replacedBid price |
      | bidQuantity      | quoteBid quantity |
      | Symbol           | symbol requested  |
      | trader           | traderD id        |
    Then I should be able to see the quote replaced
    And I can get the new quote Id for created Quote


  @regression3265h @cancel
  Scenario: Replace a TWO way Quote with ONE way offer Quote - Replace Quote @57/58 with Offer @58.5
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | bid price           |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderD id          |
    And I replace the inbound quote with a one way offer quote and the values below:
      | Field              | Value               |
      | replacedOfferPrice | replacedOffer price |
      | offerQuantity      | quoteOffer quantity |
      | Symbol             | symbol requested    |
      | trader             | traderD id          |
    Then I should be able to see the quote replaced
    And I can get the new quote Id for created Quote


  @regression3265i @cancel
  Scenario: Replace a TWO way Quote with another TWO way Quote with different prices - Replace Quote @57/58 with @57.5/58.5
    When I create an inbound Quote with the values below:
      | Field               | Value               |
      | bidPrice            | bid price           |
      | bidQuantity         | quoteBid quantity   |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | trader              | traderD id          |
    And I replace the inbound quote with a two way quote and the values below:
      | Field              | Value               |
      | replacedBidPrice   | replacedBid price   |
      | bidQuantity        | quoteBid quantity   |
      | replacedOfferPrice | replacedOffer price |
      | offerQuantity      | quoteOffer quantity |
      | Symbol             | symbol requested    |
      | trader             | traderD id          |
    Then I should be able to see the quote replaced
    And I can get the new quote Id for created Quote
