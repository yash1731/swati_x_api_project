@marketData

Feature: Get Price from Market Data Service
  As a trader
  I want to get the price for a specific symbol
  So I can have an idea of the quote I will get for my bid or offer


  Scenario: Get Price from Market Data Service
    Given I request to get the spot price from the Market Data Service
    When I enter the "specific symbol" on the symbol field
    Then I should be able to get the spot price as requested which should contain:
      | Models   | Values            |
      | Currency | specific currency |
      | Source   | price source      |