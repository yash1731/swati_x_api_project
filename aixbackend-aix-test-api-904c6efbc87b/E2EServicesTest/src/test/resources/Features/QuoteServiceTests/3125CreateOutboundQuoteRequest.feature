@regression3125 @Quote

Feature: Create an Outbound Quote Request when no Order exists in the OrderBook
  As a market maker
  I want to receive an Outbound Quote Request
  So I can place a Quote when interested in the requested Quote

  @cancel
  Scenario: Outbound Quote Request when no order exist in the system
    When I submit a quote request via the Quote Service where there exists no order for that symbol
    Then I should get the quote which should contain an empty bid and an empty offer value with a status of "Open"
    And I should be able to see "createdQuoteRequestEvent"
    And I should be able to see an Inbound quote request stream payload with the following
      | symbol | quantity | channel  | direction | trader                | status |
      | LTCUSDT | 5500     | Telegram | Inbound   | beatrice@aixtrade.com | Open   |
    Then I should be able to see an Outbound quote request stream payload with the following
      | symbol | quantity | channel  | direction | trader               | status |
#      | LTCUSDT | 5500     | Symphony | Outbound  | 210509737            | Open   |
      | LTCUSDT | 5500     | Symphony | Outbound  | 1581079305           | Open   |
      | LTCUSDT | 5500     | Symphony | Outbound  | 1236361883           | Open   |
      | LTCUSDT | 5500     | Rest     | Outbound  | 1596026150           | Open   |
      | LTCUSDT | 5500     | Symphony | Outbound  | 613065783            | Open   |
      | LTCUSDT | 5500     | Symphony | Outbound  | tester6@aixtrade.com | Open   |
      | LTCUSDT | 5500     | Fix      | Outbound  | 256135470            | Open   |
