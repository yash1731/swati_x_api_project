package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.support.WorldHelper;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.JsonReader;
import com.aixtrade.utilities.TestData;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QtyPriceVariantSingleSellOrderSteps extends End2EndTest {

    private final WorldHelper helper;
    private Response response;
    private JsonPath jsonPath;
    private String endpoint;
    private String data;

//    public QtyPriceVariantSingleSellOrderSteps(WorldHelper helper) {
//        this.helper = helper;
//    }

    @When("^I create a matching SELL order as trader B - createSingleOrderSell$")
    public void iCreateAMatchingSELLOrderAsTraderBCreateSingleOrderSell() throws Throwable{
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("04aCreateSingleOrderSell.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        helper.getOrderIdControl().storeOrderId(orderId);
    }

    @When("^I create a Single BUY order as trader A - CreateBuyOrderQtyMatchA$")
    public void iCreateASingleBUYOrderAsTraderACreateBuyOrderQtyMatchA() throws Throwable{
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("08cCreateBuyOrderQtyMatchA.json");
        response = helper.postVerb().postOnApi(endpoint, data);
    }

    @When("^I create a Single BUY order as trader A - CreateBuyOrderQtyMatchB$")
    public void iCreateASingleBUYOrderAsTraderACreateBuyOrderQtyMatchB() throws Throwable{
//        helper.baseVerb().setUp();
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("08dCreateBuyOrderQtyMatchB.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        System.out.println("found "+orderId);
        helper.getOrderIdControl().storeOrderId(orderId);
    }

    @When("^I create a Single BUY order as trader A - CreateBuyOrderNoPriceMatchA$")
    public void iCreateASingleBUYOrderAsTraderACreateBuyOrderNoPriceMatchA() throws Throwable {
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        endpoint = orderBaseUrl+orderUri;
        data = JsonReader.getData("07dCreateBuyOrderNoPriceMatchA.json");
        response = helper.postVerb().postOnApi(endpoint, data);
        jsonPath = new JsonPath(response.thenReturn().asString());
        String orderId = jsonPath.get("order.id");
        helper.getOrderIdControl().storeOrderId(orderId);
    }
}
