package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.schemaModel.Quote;
import com.aixtrade.streams.kafka.QuoteKafkaEventVerifier;
import com.aixtrade.streams.kafka.QuoteRequestKafkaEventVerifier;
import com.aixtrade.streams.quoterequest.QuoteRequestStreamData;
import com.aixtrade.streams.quoterequest.QuoteRequestStreamResponse;
import com.aixtrade.streams.quote.QuoteStreamData;
import com.aixtrade.streams.quote.QuoteStreamResponse;
import com.aixtrade.streams.StreamWorldHelper;
import com.aixtrade.support.WorldHelper;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.TestData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RequiredArgsConstructor
public class QuoteSteps extends End2EndTest {

    private Response response;
    private static Long outboundQuoteRequestId;
    private static int quoteId;
    private String endpoint;
    private static int replacedQuoteId;

    private final WorldHelper helper;
    private final StreamWorldHelper streamWorldHelper;
    private final QuoteRequestKafkaEventVerifier quoteRequestKafkaEventVerifier;
    private final QuoteKafkaEventVerifier quoteKafkaEventVerifier;

//    public QuoteSteps(WorldHelper helper, StreamWorldHelper streamWorldHelper) {
//        this.helper = helper;
//        this.streamWorldHelper = streamWorldHelper;
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

    @And("^I can see an Outbound quote request stream payload with the following$")
    public void iCanSeeAnOutboundQuoteRequestStreamPayloadWithTheFollowing(List<QuoteRequestStreamData> expectedQuoteRequestUpdates) throws InterruptedException {
//        streamWorldHelper.awaitQuoteRequestStreamCountDownLatch();
//        List<QuoteRequestStreamResponse> actualQuoteRequestUpdates = streamWorldHelper.getQuoteRequestStreamUpdates();
//        actualQuoteRequestUpdates.stream().anyMatch(update -> "Outbound".equals(update.getDirection()));
//        actualQuoteRequestUpdates.stream().filter(update -> "Outbound".equals(update.getDirection())).collect(Collectors.toList());
        quoteRequestKafkaEventVerifier.assertJsonObjectMatch(expectedQuoteRequestUpdates);
    }

    @And("^I can get the outbound Quote Request ID for the market maker - \"([^\"]*)\"$")
    public void iCanGetTheOutboundQuoteRequestIDForTheMarketMaker(String marketMakerId) throws InterruptedException {
//        streamWorldHelper.awaitQuoteRequestStreamCountDownLatch();
//        List<QuoteRequestStreamResponse> actualQuoteRequestUpdates = streamWorldHelper.getQuoteRequestStreamUpdates();
        List<QuoteRequestStreamResponse> outboundQuoteRequestUpdates =
                quoteRequestKafkaEventVerifier.findEventPayloads(quoteRequestStreamResponse ->
                        marketMakerId.equalsIgnoreCase(quoteRequestStreamResponse.getTrader()) &&
                                "Outbound".equals(quoteRequestStreamResponse.getDirection()) && "createdQuoteRequestEvent".equals(quoteRequestStreamResponse.getName()));
//                List<QuoteRequestStreamResponse> outboundQuoteRequestUpdates = actualQuoteRequestUpdates.stream().filter(update -> marketMakerId.equalsIgnoreCase(update.getTrader()) && "Outbound".equals(update.getDirection())).collect(Collectors.toList());
        QuoteRequestStreamResponse quoteStreamResponse = outboundQuoteRequestUpdates.get(0);
        outboundQuoteRequestId = quoteStreamResponse.getQuoteRequestId();
        System.out.println("Outbound Quote Request Id is " + outboundQuoteRequestId);
    }

    //  CREATE QUOTE
    @When("^I create an inbound Quote with the values below:$")
    public void iCreateAnInboundQuoteWithTheValuesBelow(DataTable dataTable) throws InterruptedException {
        List<List<String>> data = dataTable.asLists();
        String bidPrice = TestData.getValue(data.get(1).get(1));
        String bidQuantity = TestData.getValue(data.get(2).get(1));
        String timeInSecs = TestData.getValue(data.get(3).get(1));
        String offerPrice = TestData.getValue(data.get(4).get(1));
        String offerQuantity = TestData.getValue(data.get(5).get(1));
        String symbol = TestData.getValue(data.get(6).get(1));
        String trader = TestData.getValue(data.get(7).get(1));
        helper.getQuoteIdControl().storeTraderId(trader);
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuote uri");
        endpoint = baseEndpoint + uri;

        Quote quote = new Quote();
        quote.setBidPrice(bidPrice);
        quote.setBidQuantity(bidQuantity);
        quote.setExpirationInSeconds(timeInSecs);
        quote.setOfferPrice(offerPrice);
        quote.setOfferQuantity(offerQuantity);
        quote.setSymbol(symbol);
        quote.setTrader(trader);
        quote.setQuoteRequestId(outboundQuoteRequestId);
//        waitInMillis();
        response = helper.postVerb().postOnApi(endpoint, quote);
        waitAfterResponse();
    }


    @Then("^I should be able to see the inbound quote created with a status of \"([^\"]*)\"$")
    public void iShouldBeAbleToSeeTheInboundQuoteCreatedWithAStatusOf(String status) {
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        String quoteStatus = jsonPath.get("quote.status");
        assertEquals(202, statusCode);
        assertEquals(status, quoteStatus);
    }

    @And("^I can get the quote Id for the created Quote$")
    public void iCanGetTheQuoteIdForTheCreatedQuote() {
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        quoteId = jsonPath.get("quote.quoteId");
        System.out.println("Quote id is " + quoteId);
        helper.getQuoteIdControl().storeQuoteId(quoteId);
    }


    @And("I should see {string}")
    public void iShouldSee(String eventName) {
        List<QuoteStreamResponse> events =
                quoteKafkaEventVerifier.findEventPayloads(update -> eventName.equals(update.getName())
                        && "Outbound".equalsIgnoreCase(update.getDirection()));
        System.out.println("eventName is " + eventName);
        assertTrue(events.size() >= 1);
        assertThat(events.stream().anyMatch(X -> X.getName().equalsIgnoreCase(eventName)), is(true));
    }

    @And("I should see an {string} {string}")
    public void iShouldSeeAn(String inbound, String eventName) {
        List<QuoteStreamResponse> events =
                quoteKafkaEventVerifier.findEventPayloads(update -> eventName.equals(update.getName())
                        && inbound.equalsIgnoreCase(update.getDirection()));
        System.out.println("eventName is " + eventName);
        assertTrue(events.size() >= 1);
        assertThat(events.stream().anyMatch(X -> X.getName().equalsIgnoreCase(eventName)), is(true));
    }

    @Then("I should see an inbound {string}")
    public void iShouldSeeAnInbound(String eventName) {
        List<QuoteStreamResponse> events =
                quoteKafkaEventVerifier.findEventPayloads(update -> eventName.equalsIgnoreCase(update.getName())
                        && "Inbound".equalsIgnoreCase(update.getDirection()));
        System.out.println("eventName is " + eventName);
        assertTrue(events.size() >= 1);
        assertThat(events.stream().anyMatch(X -> X.getName().equalsIgnoreCase(eventName)), is(true));
    }

    @And("^an inbound quote stream payload with the following$")
    public void anInboundQuoteStreamPayloadWithTheFollowing(List<QuoteStreamData> expectedQuoteUpdates) throws InterruptedException, JSONException {
//        streamWorldHelper.awaitQuoteStreamCountDownLatch();
//        List<QuoteStreamResponse> actualQuoteUpdates = streamWorldHelper.getQuoteStreamUpdates();
//        List<QuoteStreamResponse> inboundQuoteUpdates = actualQuoteUpdates.stream().filter(update -> "Inbound".equals(update.getDirection())).collect(Collectors.toList());
//        System.out.println("actual update is " + actualQuoteUpdates);
//        System.out.println("expected update is " + expectedQuoteUpdates);
//        streamWorldHelper.assertJsonObjectMatch(expectedQuoteUpdates, inboundQuoteUpdates);
        quoteKafkaEventVerifier.assertJsonObjectMatch(expectedQuoteUpdates);
    }

    @And("^an Outbound quote stream payload with the following$")
    public void anOutboundQuoteStreamPayloadWithTheFollowing(List<QuoteStreamData> expectedQuoteUpdates) throws JSONException {
//        List<QuoteStreamResponse> actualQuoteUpdates = streamWorldHelper.getQuoteStreamUpdates();
//        boolean outboundExists = actualQuoteUpdates.stream().anyMatch(update -> "Outbound".equals(update.getDirection()));
//        List<QuoteStreamResponse> outboundQuoteUpdates = actualQuoteUpdates.stream().filter(update -> "Outbound".equals(update.getDirection())).collect(Collectors.toList());
//        System.out.println("actual update is " + actualQuoteUpdates);
//        System.out.println("expected update is " + expectedQuoteUpdates);
//        streamWorldHelper.assertJsonObjectMatch(expectedQuoteUpdates, outboundQuoteUpdates);
//        assertTrue(outboundExists);
        quoteKafkaEventVerifier.assertJsonObjectMatch(expectedQuoteUpdates);
    }

    //  CANCEL QUOTE
    @When("^I cancel the quote$")
    public void iCancelTheQuote() {
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("cancelQuote uri");
        String endpoint = baseEndpoint + uri + "/" + quoteId;
        response = helper.deleteVerb().deleteOnApi(endpoint, null);
    }

    @Then("^I should be able to see both the bid and offer side of the quote \"([^\"]*)\"$")
    public void iShouldBeAbleToSeeBothTheBidAndOfferSideOfTheQuote(String status) throws Throwable {
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        String bidStatus = jsonPath.get("quote.bid.orders.get(0).status");
        String offerStatus = jsonPath.get("quote.offer.orders.get(0).status");
        assertEquals(statusCode, 200);
//        assertEquals(status, bidStatus);
//        assertEquals(status, offerStatus);
    }

    //  REPLACE QUOTE
    @When("^I create a one way Quote for only the bid side with the values below:$")
    public void iCreateAOneWayQuoteForOnlyTheBidSideWithTheValuesBelow(DataTable dataTable) {
        List<List<String>> data = dataTable.asLists();
        String bidPrice = TestData.getValue(data.get(1).get(1));
        String bidQuantity = TestData.getValue(data.get(2).get(1));
        String timeInSecs = TestData.getValue(data.get(3).get(1));
        String symbol = TestData.getValue(data.get(4).get(1));
        String trader = TestData.getValue(data.get(5).get(1));
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuote uri");
        endpoint = baseEndpoint + uri;
        Quote quote = new Quote();
        quote.setBidPrice(bidPrice);
        quote.setBidQuantity(bidQuantity);
        quote.setExpirationInSeconds(timeInSecs);
        quote.setSymbol(symbol);
        quote.setTrader(trader);
        quote.setQuoteRequestId(outboundQuoteRequestId);
        response = helper.postVerb().postOnApi(endpoint, quote);
        waitAfterResponse();
    }

    @And("^I replace the inbound quote with a one way bid quote and the values below:$")
    public void iReplaceTheInboundQuoteWithAOneWayBidQuoteAndTheValuesBelow(DataTable dataTable) throws InterruptedException {
        List<List<String>> data = dataTable.asLists();
        String bidPrice = TestData.getValue(data.get(1).get(1));
        String bidQuantity = TestData.getValue(data.get(2).get(1));
        String symbol = TestData.getValue(data.get(3).get(1));
        String trader = TestData.getValue(data.get(4).get(1));
        helper.getQuoteIdControl().storeTraderId(trader);
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("replaceQuote uri");
        endpoint = baseEndpoint + uri;
        Quote quote = new Quote();
        quote.setBidPrice(bidPrice);
        quote.setBidQuantity(bidQuantity);
        quote.setSymbol(symbol);
        quote.setTrader(trader);
        quote.setQuoteRequestId(outboundQuoteRequestId);
        response = helper.postVerb().postOnApi(endpoint, quote);
        waitAfterResponse();
    }

    @And("^I replace the inbound quote with a one way offer quote and the values below:$")
    public void iReplaceTheInboundQuoteWithAOneWayOfferQuoteAndTheValuesBelow(DataTable dataTable) throws InterruptedException {
        List<List<String>> data = dataTable.asLists();
        String offerPrice = TestData.getValue(data.get(1).get(1));
        String offerQuantity = TestData.getValue(data.get(2).get(1));
        String symbol = TestData.getValue(data.get(3).get(1));
        String trader = TestData.getValue(data.get(4).get(1));
        helper.getQuoteIdControl().storeTraderId(trader);
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("replaceQuote uri");
        endpoint = baseEndpoint + uri;
        Quote quote = new Quote();
        quote.setOfferPrice(offerPrice);
        quote.setOfferQuantity(offerQuantity);
        quote.setSymbol(symbol);
        quote.setTrader(trader);
        quote.setQuoteRequestId(outboundQuoteRequestId);
        response = helper.postVerb().postOnApi(endpoint, quote);
        waitAfterResponse();
    }

    @And("^I replace the inbound quote with a two way quote and the values below:$")
    public void iReplaceTheInboundQuoteWithATwoWayQuoteAndTheValuesBelow(DataTable dataTable) throws InterruptedException {
        List<List<String>> data = dataTable.asLists();
        String bidPrice = TestData.getValue(data.get(1).get(1));
        String bidQuantity = TestData.getValue(data.get(2).get(1));
        String offerPrice = TestData.getValue(data.get(3).get(1));
        String offerQuantity = TestData.getValue(data.get(4).get(1));
        String symbol = TestData.getValue(data.get(5).get(1));
        String trader = TestData.getValue(data.get(6).get(1));
        helper.getQuoteIdControl().storeTraderId(trader);
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("replaceQuote uri");
        endpoint = baseEndpoint + uri;
        Quote quote = new Quote();
        quote.setBidPrice(bidPrice);
        quote.setBidQuantity(bidQuantity);
        quote.setOfferPrice(offerPrice);
        quote.setOfferQuantity(offerQuantity);
        quote.setSymbol(symbol);
        quote.setTrader(trader);
        quote.setQuoteRequestId(outboundQuoteRequestId);
        response = helper.postVerb().postOnApi(endpoint, quote);
        waitAfterResponse();
    }

    @When("^I create a one way Quote for only the offer side with the values below:$")
    public void iCreateAOneWayQuoteForOnlyTheOfferSideWithTheValuesBelow(DataTable dataTable) {
        List<List<String>> data = dataTable.asLists();
        String offerPrice = TestData.getValue(data.get(1).get(1));
        String offerQuantity = TestData.getValue(data.get(2).get(1));
        String timeInSecs = TestData.getValue(data.get(3).get(1));
        String symbol = TestData.getValue(data.get(4).get(1));
        String trader = TestData.getValue(data.get(5).get(1));
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuote uri");
        endpoint = baseEndpoint + uri;
        Quote quote = new Quote();
        quote.setOfferPrice(offerPrice);
        quote.setOfferQuantity(offerQuantity);
        quote.setExpirationInSeconds(timeInSecs);
        quote.setSymbol(symbol);
        quote.setTrader(trader);
        quote.setQuoteRequestId(outboundQuoteRequestId);
        response = helper.postVerb().postOnApi(endpoint, quote);
        waitAfterResponse();
    }

    @Then("^I should be able to see the quote replaced$")
    public void iShouldBeAbleToSeeTheQuoteReplaced() {
        int statusCode = response.getStatusCode();
        assertEquals(202, statusCode);
    }

    @And("^I can get the new quote Id for created Quote$")
    public void iCanGetTheNewQuoteIdForCreatedQuote() throws InterruptedException {
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        replacedQuoteId = jsonPath.get("quote.quoteId");
        System.out.println("New Quote id is " + replacedQuoteId);
        helper.getQuoteIdControl().storeQuoteId(replacedQuoteId);
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("cancelQuote uri");
        String endpoint = baseEndpoint + uri + "/" + replacedQuoteId;
        response = helper.deleteVerb().deleteOnApi(endpoint, null);
    }
}
