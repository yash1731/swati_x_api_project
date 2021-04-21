package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.support.WorldHelper;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.TestData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.*;

@RequiredArgsConstructor
public class GetPriceSteps extends End2EndTest {

    private final WorldHelper helper;
    private Response response;

//    public GetPriceSteps(WorldHelper helper) {
//        this.helper = helper;
//    }

    @Given("^I request to get the spot price from the Market Data Service$")
    public void iRequestToGetTheSpotPriceFromTheMarketDataService() {
//        helper.baseVerb().setUp();
    }

    @When("^I enter the \"([^\"]*)\" on the symbol field$")
    public void iEnterTheOnTheSymbolField(String symbol) {
        String marketDataBaseUrl = EnvConfig.getValue("marketDataService baseUrl");
        String marketDataUri = TestData.getValue("marketData uri");
        symbol = TestData.getValue(symbol);
        String endpoint = marketDataBaseUrl + marketDataUri + symbol;
        response = helper.getVerb().getApi(endpoint);
    }

    @Then("^I should be able to get the spot price as requested which should contain:$")
    public void iShouldBeAbleToGetTheSpotPriceAsRequestedWhichShouldContain(DataTable dataTable) {
        List<List<String>> data = dataTable.asLists();
        String priceCurrency = TestData.getValue(data.get(1).get(1));
        String priceSource = TestData.getValue(data.get(2).get(1));

        String stringResponse = response.thenReturn().asString();
        JsonPath jsonPath = new JsonPath(stringResponse);

        String currency = jsonPath.get("priceData.currency");
        float price = jsonPath.get("priceData.price");
        String source = jsonPath.get("priceData.source");
        String time = jsonPath.get("priceData.generatedTimeStamp");

        System.out.println("Currency is " + currency);
        System.out.println("Price for the " + currency + "is " + price);
        System.out.println("Source is from " + source);
        System.out.println("Generated timestamp is " + time);

        assertEquals(priceCurrency, currency);
        assertEquals(priceSource, source);
        assertEquals(response.statusCode(), 200);
        assertThat("The time entered is less than the expected time of " + response.getTime(), response.getTime(), lessThanOrEqualTo(2000L));
        assertEquals(response.getContentType(), "application/json;charset=UTF-8");
    }
}



