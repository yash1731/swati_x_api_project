@regression12 @orderbook

Feature: Create a Quote Response when a BUY and SELL order exists in the system
  As a trader
  I want to create a Quote Response when a satisfying order exists
  So I have an executed order

  Background:
    Given I have received the spot price for a "specific symbol" from the Market Data Service

  @cancel @regression12a
  Scenario: Existing Buy Order 5620 LTCUSDT and Quote Response - Offer @57 for 5500 LTCUSDT
    When I create a matching BUY order as trader B - CreateSingleBuyOrderMatchingSellOrder
    When I then submit a quote request via the Quote Service
    Then I should get the quote which should contain a bid and an empty offer
    Then I can get the Quote id for the bid
    When I accept the bid by creating a Quote Response for an offer as below:
      | Field               | Value            |
      | ExpirationInSeconds | expIn sec        |
      | OfferPrice          | offer price      |
      | OfferQuantity       | offer quantity   |
      | Symbol              | symbol requested |
      | Type                | type requested   |
      | UserId              | user id          |
    Then I should be able to see order executed

  @cancel
  Scenario: Existing Sell Order 5520 LTCUSDT and Quote Response - Buy @58 for 5500 LTCUSDT
    When I create a matching SELL order as trader B - CreateSingleSellOrderMatchingBuyOrder
    When I then submit a quote request via the Quote Service
    Then I should get the quote which should contain an offer and an empty bid
    Then I can get the Quote id for the bid
    When I accept the offer by creating a Quote Response for a bid as below:
      | Field               | Value            |
      | ExpirationInSeconds | expIn sec        |
      | bidPrice            | bid price        |
      | bidQuantity         | bid quantity     |
      | Symbol              | symbol requested |
      | Type                | type requested   |
      | UserId              | user id          |
    Then I should be able to see order executed

  @cancel
  Scenario: Existing Order 5620/5520 LTCUSDT and Quote Response - Offer @57 for 5500 LTCUSDT
    When I create a Single BUY order as trader A - CreateBuyOrderQtyMatchB
    When I create a single SELL order as trader B
    When I then submit a quote request via the Quote Service
    Then I should get the quote which should contain a bid and an offer value
    Then I can get the Quote id for the bid
    When I accept the bid by creating a Quote Response for an offer as below:
      | Field               | Value            |
      | ExpirationInSeconds | expIn sec        |
      | OfferPrice          | offer price      |
      | OfferQuantity       | offer quantity   |
      | Symbol              | symbol requested |
      | Type                | type requested   |
      | UserId              | user id          |
    Then I should be able to see order executed

  @cancel
  Scenario: Existing Order 5620/5520 LTCUSDT and Quote Response - Offer @57 for 5500 LTCUSDT
    When I create a Single BUY order as trader A - CreateBuyOrderNoPriceMatchA
    When I create a matching SELL order as trader B - CreateSingleSellOrderMatchingBuyOrder
    When I then submit a quote request via the Quote Service
    Then I should get the quote which should contain a bid and an offer value
    Then I can get the Quote id for the bid
    When I accept the offer by creating a Quote Response for a bid as below:
      | Field               | Value            |
      | ExpirationInSeconds | expIn sec        |
      | bidPrice            | bid price        |
      | bidQuantity         | bid quantity     |
      | Symbol              | symbol requested |
      | Type                | type requested   |
      | UserId              | user id          |
    Then I should be able to see order executed