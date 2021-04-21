package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.support.WorldHelper;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.JsonReader;
import com.aixtrade.utilities.TestData;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

@RequiredArgsConstructor
public class CreateOrderSteps extends End2EndTest {

    private Response response;
    private String endpoint;
    private int statusCode;
    private JsonPath jsonPath;
    private String data;
    private static String twoWayOrderId;

    private final WorldHelper helper;

//    public CreateOrderSteps(WorldHelper helper) {
//        this.helper = helper;
//    }

    //    CreateABuyOrder
    @When("^I create a Single BUY order as market maker A - createSingleOrderBuy$")
    public void iCreateASingleBUYOrderAsMarketMakerACreateSingleOrderBuy() throws Throwable {
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl + orderUri;
        data = JsonReader.getData("03aCreateSingleOrderBuy.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        helper.getOrderIdControl().storeOrderId(orderId);
    }

    @Then("^I should be able to get the details of the new order$")
    public void iShouldBeAbleToGetTheDetailsOfTheNewOrder() {
        statusCode = response.getStatusCode();
        jsonPath = new JsonPath(response.thenReturn().asString());

        String symbol = jsonPath.get("order.symbol");
        float price = jsonPath.get("order.price");
        float quantity = jsonPath.get("order.quantity");
        String side = jsonPath.get("order.side");
        float originalQuantity = jsonPath.get("order.originalQuantity");
        float liveQuantity = jsonPath.get("order.liveQuantity");
        int executionQuantity = jsonPath.get("order.executionQuantity");
        String orderId = jsonPath.get("order.id");

        System.out.println("OrderData currency is " + symbol);
        System.out.println("Price for the order is " + price);
        System.out.println("OrderData quantity is " + quantity);
        System.out.println("OrderData side is " + side);
        System.out.println("Original OrderData quantity is " + originalQuantity);
        System.out.println("Live OrderData quantity is " + liveQuantity);
        System.out.println("Execution quantity is " + executionQuantity);
        System.out.println("OrderData Id is " + orderId);

        assertEquals(201, statusCode);
        assertEquals(response.getContentType(), "application/json;charset=UTF-8");
        assertThat("The time entered is less than the expected time of " + response.getTime(), response.getTime(), lessThanOrEqualTo(3500L));
    }

    @When("^I then submit a quote request via the Quote Service$")
    public void iThenSubmitAQuoteRequestViaTheQuoteService() throws Throwable {
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

    @Then("^I should get the quote which should contain a bid and an empty offer$")
    public void iShouldGetTheQuoteWhichShouldContainABidAndAnEmptyOffer() {
        response = helper.postVerb().getResponse();
        statusCode = response.getStatusCode();
        jsonPath = new JsonPath(response.thenReturn().asString());

        int quantity = jsonPath.get("quoteRequest.latestQuote.requestedQuantity");
        String symbol = jsonPath.get("quoteRequest.latestQuote.symbol");
        String quoteStatus = jsonPath.get("quoteRequest.latestQuote.status");
        Float bidPrice = jsonPath.get("quoteRequest.latestQuote.bid.price");
        Float offerPrice = jsonPath.get("quoteRequest.latestQuote.offer.price");

        System.out.println("Quote quantity is " + quantity);
        System.out.println("Quote currency is " + symbol);
        System.out.println("Quote bid price is " + bidPrice);
        System.out.println("Quote offer price is " + offerPrice);
        System.out.println("Quote status is " + quoteStatus);

        assertEquals(statusCode, 201);
        assertNotNull(bidPrice);
        assertNull(offerPrice);
        assertEquals(response.getContentType(), "application/json;charset=UTF-8");
//        assertThat("The time entered is less than the expected time of " + response.getTime(), response.getTime(), lessThanOrEqualTo(3500L));
    }


    //    CreateASingleSellOrder
    @When("^I create a Single SELL order as market maker A - createSingleOrderSell$")
    public void iCreateASingleSELLOrderAsMarketMakerACreateSingleOrderSell() throws Throwable {
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        String endpoint = orderBaseUrl + orderUri;
        String data = JsonReader.getData("04aCreateSingleOrderSell.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        helper.getOrderIdControl().storeOrderId(orderId);
    }

    @Then("^I should be able to get the details of the new order - Sell$")
    public void iShouldBeAbleToGetTheDetailsOfTheNewOrderSell() {
        int statusCode = response.getStatusCode();
        jsonPath = new JsonPath(response.thenReturn().asString());

        String symbol = jsonPath.get("order.symbol");
        float price = jsonPath.get("order.price");
        float quantity = jsonPath.get("order.quantity");
        String side = jsonPath.get("order.side");
        float originalQuantity = jsonPath.get("order.originalQuantity");
        float liveQuantity = jsonPath.get("order.liveQuantity");
        int executionQuantity = jsonPath.get("order.executionQuantity");
        String orderId = jsonPath.get("order.id");

        System.out.println("OrderData currency is " + symbol);
        System.out.println("Price for the order is " + price);
        System.out.println("OrderData quantity is " + quantity);
        System.out.println("OrderData side is " + side);
        System.out.println("Original OrderData quantity is " + originalQuantity);
        System.out.println("Live OrderData quantity is " + liveQuantity);
        System.out.println("Execution quantity is " + executionQuantity);
        System.out.println("OrderData Id is " + orderId);

        assertEquals(201, statusCode);
        assertEquals(response.getContentType(), "application/json;charset=UTF-8");
        assertThat("The time entered is less than the expected time of " + response.getTime(), response.getTime(), lessThanOrEqualTo(3500L));
    }

    @Then("^I should get the quote which should contain an offer and an empty bid$")
    public void iShouldGetTheQuoteWhichShouldContainAnOfferAndAnEmptyBid() {
        response = helper.postVerb().getResponse();
        int statusCode = response.getStatusCode();
        jsonPath = new JsonPath(response.thenReturn().asString());

        int quantity = jsonPath.get("quoteRequest.latestQuote.requestedQuantity");
        String symbol = jsonPath.get("quoteRequest.latestQuote.symbol");
        String quoteStatus = jsonPath.get("quoteRequest.latestQuote.status");
        Float bidPrice = jsonPath.get("quoteRequest.latestQuote.bid.price");
        Float offerPrice = jsonPath.get("quoteRequest.latestQuote.offer.price");

        System.out.println("Quote quantity is " + quantity);
        System.out.println("Quote currency is " + symbol);
        System.out.println("Quote bid price is " + bidPrice);
        System.out.println("Quote offer price is " + offerPrice);
        System.out.println("Quote status is " + quoteStatus);

        assertEquals(statusCode, 201);
        assertNull(bidPrice);
        assertNotNull(offerPrice);
        assertEquals(response.getContentType(), "application/json;charset=UTF-8");
//        assertThat("The time entered is less than the expected time of " + response.getTime(), response.getTime(), lessThanOrEqualTo(3500L));
    }

    //    CreateASingleBuyAndSellOrder
    @Then("^I should get the quote which should contain a bid and an offer value$")
    public void iShouldGetTheQuoteWhichShouldContainABidAndAnOfferValue() {
        Response response = helper.postVerb().getResponse();
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());

        int quantity = jsonPath.get("quoteRequest.latestQuote.requestedQuantity");
        String symbol = jsonPath.get("quoteRequest.latestQuote.symbol");
        String quoteStatus = jsonPath.get("quoteRequest.latestQuote.status");
        Float bidPrice = jsonPath.get("quoteRequest.latestQuote.bid.price");
        Float offerPrice = jsonPath.get("quoteRequest.latestQuote.offer.price");

        System.out.println("Quote quantity is " + quantity);
        System.out.println("Quote currency is " + symbol);
        System.out.println("Quote bid price is " + bidPrice);
        System.out.println("Quote offer price is " + offerPrice);
        System.out.println("Quote status is " + quoteStatus);

        assertEquals(201, statusCode);
        assertNotNull(bidPrice);
        assertNotNull(offerPrice);
        assertEquals(response.getContentType(), "application/json;charset=UTF-8");
//        assertThat("The time entered is less than the expected time of " + response.getTime(), response.getTime(), lessThanOrEqualTo(3500L));
    }

    //    CreateRestingOrderForTest012
    @When("^I create a single SELL order as trader B$")
    public void iCreateASingleSELLOrderAsTraderB() throws IOException {
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl + orderUri;
        String data = JsonReader.getData("12cCreateSingleSellOrder.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        System.out.println("found " + orderId);
        helper.getOrderIdControl().storeOrderId(orderId);
    }

    //    CreateAndCancelATwoWayOrder
    @When("^I create a Single BUY and SELL order via the createTwoWayOrder endpoint as Trader A$")
    public void iCreateASingleBUYAndSELLOrderViaTheCreateTwoWayOrderEndpointAsTraderA() throws IOException {
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("createTwoWayOrder uri");
        String endpoint = orderBaseUrl + orderUri;
        String data = JsonReader.getData("13aCreateTwoWayOrder.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        twoWayOrderId = jsonPath.get("twoWayOrder.twoWayOrderId");
        System.out.println("Two way order id is " + twoWayOrderId);
        helper.getOrderIdControl().storeOrderId(twoWayOrderId);
    }

    @Then("^I should be able to successfully create the two way order with a status of \"([^\"]*)\" for both the BUY and the SELL side$")
    public void iShouldBeAbleToSuccessfullyCreateTheTwoWayOrderWithAStatusOfForBothTheBUYAndTheSELLSide(String orderStatus) throws Throwable {
        statusCode = response.getStatusCode();
        jsonPath = new JsonPath(response.thenReturn().asString());
        assertEquals(200, statusCode);
        String bidStatus = jsonPath.get("twoWayOrder.bid.orderStatus");
        String offerStatus = jsonPath.get("twoWayOrder.offer.orderStatus");
        String twoWayOrderStatus = jsonPath.get("twoWayOrder.orderStatus");
        assertEquals(orderStatus, bidStatus);
        assertEquals(orderStatus, offerStatus);
        assertEquals(orderStatus, twoWayOrderStatus);
        assertEquals(response.getContentType(), "application/json;charset=UTF-8");
        assertThat("The time entered is less than the expected time of " + response.getTime(), response.getTime(), lessThanOrEqualTo(3500L));
    }

    @When("^I cancel the two way order via cancelTwoWayOrder endpoint$")
    public void iCancelTheTwoWayOrderViaCancelTwoWayOrderEndpoint() throws InterruptedException {
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("cancelTwoWayOrder uri");
        String endpoint = orderBaseUrl + orderUri;
        String endpoint2 = endpoint + "/" + twoWayOrderId;
        response = helper.deleteVerb().deleteOnApi(endpoint2, null);
    }

    @Then("^I should be able to see the two way order cancelled$")
    public void iShouldBeAbleToSeeTheTwoWayOrderCancelled() {
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }
}
