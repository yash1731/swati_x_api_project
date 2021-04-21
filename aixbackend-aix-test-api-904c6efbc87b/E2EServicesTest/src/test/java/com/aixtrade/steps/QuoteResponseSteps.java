package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.schemaModel.Quote;
import com.aixtrade.schemaModel.QuoteResponse;
import com.aixtrade.streams.kafka.QuoteKafkaEventVerifier;
import com.aixtrade.streams.kafka.QuoteRequestKafkaEventVerifier;
import com.aixtrade.streams.quoterequest.QuoteRequestStreamResponse;
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

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RequiredArgsConstructor
public class QuoteResponseSteps extends End2EndTest {

    private Response response;
    private static Long outboundQuoteRequestId;
    private static int quoteId;
    private String endpoint;
    private static int outboundQuoteId;
    private static int inboundQuoteId;
    private JsonPath jsonPath;
    private static int replacedQuoteResponseId;

    private final WorldHelper helper;
    //    private final StreamWorldHelper streamWorldHelper;
    private final QuoteRequestKafkaEventVerifier quoteRequestKafkaEventVerifier;
    private final QuoteKafkaEventVerifier quoteKafkaEventVerifier;

//    public QuoteResponseSteps(WorldHelper helper, StreamWorldHelper streamWorldHelper) {
//        this.helper = helper;
//        this.streamWorldHelper = streamWorldHelper;
//    }

    //    Test012CreateQuoteResponseWhenRestingOrderExists
    @Then("^I can get the Quote id for the bid$")
    public void iCanGetTheQuoteIdForTheBid() {
        response = helper.postVerb().getResponse();
        jsonPath = new JsonPath(response.thenReturn().asString());
        inboundQuoteId = jsonPath.get("quoteRequest.latestQuote.quoteId");
        System.out.println("Quote id is " + inboundQuoteId);
        helper.getQuoteIdControl().storeQuoteId(inboundQuoteId);
    }

    @When("^I accept the bid by creating a Quote Response for an offer as below:$")
    public void iAcceptTheBidByCreatingAQuoteResponseForAnOfferAsBelow(DataTable dataTable) {
        List<List<String>> data = dataTable.asLists();
        String timeInSecs = TestData.getValue(data.get(1).get(1));
        String offerPrice = TestData.getValue(data.get(2).get(1));
        String offerQuantity = TestData.getValue(data.get(3).get(1));
        String symbol = TestData.getValue(data.get(4).get(1));
        String type = TestData.getValue(data.get(5).get(1));
        String userId = TestData.getValue(data.get(6).get(1));
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuoteResponse uri");
        endpoint = baseEndpoint + uri;

        QuoteResponse quoteResponse = new QuoteResponse();
        quoteResponse.setExpirationInSeconds(timeInSecs);
        quoteResponse.setOfferPrice(offerPrice);
        quoteResponse.setOfferQuantity(offerQuantity);
        quoteResponse.setSymbol(symbol);
        quoteResponse.setType(type);
        quoteResponse.setUserId(userId);
        quoteResponse.setQuoteId(inboundQuoteId);
        response = helper.postVerb().postOnApi(endpoint, quoteResponse);
    }

    @Then("^I should be able to see order executed$")
    public void iShouldBeAbleToSeeOrderExecuted() {
        int statusCode = response.getStatusCode();
        assertEquals(202, statusCode);
    }

    @When("^I accept the offer by creating a Quote Response for a bid as below:$")
    public void iAcceptTheOfferByCreatingAQuoteResponseForABidAsBelow(DataTable dataTable) {
        List<List<String>> data = dataTable.asLists();
        String timeInSecs = TestData.getValue(data.get(1).get(1));
        String bidPrice = TestData.getValue(data.get(2).get(1));
        String bidQuantity = TestData.getValue(data.get(3).get(1));
        String symbol = TestData.getValue(data.get(4).get(1));
        String type = TestData.getValue(data.get(5).get(1));
        String userId = TestData.getValue(data.get(6).get(1));

        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuoteResponse uri");
        endpoint = baseEndpoint + uri;
        QuoteResponse quoteResponse = new QuoteResponse();
        quoteResponse.setExpirationInSeconds(timeInSecs);
        quoteResponse.setBidPrice(bidPrice);
        quoteResponse.setBidQuantity(bidQuantity);
        quoteResponse.setSymbol(symbol);
        quoteResponse.setType(type);
        quoteResponse.setUserId(userId);
        quoteResponse.setQuoteId(inboundQuoteId);
        response = helper.postVerb().postOnApi(endpoint, quoteResponse);
    }

    //    Test3602CreateQuoteResponseInResponseToAQuote
    @And("^I already created a Quote$")
    public void iAlreadyCreatedAQuote() throws InterruptedException {
        String trader = TestData.getValue("traderE id");
//        streamWorldHelper.awaitQuoteRequestStreamCountDownLatch();
//        List<QuoteRequestStreamResponse> actualQuoteRequestUpdates = streamWorldHelper.getQuoteRequestStreamUpdates();
        List<QuoteRequestStreamResponse> outboundQuoteRequestUpdates =
                quoteRequestKafkaEventVerifier.findEventPayloads(quoteRequestStreamResponse ->
                        trader.equalsIgnoreCase(quoteRequestStreamResponse.getTrader()) &&
                                "Outbound".equals(quoteRequestStreamResponse.getDirection())&& "createdQuoteRequestEvent".equals(quoteRequestStreamResponse.getName()));
        QuoteRequestStreamResponse quoteStreamResponse = outboundQuoteRequestUpdates.get(0);
        outboundQuoteRequestId = quoteStreamResponse.getQuoteRequestId();

        String bidPrice = TestData.getValue("bid price");
        String bidQuantity = TestData.getValue("quoteBid quantity");
        String timeInSecs = TestData.getValue("expIn sec");
        String offerPrice = TestData.getValue("offer price");
        String offerQuantity = TestData.getValue("quoteOffer quantity");
        String symbol = TestData.getValue("symbol requested");
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
        response = helper.postVerb().postOnApi(endpoint, quote);
        jsonPath = new JsonPath(response.thenReturn().asString());
        quoteId = jsonPath.get("quote.quoteId");
        System.out.println("Quote id is " + quoteId);
        helper.getQuoteIdControl().storeQuoteId(quoteId);
    }

    @And("^I can get the Quote Id from the \"([^\"]*)\" sent to trader - \"([^\"]*)\"$")
    public void iCanGetTheQuoteIdFromTheSentToTrader(String eventName, String trader) throws Throwable {
//        streamWorldHelper.awaitQuoteStreamCountDownLatch();
//        List<QuoteStreamResponse> actualQuoteUpdates = streamWorldHelper.getQuoteStreamUpdates();
        List<QuoteStreamResponse> outboundQuoteUpdates =
                quoteKafkaEventVerifier.findEventPayloads(quoteStreamResponse ->
                        eventName.equals(quoteStreamResponse.getName()) &&
                                trader.equalsIgnoreCase(quoteStreamResponse.getTrader()));
//        List<QuoteStreamResponse> outboundQuoteUpdates = actualQuoteUpdates.stream().filter(update -> eventName.equals(update.getName()) &&
//                trader.equalsIgnoreCase(update.getTrader())).
//                collect(Collectors.toList());
        QuoteStreamResponse quoteStreamResponse = outboundQuoteUpdates.get(0);
        outboundQuoteId = quoteStreamResponse.getQuoteId();
        System.out.println("Outbound Quote Response Id is " + outboundQuoteId);
    }

    @When("^I create a counter by creating a Quote Response for a Bid as Trader A as below:$")
    public void iCreateACounterByCreatingAQuoteResponseForABidAsTraderAAsBelow(DataTable dataTable) {
        List<List<String>> data = dataTable.asLists();
        String timeInSecs = TestData.getValue(data.get(1).get(1));
        String bidPrice = TestData.getValue(data.get(2).get(1));
        String bidQuantity = TestData.getValue(data.get(3).get(1));
        String symbol = TestData.getValue(data.get(4).get(1));
        String userId = TestData.getValue(data.get(5).get(1));

        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuoteResponse uri");
        endpoint = baseEndpoint + uri;
        QuoteResponse quoteResponse = new QuoteResponse();
        quoteResponse.setExpirationInSeconds(timeInSecs);
        quoteResponse.setBidPrice(bidPrice);
        quoteResponse.setBidQuantity(bidQuantity);
        quoteResponse.setSymbol(symbol);
        quoteResponse.setUserId(userId);
        quoteResponse.setQuoteId(outboundQuoteId);
        response = helper.postVerb().postOnApi(endpoint, quoteResponse);
    }


    @Then("^I should be able to see the inbound Quote Response created with a status of \"([^\"]*)\"$")
    public void iShouldBeAbleToSeeTheInboundQuoteResponseCreatedWithAStatusOf(String status) {
        int statusCode = response.getStatusCode();
        jsonPath = new JsonPath(response.thenReturn().asString());
        String quoteStatus = jsonPath.get("quoteResponse.status");
        assertEquals(202, statusCode);
        assertEquals(status, quoteStatus);
    }

    @When("^I create a counter by creating a Quote Response for an offer as Trader A as below:$")
    public void iCreateACounterByCreatingAQuoteResponseForAnOfferAsTraderAAsBelow(DataTable dataTable) {
        List<List<String>> data = dataTable.asLists();
        String timeInSecs = TestData.getValue(data.get(1).get(1));
        String offerPrice = TestData.getValue(data.get(2).get(1));
        String offerQuantity = TestData.getValue(data.get(3).get(1));
        String symbol = TestData.getValue(data.get(4).get(1));
        String userId = TestData.getValue(data.get(5).get(1));

        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuoteResponse uri");
        endpoint = baseEndpoint + uri;
        QuoteResponse quoteResponse = new QuoteResponse();
        quoteResponse.setExpirationInSeconds(timeInSecs);
        quoteResponse.setOfferPrice(offerPrice);
        quoteResponse.setOfferQuantity(offerQuantity);
        quoteResponse.setSymbol(symbol);
        quoteResponse.setUserId(userId);
        quoteResponse.setQuoteId(outboundQuoteId);
        response = helper.postVerb().postOnApi(endpoint, quoteResponse);
    }

    //    Test3263-AttemptToCreateQuoteResponse
    @When("^I attempt to create a Quote Response for a bid as below:$")
    public void iAttemptToCreateAQuoteResponseForABidAsBelow(DataTable dataTable) {
        List<List<String>> data = dataTable.asLists();
        String timeInSecs = TestData.getValue(data.get(1).get(1));
        String bidPrice = TestData.getValue(data.get(2).get(1));
        String bidQuantity = TestData.getValue(data.get(3).get(1));
        String symbol = TestData.getValue(data.get(4).get(1));
        String userId = TestData.getValue(data.get(5).get(1));
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuoteResponse uri");
        endpoint = baseEndpoint + uri;
        QuoteResponse quoteResponse = new QuoteResponse();
        quoteResponse.setExpirationInSeconds(timeInSecs);
        quoteResponse.setBidPrice(bidPrice);
        quoteResponse.setBidQuantity(bidQuantity);
        quoteResponse.setSymbol(symbol);
        quoteResponse.setUserId(userId);
        quoteResponse.setQuoteId(inboundQuoteId);
        response = helper.postVerb().postOnApi(endpoint, quoteResponse);
    }


    //    ReplaceQuoteResponse
    @And("^I replace the inbound quote response with a one way bid quote response and the values below:$")
    public void iReplaceTheInboundQuoteResponseWithAOneWayBidQuoteResponseAndTheValuesBelow(DataTable dataTable) throws InterruptedException {
        List<List<String>> data = dataTable.asLists();
        String bidPrice = TestData.getValue(data.get(1).get(1));
        String bidQuantity = TestData.getValue(data.get(2).get(1));
        String symbol = TestData.getValue(data.get(3).get(1));
        String userId = TestData.getValue(data.get(4).get(1));
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("replaceQuoteResponse uri");
        endpoint = baseEndpoint + uri;
        QuoteResponse quoteResponse = new QuoteResponse();
        quoteResponse.setBidPrice(bidPrice);
        quoteResponse.setBidQuantity(bidQuantity);
        quoteResponse.setSymbol(symbol);
        quoteResponse.setUserId(userId);
        quoteResponse.setQuoteId(outboundQuoteId);
        System.out.println("inbound quote id is" + outboundQuoteId);
        Thread.sleep(2000);
        response = helper.postVerb().postOnApi(endpoint, quoteResponse);
    }

    @Then("^I should be able to see the inbound Quote Response replaced with a status of \"([^\"]*)\"$")
    public void iShouldBeAbleToSeeTheInboundQuoteResponseReplacedWithAStatusOf(String status) throws Throwable {
        int statusCode = response.getStatusCode();
        jsonPath = new JsonPath(response.thenReturn().asString());
        String quoteStatus = jsonPath.get("quoteResponse.status");
        assertEquals(202, statusCode);
        assertEquals(status, quoteStatus);
    }

    @And("^I replace the inbound quote response with a one way offer quote response and the values below:$")
    public void iReplaceTheInboundQuoteResponseWithAOneWayOfferQuoteResponseAndTheValuesBelow(DataTable dataTable) throws InterruptedException {
        List<List<String>> data = dataTable.asLists();
        String offerPrice = TestData.getValue(data.get(1).get(1));
        String offerQuantity = TestData.getValue(data.get(2).get(1));
        String symbol = TestData.getValue(data.get(3).get(1));
        String userId = TestData.getValue(data.get(4).get(1));
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("replaceQuoteResponse uri");
        endpoint = baseEndpoint + uri;
        QuoteResponse quoteResponse = new QuoteResponse();
        quoteResponse.setOfferPrice(offerPrice);
        quoteResponse.setOfferQuantity(offerQuantity);
        quoteResponse.setSymbol(symbol);
        quoteResponse.setUserId(userId);
        quoteResponse.setQuoteId(outboundQuoteId);
        System.out.println("outbound quote id is" + outboundQuoteId);
        response = helper.postVerb().postOnApi(endpoint, quoteResponse);
    }

    @And("^I can get the new Quote Response Id for the Created Quote Response$")
    public void iCanGetTheNewQuoteResponseIdForTheCreatedQuoteResponse() throws InterruptedException {
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        replacedQuoteResponseId = jsonPath.get("quoteResponse.quoteResponseId");
        System.out.println("New Quote Response id is " + replacedQuoteResponseId);
        helper.getQuoteResponseIdControl().storeQuoteResponseId(replacedQuoteResponseId);
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("cancelInboundQuoteResponse uri");
        String endpoint = baseEndpoint + uri + "/" + replacedQuoteResponseId;
        response = helper.deleteVerb().deleteOnApi(endpoint, null);
    }
}
