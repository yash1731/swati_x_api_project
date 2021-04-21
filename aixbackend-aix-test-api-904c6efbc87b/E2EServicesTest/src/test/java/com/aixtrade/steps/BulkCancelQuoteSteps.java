package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.schemaModel.BulkQuoteCancel;
import com.aixtrade.support.WorldHelper;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.TestData;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import static org.junit.Assert.assertEquals;

@RequiredArgsConstructor
public class BulkCancelQuoteSteps extends End2EndTest {

    private Response response;
    private final WorldHelper helper;

//    public BulkCancelQuoteSteps(WorldHelper helper) {
//        this.helper = helper;
//    }

    private void waitAfterResponse() {
        if (response.statusCode() < 400) {
            waitInMillis();
        }
    }

    private void waitInMillis() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("^I bulk cancel Quote using both \"([^\"]*)\" and \"([^\"]*)\"$")
    public void iBulkCancelQuoteUsingBothAnd(String symbol, String trader) {
        symbol = TestData.getValue("symbol requested");
        trader = TestData.getValue("traderF id");
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("bulkCancelQuote uri");
        String endpoint = baseEndpoint + uri + "/";
        BulkQuoteCancel quote = new BulkQuoteCancel();
        quote.setSymbol(symbol);
        quote.setUserId(trader);
        waitInMillis();
        response = helper.deleteVerb().deleteOnApiUsingBody(endpoint, quote);
    }

    @Then("^I can see Quote cancelled$")
    public void iCanSeeQuoteCancelled() {
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @When("^I bulk cancel Quote using only \"([^\"]*)\"$")
    public void iBulkCancelQuoteUsingOnly(String trader) {
        trader = TestData.getValue("traderF id");
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("bulkCancelQuote uri");
        String endpoint = baseEndpoint + uri + "/";
        BulkQuoteCancel quote = new BulkQuoteCancel();
        quote.setUserId(trader);
        waitInMillis();
        response = helper.deleteVerb().deleteOnApiUsingBody(endpoint, quote);
    }

    @When("^I bulk cancel Quote using \"([^\"]*)\" and \"([^\"]*)\"$")
    public void iBulkCancelQuoteUsingAnd(String buy, String trader) {
        buy = TestData.getValue("bid side");
        trader = TestData.getValue("traderF id");
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("bulkCancelQuote uri");
        String endpoint = baseEndpoint + uri + "/";
        BulkQuoteCancel quote = new BulkQuoteCancel();
        quote.setUserId(trader);
        quote.setSide(buy);
        waitInMillis();
        response = helper.deleteVerb().deleteOnApiUsingBody(endpoint, quote);
    }

    @When("^I bulk cancel Quote using offer side and \"([^\"]*)\"$")
    public void iBulkCancelQuoteUsingOfferSideAnd(String trader) {
        String offer = TestData.getValue("offer side");
        trader = TestData.getValue("traderF id");
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("bulkCancelQuote uri");
        String endpoint = baseEndpoint + uri + "/";
        BulkQuoteCancel quote = new BulkQuoteCancel();
        quote.setUserId(trader);
        quote.setSide(offer);
        waitInMillis();
        response = helper.deleteVerb().deleteOnApiUsingBody(endpoint, quote);
    }

    @When("^I bulk cancel Quote using offer side, \"([^\"]*)\" and \"([^\"]*)\"$")
    public void iBulkCancelQuoteUsingOfferSideAnd(String symbol, String trader) {
        String offer = TestData.getValue("offer side");
        symbol = TestData.getValue("symbol requested");
        trader = TestData.getValue("traderF id");
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("bulkCancelQuote uri");
        String endpoint = baseEndpoint + uri + "/";
        BulkQuoteCancel quote = new BulkQuoteCancel();
        quote.setUserId(trader);
        quote.setSide(offer);
        quote.setSymbol(symbol);
        waitInMillis();
        response = helper.deleteVerb().deleteOnApiUsingBody(endpoint, quote);
    }

    @When("^I bulk cancel Quote using \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"$")
    public void iBulkCancelQuoteUsingAnd(String bid, String symbol, String trader) {
        bid = TestData.getValue("bid side");
        symbol = TestData.getValue("symbol requested");
        trader = TestData.getValue("traderF id");
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("bulkCancelQuote uri");
        String endpoint = baseEndpoint + uri + "/";
        BulkQuoteCancel quote = new BulkQuoteCancel();
        quote.setUserId(trader);
        quote.setSide(bid);
        quote.setSymbol(symbol);
        waitInMillis();
        response = helper.deleteVerb().deleteOnApiUsingBody(endpoint, quote);
    }
}
