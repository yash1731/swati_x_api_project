@regression3123 @Quote

Feature: Create Expiring Quote Request Event when the Quote Request is about to expire
  As a trader
  I want to receive an Expiring Quote Request Event
  So I can be notified when my Quote Request is about to expire

  @cancel
  Scenario: Expiring Quote Request Event when my Quote Request is about to expire
    When I submit a Quote Request
    And my Quote Request is about to expire
    Then I should be able to see "expiringQuoteRequestEvent"
    And I should be able to see an Inbound quote request stream payload with the following
      | symbol | quantity | channel  | direction | trader                | status |
      | LTCUSDT | 5500     | Telegram | Inbound   | beatrice@aixtrade.com | Open   |
      | LTCUSDT | 5500     | Telegram | Inbound   | beatrice@aixtrade.com | Open   |
