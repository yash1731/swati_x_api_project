package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.support.WorldHelper;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.JsonReader;
import com.aixtrade.utilities.TestData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PriceVariantSingleOrderSteps extends End2EndTest {

    private final WorldHelper helper;
    private Response response;
    private String endpoint;
    private String data;
    private JsonPath jsonPath;

//    public PriceVariantSingleOrderSteps(WorldHelper helper) {
//        this.helper = helper;
//    }

    @When("^I create a matching SELL order as trader B - CreateSellOrderNoPriceMatchA$")
    public void iCreateAMatchingSELLOrderAsTraderBCreateSellOrderNoPriceMatchA() throws Throwable {
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("07aCreateSellOrderNoPriceMatchA.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        helper.getOrderIdControl().storeOrderId(orderId);
    }

    @When("^I submit a quote request via the Quote Service for trader A$")
    public void iSubmitAQuoteRequestViaTheQuoteServiceForTraderA() throws Throwable {
//        Thread.sleep(1000);
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

    @When("^I submit a quote request via the Quote Service for trader B$")
    public void iSubmitAQuoteRequestViaTheQuoteServiceForTraderB() throws Throwable {
//        Thread.sleep(1000);
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

    @When("^I create a matching SELL order as trader B - CreateSellOrderNoPriceMatchB$")
    public void iCreateAMatchingSELLOrderAsTraderBCreateSellOrderNoPriceMatchB() throws Throwable {
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("07bCreateSellOrderNoPriceMatchB.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        helper.getOrderIdControl().storeOrderId(orderId);
    }

    @And("^I search for the executed SELL order - SearchCreateSellOrderNoPriceMatchB$")
    public void iSearchForTheExecutedSELLOrderSearchCreateSellOrderNoPriceMatchB() throws Throwable {
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String searchOrderUri = TestData.getValue("searchOrder uri");
        endpoint = orderBaseUrl+searchOrderUri;
        data = JsonReader.getData("07cSearchCreateSellOrderNoPriceMatchB.json");
        response = helper.postVerb().postOnApi(endpoint, data);
    }

    @When("^I create a matching BUY order as trader B - CreateBuyOrderNoPriceMatchA$")
    public void iCreateAMatchingBUYOrderAsTraderBCreateBuyOrderNoPriceMatchA() throws Throwable {
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("07dCreateBuyOrderNoPriceMatchA.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        helper.getOrderIdControl().storeOrderId(orderId);
    }

    @When("^I create a matching BUY order as trader B - CreateBuyOrderNoPriceMatchB$")
    public void iCreateAMatchingBUYOrderAsTraderBCreateBuyOrderNoPriceMatchB() throws Throwable{
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("07eCreateBuyOrderNoPriceMatchB.json");
        response = helper.postVerb().postOnApi(endpoint, data);
    }

    @And("^I search for the executed BUY order - SearchCreateBuyOrderNoPriceMatchB$")
    public void iSearchForTheExecutedBUYOrderSearchCreateBuyOrderNoPriceMatchB() throws Throwable {
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String searchOrderUri = TestData.getValue("searchOrder uri");
        endpoint = orderBaseUrl+searchOrderUri;
        data = JsonReader.getData("07fSearchCreateBuyOrderNoPriceMatchB.json");
        response = helper.postVerb().postOnApi(endpoint, data);
    }
}
