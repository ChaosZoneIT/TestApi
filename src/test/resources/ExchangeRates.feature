Feature: Test connect to api
  Scenario: Show exchange rates fetched from API
    Given Get data from Api
    Then Correct response code
    And Find and display from downloaded data exchange rates for currency code: EUR and currency name dolar amerykański
    And and show currencies whose rates are higher than 4 and lower than 0.01