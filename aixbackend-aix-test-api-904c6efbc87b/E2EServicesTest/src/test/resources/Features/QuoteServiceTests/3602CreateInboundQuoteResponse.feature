@regression3602 @Quote

Feature:Create an inbound Quote Response
  As a trader
  I want to create a Quote Response
  So I can respond to an existing Quote


  Background:
    Given I already submitted a Quote Request as Trader A
    And I already created a Quote
    And I can get the Quote Id from the "improvedQuoteEvent" sent to trader - "beatrice@aixtrade.com"

  @cancel
  Scenario:Trader responds to a Quote by creating an Inbound Quote Response for bid @57.5 - No Trade
    When I create a counter by creating a Quote Response for a Bid as Trader A as below:
      | Field               | Value             |
      | ExpirationInSeconds | expIn sec         |
      | bidPrice            | replacedBid price |
      | bidQuantity         | quoteBid quantity |
      | Symbol              | symbol requested  |
      | UserId              | user id           |
    Then I should be able to see the inbound Quote Response created with a status of "PendingOpen"


  @cancel @regression3602A
  Scenario:Trader responds to a Quote by creating an Inbound Quote Response for offer @57.9- No Trade
    When I create a counter by creating a Quote Response for an offer as Trader A as below:
      | Field               | Value               |
      | ExpirationInSeconds | expIn sec           |
      | offerPrice          | offer price         |
      | offerQuantity       | quoteOffer quantity |
      | Symbol              | symbol requested    |
      | UserId              | user id             |
    Then I should be able to see the inbound Quote Response created with a status of "PendingOpen"

