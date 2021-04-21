@regression13 @Quote

Feature: Cancel a Quote Request
  As a trader
  I want to cancel a Quote Request when an order exists
  So I can make another Quote Request

  Background:
    Given I have received the spot price for a "specific symbol" from the Market Data Service

  @cancel
  Scenario: Cancel a Quote Request when an Order exists
    When I create a Single BUY order as market maker A - createSingleOrderBuy
    When I then submit a quote request via the Quote Service
    Then I should get the quote which should contain a bid and an empty offer
    Then I can get the Quote Request id for the bid
    When I cancel the quote request
    Then I should be able to see the quote request "Cancelled"