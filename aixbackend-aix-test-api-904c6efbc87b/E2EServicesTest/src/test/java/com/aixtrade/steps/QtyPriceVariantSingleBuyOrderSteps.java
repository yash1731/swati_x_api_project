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
public class QtyPriceVariantSingleBuyOrderSteps extends End2EndTest {

    private final WorldHelper helper;
    private Response response;
    private JsonPath jsonPath;
    private String endpoint;
    private String data;

//    public QtyPriceVariantSingleBuyOrderSteps(WorldHelper helper) {
//        this.helper = helper;
//    }

    @And("^I create a matching BUY order as trader B - createSingleOrderBuy$")
    public void iCreateAMatchingBUYOrderAsTraderBCreateSingleOrderBuy() throws Throwable {
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("03aCreateSingleOrderBuy.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        helper.getOrderIdControl().storeOrderId(orderId);
    }

    @When("^I create a Single SELL order as trader A - CreateSellOrderQtyMatchA$")
    public void iCreateASingleSELLOrderAsTraderACreateSellOrderQtyMatchA() throws Throwable{
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

    @And("^I create a Single SELL order as trader A - CreateSellOrderNoPriceMatchA$")
    public void iCreateASingleSELLOrderAsTraderACreateSellOrderNoPriceMatchA() throws Throwable {
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

    @And("^I create a Single SELL order as trader A - CreateSellOrderQtyMatchB$")
    public void iCreateASingleSELLOrderAsTraderACreateSellOrderQtyMatchB() throws Throwable{
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("08bCreateSellOrderQtyMatchB.json");
        response = helper.postVerb().postOnApi(endpoint, data);
    }
}
