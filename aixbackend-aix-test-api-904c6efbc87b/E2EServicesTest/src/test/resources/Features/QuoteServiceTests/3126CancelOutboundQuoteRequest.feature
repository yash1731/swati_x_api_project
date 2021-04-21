@regression3126 @Quote

Feature: Cancel an outbound Quote Request
  As a market maker
  I want to cancel my outbound Quote Request

  Background:
    Given I have received the spot price for a "specific symbol" from the Market Data Service

  @cancel
  Scenario: Cancel an Outbound Quote Request as a market maker
    And I already submitted a Quote Request as Trader A
    Then I should be able to see an Outbound quote request stream payload with the following
      | symbol | quantity | channel  | direction | trader               | status |
#      | LTCUSDT | 5500     | Symphony | Outbound  | 210509737            | Open   |
      | LTCUSDT | 5500     | Symphony | Outbound  | 1581079305           | Open   |
      | LTCUSDT | 5500     | Symphony | Outbound  | 1236361883           | Open   |
      | LTCUSDT | 5500     | Rest     | Outbound  | 1596026150           | Open   |
      | LTCUSDT | 5500     | Symphony | Outbound  | 613065783            | Open   |
      | LTCUSDT | 5500     | Symphony | Outbound  | tester6@aixtrade.com | Open   |
      | LTCUSDT | 5500     | Fix      | Outbound  | 256135470            | Open   |
    And I should be able to get the outbound Quote Request ID for the market maker - "210509737"
    When I cancel the outbound quote request
    Then I should be able to see the outbound quote Request cancelled with a status of "Cancelled"
