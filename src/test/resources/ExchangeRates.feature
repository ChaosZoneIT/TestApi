Feature: Test connect to api
  Scenario: Show exchange rates fetched from API
    Given Get data from Api
    Then Correct response code
    And Find and display from downloaded data exchange rates for currency code: USD and currency name dolar amerykański
    And and show currencies whose rates are higher than 5 and lower than 3