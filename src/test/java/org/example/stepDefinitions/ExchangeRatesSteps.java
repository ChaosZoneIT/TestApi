package org.example.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;

public class ExchangeRatesSteps {

    private static final String URL_API = "https://api.nbp.pl/api/exchangerates/tables/A";
    private static final int STATUS_OK = 200;
    private Response response;

    enum Rates {
        LOWER("lower"),
        HIGHER("higher");
        private final String text;

        Rates(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    @Given("Get data from Api")
    public void get_data_from_api() {
        response = given().get(URL_API).then().extract().response();
    }

    @Then("Correct response code")
    public void correct_response_code() {
        System.out.println("Response code: " + response.getStatusCode());
        response.then().statusCode(STATUS_OK);
    }

    @Then("^Find and display from downloaded data exchange rates for currency code: ([A-Z]{3}) and currency name (.*)$")
    public void find_and_display_from_downloaded_data_exchange_rates_for_currency_code_and_currency_name(String currencyCode, String currencyName) {
        String exchangeRateForCurrencyCode = from(response.asString()).getString("[0].rates.find{it.code == '" + currencyCode + "'}.mid");
        showExchangeRates(currencyCode, exchangeRateForCurrencyCode);

        String exchangeRateForCurrencyName = response.jsonPath().getString("[0].rates.find{it.currency == '" + currencyName + "'}.mid");
        showExchangeRates(currencyName, exchangeRateForCurrencyName);
    }

    @Then("and show currencies whose rates are higher than {int} and lower than {int}")
    public void and_show_currencies_whose_rates_are_higher_than_and_lower_than(Integer higherExchange, Integer lowerExchange) {
        List<String> currenciesWithRateHigher = response.jsonPath().getList("[0].rates.findAll{it.mid > " + higherExchange + "}.currency");
        showCurrenciesWhoseRatesIs(Rates.HIGHER, higherExchange, currenciesWithRateHigher);

        List<String> currenciesWithRateLower = response.jsonPath().getList("[0].rates.findAll{it.mid < " + lowerExchange + "}.currency");
        showCurrenciesWhoseRatesIs(Rates.LOWER, lowerExchange, currenciesWithRateLower);
    }

    private void showCurrenciesWhoseRatesIs(Rates rates, Integer value, List<String> currencies) {
        System.out.println("Currencies with a rate " + rates + " than " + value + ": " + String.join(", ", currencies));
    }

    private void showExchangeRates(String currency, String exchangeRateForCurrency) {
        if (exchangeRateForCurrency != null && !exchangeRateForCurrency.isBlank()) {
            System.out.println("Exchange rate for " + currency + ": " + exchangeRateForCurrency);
        } else {
            System.out.printf("No exchange rate found for: " + currency);
        }
    }
}
