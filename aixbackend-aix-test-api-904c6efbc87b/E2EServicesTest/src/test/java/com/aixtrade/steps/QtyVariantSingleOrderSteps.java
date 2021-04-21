package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.support.WorldHelper;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.JsonReader;
import com.aixtrade.utilities.TestData;
import io.cucumber.java.en.And;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RequiredArgsConstructor
public class QtyVariantSingleOrderSteps extends End2EndTest {

    private final WorldHelper helper;
    private Response response;
    private String endpoint;
    private String data;
    private JsonPath jsonPath;

//    public QtyVariantSingleOrderSteps(WorldHelper helper) {
//        this.helper = helper;
//    }

    @And("^I create a matching SELL order as trader B - CreateSellOrderQtyMatchA$")
    public void iCreateAMatchingSELLOrderAsTraderBCreateSellOrderQtyMatchA() throws Throwable {
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("08aCreateSellOrderQtyMatchA.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        helper.getOrderIdControl().storeOrderId(orderId);
    }

    @And("^the status of the offer should be \"([^\"]*)\"$")
    public void theStatusOfTheOfferShouldBe(String status) throws Throwable {
        response = helper.postVerb().getResponse();
        jsonPath = new JsonPath(response.thenReturn().asString());
        List<String> quoteOfferStatus = jsonPath.getList("quoteRequest.latestQuote.offer.orders.status");
        for (String offerStatus : quoteOfferStatus) {
            System.out.println("OfferData status is " + offerStatus);
            assertEquals(offerStatus, status);
        }
    }

    @And("^I create a matching SELL order as trader B - CreateSellOrderQtyMatchB$")
    public void iCreateAMatchingSELLOrderAsTraderBCreateSellOrderQtyMatchB() throws Throwable {
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("08bCreateSellOrderQtyMatchB.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        helper.getOrderIdControl().storeOrderId(orderId);
    }

    @And("^the status of the bid should be \"([^\"]*)\"$")
    public void theStatusOfTheBidShouldBe(String bidOrderStatus) throws Throwable {
        response = helper.postVerb().getResponse();
        jsonPath = new JsonPath(response.thenReturn().asString());
        List<String> quoteBidStatus = jsonPath.getList("quoteRequest.latestQuote.bid.orders.status");
        for (String bidStatus : quoteBidStatus) {
            System.out.println("BidData status is " + bidStatus);
            assertEquals(bidOrderStatus, bidStatus);
        }
    }

    @And("^I create a matching BUY order as trader B - CreateBuyOrderQtyMatchA$")
    public void iCreateAMatchingBUYOrderAsTraderBCreateBuyOrderQtyMatchA() throws Throwable {
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("08cCreateBuyOrderQtyMatchA.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        helper.getOrderIdControl().storeOrderId(orderId);
    }

    @And("^I create a matching BUY order as trader B - CreateBuyOrderQtyMatchB$")
    public void iCreateAMatchingBUYOrderAsTraderBCreateBuyOrderQtyMatchB() throws Throwable {
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("08dCreateBuyOrderQtyMatchB.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        helper.getOrderIdControl().storeOrderId(orderId);
    }
}
