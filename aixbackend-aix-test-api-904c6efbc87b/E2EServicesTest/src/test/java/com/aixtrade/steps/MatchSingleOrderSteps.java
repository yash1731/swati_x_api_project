package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.streams.kafka.OrderKafkaEventVerifier;
import com.aixtrade.streams.order.OrderStreamData;
import com.aixtrade.streams.order.OrderStreamResponse;
import com.aixtrade.streams.StreamUpdateCount;
import com.aixtrade.streams.StreamWorldHelper;
import com.aixtrade.support.WorldHelper;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.JsonReader;
import com.aixtrade.utilities.TestData;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RequiredArgsConstructor
public class MatchSingleOrderSteps extends End2EndTest {

    private Response response;
    private String endpoint;
    private int statusCode;
    private JsonPath jsonPath;
    private String data;
//    private final StreamWorldHelper streamWorldHelper;

    private final WorldHelper helper;
    private final OrderKafkaEventVerifier orderKafkaEventVerifier;

//    public MatchSingleOrderSteps(WorldHelper helper, StreamWorldHelper streamWorldHelper, OrderKafkaEventVerifier orderKafkaEventVerifier) {
//        this.helper = helper;
//        this.streamWorldHelper = streamWorldHelper;
//        this.orderKafkaEventVerifier = orderKafkaEventVerifier;
//    }


//    @Given("^I expect to receive the following stream updates$")
//    public void iExpectToReceiveTheFollowingStreamUpdates(List<StreamUpdateCount> streamUpdateCounts) {
////        streamWorldHelper.initCountDownLatch(
////                streamUpdateCounts,
////                "order",
////                streamUpdateCount -> streamWorldHelper.initOrderStreamCountDownLatch(streamUpdateCount.getCount()));
////
////    streamWorldHelper.initCountDownLatch(
////                streamUpdateCounts,
////                "quote",
////                streamUpdateCount -> streamWorldHelper.initQuoteStreamCountDownLatch(streamUpdateCount.getCount()));
////
////    streamWorldHelper.initCountDownLatch(
////                streamUpdateCounts,
////                "quoteResponse",
////                streamUpdateCount -> streamWorldHelper.initQuoteResponseStreamCountDownLatch(streamUpdateCount.getCount()));
////
////    streamWorldHelper.initCountDownLatch(
////                streamUpdateCounts,
////                "quoteRequest",
////                streamUpdateCount -> streamWorldHelper.initQuoteRequestStreamCountDownLatch(streamUpdateCount.getCount()));
//
//    }

    @When("^I create a matching SELL order as trader B - CreateSingleSellOrderMatchingBuyOrder$")
    public void iCreateAMatchingSELLOrderAsTraderBCreateSingleSellOrderMatchingBuyOrder() throws Throwable {
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("06dCreateSingleSellOrderMatchingBuyOrder.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
    }

    @And("^I search for the executed SELL order - SearchCreateSingleSellOrderMatchingBuyOrder$")
    public void iSearchForTheExecutedSELLOrderSearchCreateSingleSellOrderMatchingBuyOrder() throws Throwable {
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String searchOrderUri = TestData.getValue("searchOrder uri");
        endpoint = orderBaseUrl+searchOrderUri;
        data = JsonReader.getData("06eSearchCreateSingleSellOrderMatchingBuyOrder.json");
        response = helper.postVerb().postOnApi(endpoint, data);
    }

    @Then("^I should be able to see the SELL order with the order status as \"([^\"]*)\"$")
    public void iShouldBeAbleToSeeTheSELLOrderWithTheOrderStatusAs(String arg0) throws Throwable {
        response = helper.postVerb().getResponse();
        statusCode = response.getStatusCode();
        jsonPath = new JsonPath(response.thenReturn().asString());
        assertEquals(statusCode, 200);
    }

    @And("^an order stream payload with the following$")
    public void anOrderStreamPayloadWithTheFollowing(List<OrderStreamData> expectedOrderUpdates) {
        orderKafkaEventVerifier.assertJsonObjectMatch(expectedOrderUpdates);
    }

    private Predicate<OrderStreamResponse> orderStreamPredicate() {
        return orderStreamResponse ->
                "Open".equals(orderStreamResponse.getOrderStatus()) ||
                "Executed".equals(orderStreamResponse.getOrderStatus());
    }

    @When("^I submit a quote request via the Quote Service where order has been executed for trader A$")
    public void iSubmitAQuoteRequestViaTheQuoteServiceWhereOrderHasBeenExecutedForTraderA() throws Throwable {
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuoteRequest uri");
        String endpoint = baseEndpoint + uri;
        data = JsonReader.getData("002CreateQuoteRequest.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        int quoteRequestId = jsonPath.get("quoteRequest.quoteRequestId");
        System.out.println("Quote request id is " + quoteRequestId);
        helper.getQuoteRequestIdControl().storeQuoteRequestId(quoteRequestId);
    }

    @When("^I submit a quote request via the Quote Service where order has been executed for trader B$")
    public void iSubmitAQuoteRequestViaTheQuoteServiceWhereOrderHasBeenExecutedForTraderB() throws Throwable {
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuoteRequest uri");
        String endpoint = baseEndpoint + uri;
        data = JsonReader.getData("06aCreateQuoteRequestTraderB.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        int quoteRequestId = jsonPath.get("quoteRequest.quoteRequestId");
        System.out.println("Quote request id is " + quoteRequestId);
        helper.getQuoteRequestIdControl().storeQuoteRequestId(quoteRequestId);
    }

    @When("^I create a matching BUY order as trader B - CreateSingleBuyOrderMatchingSellOrder$")
    public void iCreateAMatchingBUYOrderAsTraderBCreateSingleBuyOrderMatchingSellOrder() throws Throwable {
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("06bCreateSingleBuyOrderMatchingSellOrder.json");
        response = helper.postVerb().postOnApi(endpoint, data);
    }

    @And("^I search for the executed BUY order - SearchCreateSingleBuyOrderMatchingSellOrder$")
    public void iSearchForTheExecutedBUYOrderSearchCreateSingleBuyOrderMatchingSellOrder() throws Throwable {
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String searchOrderUri = TestData.getValue("searchOrder uri");
        endpoint = orderBaseUrl+searchOrderUri;
        data = JsonReader.getData("06cSearchCreate SingleBuyOrderMatchingSellOrder.json");
        response = helper.postVerb().postOnApi(endpoint, data);
    }

    @Then("^I should be able to see the BUY order with the order status as \"([^\"]*)\"$")
    public void iShouldBeAbleToSeeTheBUYOrderWithTheOrderStatusAs(String arg0) throws Throwable {
        response = helper.postVerb().getResponse();
        statusCode = response.getStatusCode();
        jsonPath = new JsonPath(response.thenReturn().asString());
        assertEquals(statusCode, 200);
    }
}
