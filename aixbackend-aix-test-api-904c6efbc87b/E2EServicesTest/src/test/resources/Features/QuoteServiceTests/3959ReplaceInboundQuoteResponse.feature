@regression3959 @Quote

Feature:Replace an inbound Quote Response
  As a trader
  I want to replace a Quote Response
  So that I can have a better chance of having my counter executed

  Background:
    Given I already submitted a Quote Request as Trader A
    And I already created a Quote
    And I can get the Quote Id from the "improvedQuoteEvent" sent to trader - "beatrice@aixtrade.com"

  @cancel
  Scenario:Replace a ONE way BID Quote Response with a Bid - Replace Bid @57 with Bid @57.5
    When I create a counter by creating a Quote Response for a Bid as Trader A as below:
      | Field               | Value             |
      | ExpirationInSeconds | expIn sec         |
      | bidPrice            | bid price         |
      | bidQuantity         | quoteBid quantity |
      | Symbol              | symbol requested  |
      | UserId              | user id           |
    And I replace the inbound quote response with a one way bid quote response and the values below:
      | Field            | Value             |
      | replacedBidPrice | replacedBid price |
      | bidQuantity      | quoteBid quantity |
      | Symbol           | symbol requested  |
      | UserId           | user id           |
    Then I should be able to see the inbound Quote Response replaced with a status of "PendingOpen"
    And I can get the new Quote Response Id for the Created Quote Response


  @cancel @regression3959b
  Scenario:Replace a ONE way BID Quote Response with an Offer - Replace Bid @57.5 with Offer @57.9
    When I create a counter by creating a Quote Response for a Bid as Trader A as below:
      | Field               | Value             |
      | ExpirationInSeconds | expIn sec         |
      | bidPrice            | bid price         |
      | bidQuantity         | quoteBid quantity |
      | Symbol              | symbol requested  |
      | UserId              | user id           |
    And I replace the inbound quote response with a one way offer quote response and the values below:
      | Field              | Value                |
      | replacedOfferPrice | replacedOffer2 price |
      | OfferQuantity      | quoteOffer quantity  |
      | Symbol             | symbol requested     |
      | UserId             | user id              |
    Then I should be able to see the inbound Quote Response created with a status of "PendingOpen"
    And I can get the new Quote Response Id for the Created Quote Response


  @cancel
  Scenario:Replace a ONE way OFFER Quote Response with an Offer - Replace Offer @58 with Offer @57.9
    When I create a counter by creating a Quote Response for an offer as Trader A as below:
      | Field               | Value               |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | UserId              | user id             |
    And I replace the inbound quote response with a one way offer quote response and the values below:
      | Field              | Value                |
      | replacedOfferPrice | replacedOffer2 price |
      | OfferQuantity      | quoteOffer quantity  |
      | Symbol             | symbol requested     |
      | UserId             | user id              |
    Then I should be able to see the inbound Quote Response created with a status of "PendingOpen"
    And I can get the new Quote Response Id for the Created Quote Response


  @cancel
  Scenario:Replace a ONE way OFFER Quote Response with a Bid - Replace Offer @58 with Bid @57.5
    When I create a counter by creating a Quote Response for an offer as Trader A as below:
      | Field               | Value               |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | UserId              | user id             |
    And I replace the inbound quote response with a one way bid quote response and the values below:
      | Field            | Value             |
      | replacedBidPrice | replacedBid price |
      | bidQuantity      | quoteBid quantity |
      | Symbol           | symbol requested  |
      | UserId           | user id           |
    Then I should be able to see the inbound Quote Response replaced with a status of "PendingOpen"
    And I can get the new Quote Response Id for the Created Quote Response
