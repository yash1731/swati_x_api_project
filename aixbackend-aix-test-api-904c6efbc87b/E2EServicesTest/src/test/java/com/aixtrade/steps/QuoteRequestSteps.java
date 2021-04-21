package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.streams.kafka.QuoteRequestKafkaEventVerifier;
import com.aixtrade.streams.quoterequest.QuoteRequestStreamData;
import com.aixtrade.streams.quoterequest.QuoteRequestStreamResponse;
import com.aixtrade.support.WorldHelper;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.JsonReader;
import com.aixtrade.utilities.TestData;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RequiredArgsConstructor
public class QuoteRequestSteps extends End2EndTest {

    private String endpoint;
    private Response response;
    private JsonPath jsonPath;
    private static int quoteRequestId;
    private static Long outboundQuoteRequestId;

    private final WorldHelper helper;
    private final QuoteRequestKafkaEventVerifier quoteRequestKafkaEventVerifier;

//    public QuoteRequestSteps(WorldHelper helper, StreamWorldHelper streamWorldHelper) {
//        this.helper = helper;
//        this.streamWorldHelper = streamWorldHelper;
//    }

    //    CreateInboundQuoteRequestNoExistingOrder
    @Given("^I have received the spot price for a \"([^\"]*)\" from the Market Data Service$")
    public void iHaveReceivedTheSpotPriceForAFromTheMarketDataService(String symbol) throws Throwable {
        String marketDataBaseUrl = EnvConfig.getValue("marketDataService baseUrl");
        String marketDataUri = TestData.getValue("marketData uri");
        symbol = TestData.getValue(symbol);
        endpoint = marketDataBaseUrl + marketDataUri + symbol;
        response = helper.getVerb().getApi(endpoint);
    }

    @When("^I submit a quote request via the Quote Service where there exists no order for that symbol$")
    public void iSubmitAQuoteRequestViaTheQuoteServiceWhereThereExistsNoOrderForThatSymbol() throws Throwable {
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuoteRequest uri");
        endpoint = baseEndpoint + uri;
        String data = JsonReader.getData("002CreateQuoteRequest.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        int quoteRequestId = jsonPath.get("quoteRequest.quoteRequestId");
        System.out.println("Quote request id is " + quoteRequestId);
        helper.getQuoteRequestIdControl().storeQuoteRequestId(quoteRequestId);
    }

    @Then("^I should get the quote which should contain an empty bid and an empty offer value with a status of \"([^\"]*)\"$")
    public void iShouldGetTheQuoteWhichShouldContainAnEmptyBidAndAnEmptyOfferValueWithAStatusOf(String status) throws Throwable {
        response = helper.postVerb().getResponse();
        int statusCode = response.getStatusCode();
        jsonPath = new JsonPath(response.thenReturn().asString());
        int quantity = jsonPath.get("quoteRequest.latestQuote.requestedQuantity");
        String symbol = jsonPath.get("quoteRequest.latestQuote.symbol");
        String quoteStatus = jsonPath.get("quoteRequest.latestQuote.status");
        String trader = jsonPath.get("quoteRequest.trader");
        Float bidPrice = jsonPath.get("quoteRequest.latestQuote.bid.price");
        Boolean bidQuote = jsonPath.get("quoteRequest.latestQuote.bid.isQuote");
        Float offerPrice = jsonPath.get("quoteRequest.latestQuote.offer.price");
        Boolean offerQuote = jsonPath.get("quoteRequest.latestQuote.offer.isQuote");

        System.out.println("Requested quantity is " + quantity);
        System.out.println("Quote currency is " + symbol);
        System.out.println("Trader is " + trader);

        response.getContentType();
        response.header("Connections");
        response.getCookies();

        for (Header header : response.getHeaders())
            System.out.println(header);

        assertEquals(statusCode, 201);
        //assertNull(bidPrice);
        assertThat(bidQuote, is(false));
        assertNull(offerPrice);
        assertThat(offerQuote, is(false));
        assertEquals(status, quoteStatus);
        assertEquals(response.getContentType(), "application/json;charset=UTF-8");
//        assertThat("The time entered is less than the expected time of " + response.getTime(), response.getTime(), lessThanOrEqualTo(3500L));
    }


    //    CreateInboundQuoteRequestForExistingOrder
    @When("^I then submit a quote request via the Quote Service within the existing order limit as Trader A$")
    public void iThenSubmitAQuoteRequestViaTheQuoteServiceWithinTheExistingOrderLimitAsTraderA() throws Throwable {
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuoteRequest uri");
        endpoint = baseEndpoint + uri;
        String data = JsonReader.getData("002CreateQuoteRequest.json");
        Response response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        int quoteRequestId = jsonPath.get("quoteRequest.quoteRequestId");
        System.out.println("Quote request id is " + quoteRequestId);
        helper.getQuoteRequestIdControl().storeQuoteRequestId(quoteRequestId);
    }

    @And("^I should be able to see \"([^\"]*)\"$")
    public void iShouldBeAbleToSee(String eventName) throws Throwable {
//        Thread.sleep(5000);
//        streamWorldHelper.awaitQuoteRequestStreamCountDownLatch();
//        List<QuoteRequestStreamResponse> actualQuoteRequestUpdates = streamWorldHelper.getQuoteRequestStreamUpdates();
//        List<QuoteRequestStreamResponse> events = actualQuoteRequestUpdates.stream().filter(update -> eventName.equals(update.getName()) && "LTCUSDT".equalsIgnoreCase(update.getSymbol())).collect(Collectors.toList());
//        System.out.println("eventName is " + eventName);
//        assertThat(actualQuoteRequestUpdates.stream().anyMatch(X -> X.getName().equalsIgnoreCase(eventName)), is(true));
        quoteRequestKafkaEventVerifier.assertPredicateMatch(quoteRequestStreamResponse ->
                eventName.equals(quoteRequestStreamResponse.getName()) &&
                        "LTCUSDT".equalsIgnoreCase(quoteRequestStreamResponse.getSymbol()));
    }

    @And("^I should be able to see an Inbound quote request stream payload with the following$")
    public void iShouldBeAbleToSeeAnInboundQuoteRequestStreamPayloadWithTheFollowing(List<QuoteRequestStreamData> expectedQuoteUpdates) throws InterruptedException, JSONException {
//        streamWorldHelper.awaitQuoteRequestStreamCountDownLatch();
//        List<QuoteRequestStreamResponse> actualQuoteUpdates = streamWorldHelper.getQuoteRequestStreamUpdates();
//        List<QuoteRequestStreamResponse> inboundQuoteUpdates = actualQuoteUpdates.stream().filter(update -> "Inbound".equals(update.getDirection())).collect(Collectors.toList());
//        System.out.println("actual update is " + actualQuoteUpdates);
        quoteRequestKafkaEventVerifier.assertJsonObjectMatch(expectedQuoteUpdates);
    }


    //    CreateOutboundQuoteRequest
    @Then("^I should be able to see an Outbound quote request stream payload with the following$")
    public void iShouldBeAbleToSeeAnOutboundQuoteRequestStreamPayloadWithTheFollowing(List<QuoteRequestStreamData> expectedQuoteRequestUpdates) throws InterruptedException, JSONException {
//        streamWorldHelper.awaitQuoteRequestStreamCountDownLatch();
//        List<QuoteRequestStreamResponse> actualQuoteUpdates = streamWorldHelper.getQuoteRequestStreamUpdates();
//        List<QuoteRequestStreamResponse> outboundQuoteRequestUpdates = actualQuoteUpdates.stream()
//                .filter(actualUpdate -> expectedQuoteUpdates.stream()
//                        .anyMatch(expectedUpdate ->
//                                expectedUpdate.getDirection().equals(actualUpdate.getDirection()) &&
//                                        actualUpdate.getTrader().equals(expectedUpdate.getTrader())))
//                .collect(Collectors.toList());
//        boolean outboundExists = actualQuoteUpdates.stream().anyMatch(update -> "Outbound".equals(update.getDirection()));
//        System.out.println("actual update is " + actualQuoteUpdates);
//        System.out.println("expected update is " + expectedQuoteUpdates);
        quoteRequestKafkaEventVerifier.assertJsonObjectMatch(expectedQuoteRequestUpdates);
//        assertTrue(outboundExists);
    }


    //    CancelInboundQuoteRequest
    @Then("^I can get the Quote Request id for the bid$")
    public void iCanGetTheQuoteRequestIdForTheBid() {
        response = helper.postVerb().getResponse();
        jsonPath = new JsonPath(response.thenReturn().asString());
        quoteRequestId = jsonPath.get("quoteRequest.quoteRequestId");
        System.out.println("Quote request id is " + quoteRequestId);
        helper.getQuoteRequestIdControl().storeQuoteRequestId(quoteRequestId);
    }

    @When("^I cancel the quote request$")
    public void iCancelTheQuoteRequest() throws InterruptedException {
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("cancelQuoteRequest uri");
        endpoint = baseEndpoint + uri + "/" + quoteRequestId;
//        Thread.sleep(2000);
        response = helper.deleteVerb().deleteOnApi(endpoint, null);
    }

    @Then("^I should be able to see the quote request \"([^\"]*)\"$")
    public void iShouldBeAbleToSeeTheQuoteRequest(String status) throws Throwable {
        int statusCode = response.getStatusCode();
        jsonPath = new JsonPath(response.thenReturn().asString());

        String symbol = jsonPath.get("quoteRequest.symbol");
        int quantity = jsonPath.get("quoteRequest.quantity");
        String direction = jsonPath.get("quoteRequest.direction");
        String trader = jsonPath.get("quoteRequest.trader");
        String quoteStatus = jsonPath.get("quoteRequest.status");
        System.out.println("Quantity is " + quantity);
        System.out.println("Quote currency is " + symbol);
        System.out.println("Trader is " + trader);
        System.out.println("Direction is " + direction);
        assertEquals(200, statusCode);
        assertEquals(status, quoteStatus);
    }


    //    CancelOutboundQuoteRequest
    @And("^I already submitted a Quote Request as Trader A$")
    public void iAlreadySubmittedAQuoteRequestAsTraderA() throws InterruptedException, IOException {
//        Thread.sleep(1000);
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuoteRequest uri");
        endpoint = baseEndpoint + uri;
        String data = JsonReader.getData("002CreateQuoteRequest.json");
        Response response = helper.postVerb().postOnApi(endpoint, data);
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        int quoteRequestId = jsonPath.get("quoteRequest.quoteRequestId");
        System.out.println("Quote request id is " + quoteRequestId);
        helper.getQuoteRequestIdControl().storeQuoteRequestId(quoteRequestId);
    }

    @And("^I should be able to get the outbound Quote Request ID for the market maker - \"([^\"]*)\"$")
    public void iShouldBeAbleToGetTheOutboundQuoteRequestIDForTheMarketMaker(String marketMakerId) throws InterruptedException {
//        streamWorldHelper.awaitQuoteRequestStreamCountDownLatch();
//        List<QuoteRequestStreamResponse> actualQuoteUpdates = streamWorldHelper.getQuoteRequestStreamUpdates();
        List<QuoteRequestStreamResponse> outboundQuoteUpdates = quoteRequestKafkaEventVerifier.findEventPayloads(update -> marketMakerId.equalsIgnoreCase(update.getTrader()) && "Outbound".equals(update.getDirection()) && "Open".equals(update.getStatus()));
        System.out.println("outboundQR update is " + outboundQuoteUpdates);
        QuoteRequestStreamResponse quoteStreamResponse = outboundQuoteUpdates.get(0);
        outboundQuoteRequestId = quoteStreamResponse.getQuoteRequestId();
        System.out.println(outboundQuoteRequestId);
//        quoteRequestKafkaEventVerifier.assertPredicateMatch(quoteRequestStreamResponse ->
//                marketMakerId.equalsIgnoreCase(quoteRequestStreamResponse.getTrader()) &&
//                        "Outbound".equals(quoteRequestStreamResponse.getDirection()));
    }

    @When("^I cancel the outbound quote request$")
    public void iCancelTheOutboundQuoteRequest() throws InterruptedException {
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("cancelQuoteRequest uri");
        String outbound = "/outbound/";
        endpoint = baseEndpoint + uri + outbound + outboundQuoteRequestId;
        response = helper.deleteVerb().deleteOnApi(endpoint, null);
    }

    @Then("^I should be able to see the outbound quote Request cancelled with a status of \"([^\"]*)\"$")
    public void iShouldBeAbleToSeeTheOutboundQuoteRequestCancelledWithAStatusOf(String status) {
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
//        String symbol = jsonPath.get("quoteRequest.symbol");
//        int quantity = jsonPath.get("quoteRequest.quantity");
//        String direction = jsonPath.get("quoteRequest.direction");
//        String trader = jsonPath.get("quoteRequest.trader");
//        String quoteStatus = jsonPath.get("quoteRequest.status");
//        System.out.println("Requested quantity is " + quantity);
////        System.out.println("Quote currency is " + symbol);
//        System.out.println("Trader is " + trader);
//        System.out.println("Direction is " + direction);
        assertEquals(200, statusCode);
//        assertEquals(status, quoteStatus);
    }
}
